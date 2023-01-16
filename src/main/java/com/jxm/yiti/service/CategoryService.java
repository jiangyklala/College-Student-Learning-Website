package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.Category;
import com.jxm.yiti.domain.CategoryExample;
import com.jxm.yiti.mapper.CategoryMapper;
import com.jxm.yiti.req.CategoryQueryReq;
import com.jxm.yiti.req.CategorySaveReq;
import com.jxm.yiti.resp.CategoryQueryResp;
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
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    /**
     * 查询下载列表的所有数据, 带有模糊匹配功能
     */
    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {  // 动态 SQL
            criteria.andNameLike("%" + req.getName() + "%");  // 模糊匹配条件
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<Category> categorys = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> categoryPageInfo = new PageInfo<>(categorys); // 记得这里需要初始化
        LOG.info("当前页: " + categoryPageInfo.getPageNum() + ", 总页数: " + categoryPageInfo.getPages() + " , 总记录数: " + categoryPageInfo.getTotal()); //        + " , 总页数: " + categoryPageInfo.getTotal()

        PageResp<CategoryQueryResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(categorys, CategoryQueryResp.class));
        resp.setTotal(categoryPageInfo.getTotal());

        return resp;

    }

    /**
     * 新增或者更新一个下载项
     */
    public int save(CategorySaveReq req) {
        SnowFlakeIdWorker snowFlakeIdWorker = new SnowFlakeIdWorker(0, 0);
        Category res = CopyUtil.copy(req, Category.class);

        try {
            if (ObjectUtils.isEmpty(req.getId())) {
                res.setId(snowFlakeIdWorker.nextId());
                return categoryMapper.insert(res);
            } else {
                return categoryMapper.updateByPrimaryKey(res);
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
        return categoryMapper.deleteByPrimaryKey(id);
    }
}
