package com.jxm.yiti.controller;

import com.jxm.yiti.req.CategoryQueryReq;
import com.jxm.yiti.req.CategorySaveReq;
import com.jxm.yiti.resp.CategoryQueryResp;
import com.jxm.yiti.resp.CommonResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.service.CategoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 查询下载列表的所有数据
     *
     * @param req 查询参数
     */
    @GetMapping("/selectAll")
    @ResponseBody
    public CommonResp list(CategoryQueryReq req) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();

        PageResp<CategoryQueryResp> list = categoryService.selectAll(req);

        resp.setContent(list);
        return resp;
    }

    /**
     * 查询分类列表某个模块的所有分类
     *
     * @param type 全部分类 == -1; 微信分类 == 1
     */
    @GetMapping("/selectAllOBSort/{type}")
    @ResponseBody
    public CommonResp selectAllOBSort(@PathVariable Integer type) {
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();

        List<CategoryQueryResp> list = categoryService.selectAllOBSort(type);

        resp.setContent(list);
        return resp;
    }

    /**
     * 查询所有分类中, 某个模块下的, 某个层级的所有分类
     *
     * @param type  -1 -- 全部分类;
     *              1 -- 微信小程序普通面试题分类;
     *              2 -- 微信小程序大厂面经分类;
     *              3 -- 微信小程序场景题分类;
     * @param level -1 -- 查全部;
     *              n -- 查某级
     */
    @Cacheable(cacheNames = "category/selectByTypeAndLevel", keyGenerator = "myKeyGenerator")
    @GetMapping("/selectByTypeAndLevel/{type}/{level}")
    @ResponseBody
    public CommonResp selectByTypeAndLevel(@PathVariable Integer type, @PathVariable Integer level) {
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();

        List<CategoryQueryResp> list = categoryService.selectByTypeAndLevel(type, level);

        resp.setContent(list);
        log.info("没有获取到缓存, 更新缓存");
        return resp;
    }

    /**
     * 新增或者更新一个下载项
     *
     * @param req 保存参数
     * @return CommonResp
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "category/selectByTypeAndLevel", allEntries = true)
    })
    @PostMapping("/save")
    @ResponseBody
    public CommonResp save(@RequestBody CategorySaveReq req) {  // 以 json 方式提交
        CommonResp resp = new CommonResp();

        if (categoryService.save(req) != 1) {
            resp.setSuccess(false);
            resp.setMessage("保存下载项失败");
        }

        return resp;
    }

    /**
     * 重置所有题目的 题目总数
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "category/selectByTypeAndLevel", allEntries = true)
    })
    @GetMapping("/resetCount")
    @ResponseBody
    public CommonResp resetCount() {
        CommonResp resp = new CommonResp();

        categoryService.resetCount();

        return resp;
    }

    /**
     * 删除某个分类, 及其所有一级子分类
     *
     * @param id 所删除下载项的 id
     * @return CommonResp
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "category/selectByTypeAndLevel", allEntries = true)
    })
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public CommonResp delete(@PathVariable Integer id) {  // 拿到 "/delete/{id}" 里的 id
        CommonResp resp = new CommonResp();

        categoryService.delete(resp, id);

        return resp;
    }

}
