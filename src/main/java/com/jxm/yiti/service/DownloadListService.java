package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.DownloadList;
import com.jxm.yiti.domain.DownloadListExample;
import com.jxm.yiti.mapper.DownloadListMapper;
import com.jxm.yiti.req.DownloadListQueryReq;
import com.jxm.yiti.req.DownloadListSaveReq;
import com.jxm.yiti.resp.DownloadListQueryResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class DownloadListService {

    @Resource
    private DownloadListMapper downloadListMapper;

    private static final Logger LOG = LoggerFactory.getLogger(DownloadListService.class);

    /**
     * 查询下载列表的所有数据, 带有模糊匹配功能
     */
    public PageResp<DownloadListQueryResp> list(DownloadListQueryReq req) {
        DownloadListExample downloadListExample = new DownloadListExample();
        DownloadListExample.Criteria criteria = downloadListExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {  // 动态 SQL
            criteria.andNameLike("%" + req.getName() + "%");  // 模糊匹配条件
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<DownloadList> downloadLists = downloadListMapper.selectByExample(downloadListExample);
        PageInfo<DownloadList> downloadListPageInfo = new PageInfo<>(downloadLists); // 记得这里需要初始化
        LOG.info("当前页: " + downloadListPageInfo.getPageNum() + ", 总页数: " + downloadListPageInfo.getPages() + " , 总记录数: " + downloadListPageInfo.getTotal()); //        + " , 总页数: " + downloadListPageInfo.getTotal()

        PageResp<DownloadListQueryResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(downloadLists, DownloadListQueryResp.class));
        resp.setTotal(downloadListPageInfo.getTotal());

        return resp;

    }

    /**
     * 新增或者更新一个下载项
     */
    public int save(DownloadListSaveReq req) {
        SnowFlakeIdWorker snowFlakeIdWorker = new SnowFlakeIdWorker(0, 0);
        DownloadList res = CopyUtil.copy(req, DownloadList.class);

        try {
            if (ObjectUtils.isEmpty(req.getId())) {
                res.setId(snowFlakeIdWorker.nextId());
                return downloadListMapper.insert(res);
            } else {
                return downloadListMapper.updateByPrimaryKey(res);
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
        return downloadListMapper.deleteByPrimaryKey(id);
    }
}
