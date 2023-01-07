package com.jxm.yiti.service;

import com.jxm.yiti.domain.DownloadList;
import com.jxm.yiti.mapper.DownloadListMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DownloadListService {

    @Resource
    private DownloadListMapper downloadListMapper;

    public List<DownloadList> list() {
        return downloadListMapper.selectByExample(null);
    }
}
