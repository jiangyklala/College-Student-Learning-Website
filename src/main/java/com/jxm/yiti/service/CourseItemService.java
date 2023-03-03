package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.CourseItem;
import com.jxm.yiti.domain.CourseItemExample;
import com.jxm.yiti.domain.CourseList;
import com.jxm.yiti.mapper.CourseItemMapper;
import com.jxm.yiti.req.CourseItemQueryReq;
import com.jxm.yiti.req.CourseItemSaveReq;
import com.jxm.yiti.resp.CourseItemQueryResp;
import com.jxm.yiti.resp.CourseItemQueryResp;
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
public class CourseItemService {

    @Resource
    private CourseItemMapper courseItemMapper;

    private static final Logger LOG = LoggerFactory.getLogger(CourseItemService.class);

    public PageResp<CourseItemQueryResp> select(CourseItemQueryReq req) {
        CourseItemExample courseItemExample = new CourseItemExample();
        CourseItemExample.Criteria criteria = courseItemExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getCourse())) {                                    // 动态 SQL
            criteria.andCourseLike("%" + req.getCourse() + "%");                        // 模糊匹配条件
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<CourseItem> courseItems = courseItemMapper.selectByExample(courseItemExample);

        PageInfo<CourseItem> courseItemPageInfo = new PageInfo<>(courseItems);
        LOG.info("当前页: "
                + courseItemPageInfo.getPageNum()
                + ", 总页数: " + courseItemPageInfo.getPages()
                + " , 总记录数: " + courseItemPageInfo.getTotal());
        PageResp<CourseItemQueryResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(courseItems, CourseItemQueryResp.class));
        resp.setTotal(courseItemPageInfo.getTotal());

        return resp;
    }

    /**
     * 新增或者更新一个课程视频
     */
    public int save(CourseItemSaveReq req) {
        SnowFlakeIdWorker snowFlakeIdWorker = new SnowFlakeIdWorker(0, 0);
        CourseItem res = CopyUtil.copy(req, CourseItem.class);
        CourseItemExample courseItemExample = new CourseItemExample();
        CourseItemExample.Criteria criteria = courseItemExample.createCriteria();

        try {
            if (ObjectUtils.isEmpty(req.getId())) {
                res.setId(snowFlakeIdWorker.nextId());
                return courseItemMapper.insert(res);
            } else {
                criteria.andIdEqualTo(req.getId());
                return courseItemMapper.updateByExample(res, courseItemExample);
            }
        } catch (DataIntegrityViolationException e) {
            LOG.info("错误: 插入或更新错误");
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * 删除一个课程视频, 需要用到 id 列
     */
    public int delete(Long id) {
        CourseItemExample courseItemExample = new CourseItemExample();
        CourseItemExample.Criteria criteria = courseItemExample.createCriteria();
        criteria.andIdEqualTo(id);

        return courseItemMapper.deleteByExample(courseItemExample);
    }

}
