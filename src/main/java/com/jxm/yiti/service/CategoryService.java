package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.Category;
import com.jxm.yiti.domain.CategoryExample;
import com.jxm.yiti.mapper.CategoryMapper;
import com.jxm.yiti.req.CategoryQueryReq;
import com.jxm.yiti.req.CategorySaveReq;
import com.jxm.yiti.resp.CategoryQueryResp;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.utils.CopyUtil;
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
    public PageResp<CategoryQueryResp> selectAll(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
//        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<Category> categorys = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> categoryPageInfo = new PageInfo<>(categorys);
        LOG.info("当前页: " + categoryPageInfo.getPageNum()
                + ", 总页数: " + categoryPageInfo.getPages()
                + " , 总记录数: " + categoryPageInfo.getTotal());
        PageResp<CategoryQueryResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(categorys, CategoryQueryResp.class));
        resp.setTotal(categoryPageInfo.getTotal());

        return resp;

    }

    /**
     * 新增或者更新一个分类
     */
    public int save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);

        try {
            // 是更新某个分类信息
            if (!ObjectUtils.isEmpty(req.getId())) {
                return categoryMapper.updateByPrimaryKeySelective(category);
            }

            // 是新增某个分类
            return categoryMapper.insertSelective(category);
        } catch (DataIntegrityViolationException e) {
            LOG.error("错误: 插入或更新错误", e);
            return -1;
        }
    }

    /**
     * 删除某个分类, 及其所有一级子分类
     */
    public void delete(CommonResp resp, Integer id) {
        try {
            // 删除所有 parent == id 的分类
            CategoryExample categoryExample = new CategoryExample();
            CategoryExample.Criteria criteria = categoryExample.createCriteria();
            criteria.andParentEqualTo(id);
            categoryMapper.deleteByExample(categoryExample);

            // 删除 id = id 的分类
            categoryMapper.deleteByPrimaryKey(id);
        } catch (RuntimeException e) {
            resp.setMessage("删除分类失败");
        }
    }

    /**
     * 查询分类表的所有数据, 不带分页功能, 并按 sort 字段排序
     */
    public List<CategoryQueryResp> selectAllOBSort(Integer type) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (type != -1) {
            criteria.andTypeEqualTo(type);
        }
        List<Category> categorys = categoryMapper.selectByExample(categoryExample);

        return CopyUtil.copyList(categorys, CategoryQueryResp.class);
    }

    public List<CategoryQueryResp> selectByTypeAndLevel(Integer type, Integer level) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (type != -1) {
            criteria.andTypeEqualTo(type);
        }
        if (level != -1) {
            criteria.andLevelEqualTo(type);
        }
        List<Category> categorys = categoryMapper.selectByExample(categoryExample);

        return CopyUtil.copyList(categorys, CategoryQueryResp.class);
    }
}
