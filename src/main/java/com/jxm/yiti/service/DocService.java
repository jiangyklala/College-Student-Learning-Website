package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.Doc;
import com.jxm.yiti.domain.DocExample;
import com.jxm.yiti.mapper.DocMapper;
import com.jxm.yiti.req.DocQueryReq;
import com.jxm.yiti.req.DocSaveReq;
import com.jxm.yiti.resp.DocQueryResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Service
public class DocService {

    @Resource
    private DocMapper docMapper;

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);


    /**
     * 查询下载列表的所有数据, 带有模糊匹配功能
     */
    public List<DocQueryResp> selectAll(DocQueryReq req) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {                                      // 动态 SQL
            criteria.andNameLike("%" + req.getName() + "%");                            // 模糊匹配条件
        }

        List<Doc> docs = docMapper.selectByExample(docExample);

        List<DocQueryResp> resp = CopyUtil.copyList(docs, DocQueryResp.class);
        return resp;

    }

    /**
     * 查询下载列表的所有数据, 带有模糊匹配功能
     */
    public List<DocQueryResp> selectByColumnId(Long columnId) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andColumnIdEqualTo(columnId);

        List<Doc> docs = docMapper.selectByExample(docExample);

        List<DocQueryResp> resp = CopyUtil.copyList(docs, DocQueryResp.class);
        return resp;

    }

    /**
     * 新增或者更新一个下载项
     */
    public int save(DocSaveReq req) {
        SnowFlakeIdWorker snowFlakeIdWorker = new SnowFlakeIdWorker(0, 0);
        Doc res = CopyUtil.copy(req, Doc.class);

        try {
            if (ObjectUtils.isEmpty(req.getId())) {
                res.setId(snowFlakeIdWorker.nextId());      // 新增时, 设置雪花ID
                return docMapper.insert(res);
            } else {
                return docMapper.updateByPrimaryKey(res);
            }
        } catch (DataIntegrityViolationException e) {
            LOG.info("错误: 插入或更新错误");
            return -1;
        }
    }

    /**
     * 删除 1 个课程项
     */
    public int deleteId(Long id) {
        int res = docMapper.deleteByPrimaryKey(id);
        if (res != 1) {
            LOG.info("删除 1 个课程项失败");
            return 0;
        } else {
            return res;
        }
    }
    /**
     * 删除 1 个课程项
     */
    public int deleteStr(String idStr) {
        int res = 0;
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        List<String> idStrList = Arrays.asList(idStr.split(","));    // 将每个 columnId 抽出来
        criteria.andIdIn(idStrList);

        try {
            res = docMapper.deleteByExample(docExample);
        } catch (Exception e) {
            LOG.info("删除多个专栏文档失败");
        }

        return res;
    }

}
