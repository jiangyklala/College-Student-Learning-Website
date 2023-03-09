package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.ColumnList;
import com.jxm.yiti.domain.ColumnListExample;
import com.jxm.yiti.domain.ColumnList;
import com.jxm.yiti.mapper.ColumnListMapper;
import com.jxm.yiti.req.ColumnListQueryReq;
import com.jxm.yiti.req.ColumnListSaveReq;
import com.jxm.yiti.resp.ColumnListQueryResp;
import com.jxm.yiti.resp.ColumnListQueryResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ColumnListService {

    @Resource
    private ColumnListMapper columnListMapper;

    private static final Logger LOG = LoggerFactory.getLogger(ColumnListService.class);

    /**
     * 查询专栏列表的所有数据, 带有模糊匹配功能
     */
    public PageResp<ColumnListQueryResp> selectAll(ColumnListQueryReq req) {
        ColumnListExample columnListExample = new ColumnListExample();
        ColumnListExample.Criteria criteria = columnListExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {                                      // 动态 SQL
            criteria.andNameLike("%" + req.getName() + "%");                            // 模糊匹配条件
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<ColumnList> columnLists = columnListMapper.selectByExample(columnListExample);

        PageInfo<ColumnList> columnListPageInfo = new PageInfo<>(columnLists);
        LOG.info("当前页: " + columnListPageInfo.getPageNum()
                + ", 总页数: " + columnListPageInfo.getPages()
                + " , 总记录数: " + columnListPageInfo.getTotal());
        PageResp<ColumnListQueryResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(columnLists, ColumnListQueryResp.class));
        resp.setTotal(columnListPageInfo.getTotal());

        return resp;

    }

    /**
     * 新增或者更新一个专栏项
     */
    public int save(ColumnListSaveReq req) {
        SnowFlakeIdWorker snowFlakeIdWorker = new SnowFlakeIdWorker(0, 0);
        ColumnList res = CopyUtil.copy(req, ColumnList.class);

        try {
            if (ObjectUtils.isEmpty(req.getId())) {
                res.setId(snowFlakeIdWorker.nextId());
                return columnListMapper.insert(res);
            } else {
                return columnListMapper.updateByPrimaryKey(res);
            }
        } catch (DataIntegrityViolationException e) {
            LOG.info("错误: 插入或更新错误");
            return -1;
        }
    }

    /**
     * 删除 1 个专栏项
     */
    public int delete(Long id) {
        int res = columnListMapper.deleteByPrimaryKey(id);
        if (res != 1) {
            LOG.info("删除 1 个课程项失败");
            return 0;
        } else {
            return res;
        }
    }

    /**
     * 根据 categoryId2 查询: 在同一分类下的所有专栏项
     */
    public List<ColumnListQueryResp> selectByCategoryId2(ColumnListQueryReq req) {
        ColumnListExample columnListExample = new ColumnListExample();
        if (req.getCategoryId2() != -1) {
            ColumnListExample.Criteria criteria = columnListExample.createCriteria();
            criteria.andCategoryId2EqualTo(req.getCategoryId2());                            // 匹配 categoryId2 相同的的专栏项
        }

        List<ColumnList> columnLists = columnListMapper.selectByExample(columnListExample);

        List<ColumnListQueryResp> resp = CopyUtil.copyList(columnLists, ColumnListQueryResp.class);
        return resp;
    }

    /**
     * 查询所有课程, 按 categoryId1 分组, 每组按 categoryId1 升序排序
     */
    public List<List<ColumnListQueryResp>> selectAllGpByCgId2() {

        List<ColumnList> columnLists = columnListMapper.selectByExample(null);
        List<ColumnListQueryResp> columnListQueryResps = CopyUtil.copyList(columnLists, ColumnListQueryResp.class);     // 先转化为查询类
        Map<Long, List<ColumnListQueryResp>> columnListQueryRespsMap                                                    // 再将其按 categoryId1 分组
                = columnListQueryResps.stream().collect(Collectors.groupingBy(ColumnListQueryResp::getCategoryId1));
        List<List<ColumnListQueryResp>> resList                                                                         // 转换为 List
                = new ArrayList<>(columnListQueryRespsMap.values());
        resList.sort((o1, o2) -> {                                                                                      // 传入比较器, 按 categoryId1 升序排序
            long res = o1.get(0).getCategoryId1() - o2.get(0).getCategoryId1();
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
