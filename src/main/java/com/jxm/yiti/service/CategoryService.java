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
        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {                                              // 动态 SQL
            criteria.andNameLike("%" + req.getName() + "%");                                    // 模糊匹配条件
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
     * 新增或者更新一个下载项
     */
    public int save(CategorySaveReq req) {
        Category res = CopyUtil.copy(req, Category.class);

        try {
            if (ObjectUtils.isEmpty(req.getId())) {
                res.setId(Long.valueOf(res.getSort()));     // 如果是新增操作, 则令其 sort 字段的值作为 id
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
     * 删除 1 个目录项
     */
    public int delete(Long id) {
        int res = categoryMapper.deleteByPrimaryKey(id);
        if (res != 1) {
            LOG.info("删除 1 个目录项失败");
            return 0;
        } else {
            return res;
        }
    }

    /**
     * 查询分类表的所有数据, 不带分页功能, 并按 sort 字段排序
     */
    public List<CategoryQueryResp> selectAllOBSort() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");                                   //  按 sort 这个字段, asc
        List<Category> categorys = categoryMapper.selectByExample(categoryExample);

        return CopyUtil.copyList(categorys, CategoryQueryResp.class);
    }

    public List<CategoryQueryResp> selectPracticeOBSort() {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andSortGreaterThan(999);
        categoryExample.setOrderByClause("sort asc");                                   //  按 sort 这个字段, asc
        List<Category> categorys = categoryMapper.selectByExample(categoryExample);

        return CopyUtil.copyList(categorys, CategoryQueryResp.class);
    }
}
