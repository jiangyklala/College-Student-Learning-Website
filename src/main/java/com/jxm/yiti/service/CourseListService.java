package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.CourseList;
import com.jxm.yiti.domain.CourseListExample;
import com.jxm.yiti.domain.CourseList;
import com.jxm.yiti.domain.CourseListExample;
import com.jxm.yiti.mapper.CourseListMapper;
import com.jxm.yiti.mapper.CourseListMapper;
import com.jxm.yiti.req.CourseListQueryReq;
import com.jxm.yiti.req.CourseListSaveReq;
import com.jxm.yiti.req.CourseListQueryReq;
import com.jxm.yiti.req.CourseListSaveReq;
import com.jxm.yiti.resp.CourseListQueryResp;
import com.jxm.yiti.resp.CourseListQueryResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CourseListService {

    @Resource
    private CourseListMapper courseListMapper;

    private static final Logger LOG = LoggerFactory.getLogger(CourseListService.class);


    /**
     * 查询下载列表的所有数据, 带有模糊匹配功能
     */
    public PageResp<CourseListQueryResp> list(CourseListQueryReq req) {
        CourseListExample courseListExample = new CourseListExample();
        CourseListExample.Criteria criteria = courseListExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {  // 动态 SQL
            criteria.andNameLike("%" + req.getName() + "%");  // 模糊匹配条件
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<CourseList> courseLists = courseListMapper.selectByExample(courseListExample);
        PageInfo<CourseList> courseListPageInfo = new PageInfo<>(courseLists); // 记得这里需要初始化
        LOG.info("当前页: " + courseListPageInfo.getPageNum() + ", 总页数: " + courseListPageInfo.getPages() + " , 总记录数: " + courseListPageInfo.getTotal()); //        + " , 总页数: " + courseListPageInfo.getTotal()

        PageResp<CourseListQueryResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(courseLists, CourseListQueryResp.class));
        resp.setTotal(courseListPageInfo.getTotal());

        return resp;

    }

    /**
     * 新增或者更新一个下载项
     */
    public int save(CourseListSaveReq req) {
        SnowFlakeIdWorker snowFlakeIdWorker = new SnowFlakeIdWorker(0, 0);
        CourseList res = CopyUtil.copy(req, CourseList.class);

        try {
            if (ObjectUtils.isEmpty(req.getId())) {
                res.setId(snowFlakeIdWorker.nextId());
                return courseListMapper.insert(res);
            } else {
                return courseListMapper.updateByPrimaryKey(res);
            }
        } catch (DataIntegrityViolationException e) {
            LOG.info("错误: 插入或更新错误");
            return -1;
        }
    }

    /**
     * 删除一个下载项
     */
    public int delete(Long id) {
        return courseListMapper.deleteByPrimaryKey(id);
    }

}
