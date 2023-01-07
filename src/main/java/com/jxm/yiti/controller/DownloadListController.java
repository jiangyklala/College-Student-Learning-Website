package com.jxm.yiti.controller;

import com.jxm.yiti.domain.DownloadList;
import com.jxm.yiti.service.DownloadListService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/downloadList")
public class DownloadListController {

    @Resource
    private DownloadListService downloadListService;

    @GetMapping("/list")
    @ResponseBody
    public List<DownloadList> list() {
        return downloadListService.list();
    }

}
