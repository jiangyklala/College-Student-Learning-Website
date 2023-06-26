package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.QuestionDetail;
import com.jxm.yiti.domain.QuestionDetailExample;
import com.jxm.yiti.mapper.QuestionDetailMapper;
import com.jxm.yiti.req.QuestionDetailQueryReq;
import com.jxm.yiti.req.QuestionDetailSaveReq;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.resp.QuestionDetailQueryResp;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class PracticeProblemsService {

    @Resource
    private QuestionDetailMapper questionDetailMapper;

    private static final Logger LOG = LoggerFactory.getLogger(PracticeProblemsService.class);

    public PracticeProblemsService() throws IOException, TimeoutException {
    }

    /**
     * 查询下载列表的所有数据, 带有模糊匹配功能
     */
    public PageResp<QuestionDetailQueryResp> selectAll(QuestionDetailQueryReq req) {
        QuestionDetailExample questionDetailExample = new QuestionDetailExample();
        QuestionDetailExample.Criteria criteria = questionDetailExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {                                      // 动态 SQL
            criteria.andNameLike("%" + req.getName() + "%");                            // 模糊匹配条件
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<QuestionDetail> questionDetails = questionDetailMapper.selectByExample(questionDetailExample);

        PageInfo<QuestionDetail> questionDetailPageInfo = new PageInfo<>(questionDetails);
        LOG.info("当前页: " + questionDetailPageInfo.getPageNum()
                + ", 总页数: " + questionDetailPageInfo.getPages()
                + " , 总记录数: " + questionDetailPageInfo.getTotal());
        PageResp<QuestionDetailQueryResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(questionDetails, QuestionDetailQueryResp.class));
        resp.setTotal(questionDetailPageInfo.getTotal());

        return resp;

    }

    /**
     * 新增或者更新一个下载项
     */
    public int save(QuestionDetailSaveReq req) {
        SnowFlakeIdWorker snowFlakeIdWorker = new SnowFlakeIdWorker(0, 0);
        QuestionDetail res = CopyUtil.copy(req, QuestionDetail.class);

        try {
            if (ObjectUtils.isEmpty(req.getId())) {
                res.setId(snowFlakeIdWorker.nextId());
                return questionDetailMapper.insert(res);
            } else {
                return questionDetailMapper.updateByPrimaryKey(res);
            }
        } catch (DataIntegrityViolationException e) {
            LOG.info("错误: 插入或更新错误");
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 删除 1 个下载项
     */
    public int delete(Long id) {
        int res = questionDetailMapper.deleteByPrimaryKey(id);
        if (res != 1) {
            LOG.info("删除 1 个课程项失败");
            return 0;
        } else {
            return res;
        }
    }

    /**
     * 根据 categoryId2 查询: 在同一分类下的所有下载项
     */
    public PageResp<QuestionDetailQueryResp> selectByCategoryId(QuestionDetailQueryReq req) {
        QuestionDetailExample questionDetailExample = new QuestionDetailExample();
        if (req.getCategoryId2() != -1) {
            QuestionDetailExample.Criteria criteria = questionDetailExample.createCriteria();
            criteria.andCategoryId2EqualTo(req.getCategoryId2());                            // 匹配 categoryId2 相同的的下载项
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<QuestionDetail> questionDetails = questionDetailMapper.selectByExample(questionDetailExample);

        PageInfo<QuestionDetail> questionDetailPageInfo = new PageInfo<>(questionDetails);
        LOG.info("当前页: " + questionDetailPageInfo.getPageNum()
                + ", 总页数: " + questionDetailPageInfo.getPages()
                + " , 总记录数: " + questionDetailPageInfo.getTotal());
        PageResp<QuestionDetailQueryResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(questionDetails, QuestionDetailQueryResp.class));
        resp.setTotal(questionDetailPageInfo.getTotal());

        return resp;
    }

    public List<List<QuestionDetailQueryResp>> selectAllGpByCgId1() {
        List<QuestionDetail> questionDetailsList = questionDetailMapper.selectByExample(null);
        List<QuestionDetailQueryResp> questionDetailQueryResps = CopyUtil.copyList(questionDetailsList, QuestionDetailQueryResp.class);     // 先转化为查询类
        Map<Long, List<QuestionDetailQueryResp>> questionDetailQueryMap                                                    // 再将其按 categoryId1 分组
                = questionDetailQueryResps.stream().collect(Collectors.groupingBy(QuestionDetailQueryResp::getCategoryId1));
        List<List<QuestionDetailQueryResp>> resList                                                                         // 转换为 List
                = new ArrayList<>(questionDetailQueryMap.values());
        resList.sort((o1, o2) -> {                                                                                      // 传入比较器, 按 categoryId2 升序排序
            long res = o1.get(0).getCategoryId2() - o2.get(0).getCategoryId2();
            if (res > 0) {
                return 1;
            } else if (res < 0) {
                return -1;
            } else {
                return 0;
            }
        });

        return resList;
    }
}
