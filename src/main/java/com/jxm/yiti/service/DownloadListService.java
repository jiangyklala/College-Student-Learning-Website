package com.jxm.yiti.service;

import com.jxm.yiti.domain.DownloadList;
import com.jxm.yiti.domain.DownloadListExample;
import com.jxm.yiti.mapper.DownloadListMapper;
import com.jxm.yiti.req.DownloadListReq;
import com.jxm.yiti.resp.DownloadListResp;
import com.jxm.yiti.utils.CopyUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DownloadListService {

    @Resource
    private DownloadListMapper downloadListMapper;

    public List<DownloadListResp> list(DownloadListReq req) {
        DownloadListExample downloadListExample = new DownloadListExample();
        DownloadListExample.Criteria criteria = downloadListExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {  // 动态 SQL
            criteria.andNameLike("%" + req.getName() + "%");  // 模糊匹配条件
        }

        return CopyUtil.copyList(downloadListMapper.selectByExample(downloadListExample), DownloadListResp.class);

    }
}
