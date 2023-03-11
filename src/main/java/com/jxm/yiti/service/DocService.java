package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.Doc;
import com.jxm.yiti.domain.DocContent;
import com.jxm.yiti.domain.DocExample;
import com.jxm.yiti.mapper.DocContentMapper;
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

    @Resource
    private DocContentMapper docContentMapper;

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
        int res = 0;
        SnowFlakeIdWorker snowFlakeIdWorker = new SnowFlakeIdWorker(0, 0);
        Doc doc = CopyUtil.copy(req, Doc.class);
        DocContent docContent = CopyUtil.copy(req, DocContent.class);

        try {
            if (ObjectUtils.isEmpty(req.getId())) {                                     // 是新增操作
                doc.setId(snowFlakeIdWorker.nextId());
                docContent.setId(doc.getId());                                          // 两者 id 保持一致

                try {
                    res += docMapper.insert(doc);
                    res += docContentMapper.insert(docContent);
                } catch (Exception e) {
                    LOG.info("新增文档失败");
                }

                return res;
            } else {                                                                   // 是更新操作
                try {
                    res += docMapper.updateByPrimaryKey(doc);
                    res += docContentMapper.updateByPrimaryKeyWithBLOBs(docContent);   // 对大字段进行更新操作
                } catch (Exception e) {
                    LOG.info("更新文档失败");
                }

                return res;
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

    /**
     * 根据 id 查 doc_content 表中的内容
     */
    public DocContent selectContentById(Long id) {
        int res = 0;
        DocContent docContent = null;

        try {
            docContent = docContentMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            LOG.info("查找文档内容失败");
        }

        return docContent;

    }
}
