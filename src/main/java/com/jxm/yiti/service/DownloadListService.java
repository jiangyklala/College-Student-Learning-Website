package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.DownloadList;
import com.jxm.yiti.domain.DownloadListExample;
import com.jxm.yiti.mapper.DownloadListMapper;
import com.jxm.yiti.req.DownloadListReq;
import com.jxm.yiti.resp.DownloadListResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.utils.CopyUtil;
import jakarta.annotation.Resource;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DownloadListService {

    @Resource
    private DownloadListMapper downloadListMapper;

    private static final Logger LOG = LoggerFactory.getLogger(DownloadListService.class);

    public PageResp<DownloadListResp> list(DownloadListReq req) {
        DownloadListExample downloadListExample = new DownloadListExample();
        DownloadListExample.Criteria criteria = downloadListExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {  // 动态 SQL
            criteria.andNameLike("%" + req.getName() + "%");  // 模糊匹配条件
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<DownloadList> downloadLists = downloadListMapper.selectByExample(downloadListExample);
        PageInfo<DownloadList> downloadListPageInfo = new PageInfo<>(downloadLists); // 记得这里需要初始化
        LOG.info("当前页: " + downloadListPageInfo.getPageNum() + ", 总页数: " + downloadListPageInfo.getPages() + " , 总记录数: " + downloadListPageInfo.getTotal()); //        + " , 总页数: " + downloadListPageInfo.getTotal()

        PageResp<DownloadListResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(downloadLists, DownloadListResp.class));
        resp.setTotal(downloadListPageInfo.getTotal());

        return resp;

    }
}
