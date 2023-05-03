package com.jxm.yiti.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.ChatHistory;
import com.jxm.yiti.domain.ChatHistoryContent;
import com.jxm.yiti.domain.ChatHistoryExample;
import com.jxm.yiti.domain.DownloadList;
import com.jxm.yiti.mapper.ChatHistoryContentMapper;
import com.jxm.yiti.mapper.ChatHistoryMapper;
import com.jxm.yiti.req.ChatCplQueryReq;
import com.jxm.yiti.resp.ChatCplQueryResp;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class GptService {

    @Resource
    private ChatHistoryMapper chatHistoryMapper;

    @Resource
    private ChatHistoryContentMapper chatHistoryContentMapper;

    private static final Logger LOG = LoggerFactory.getLogger(GptService.class);

    /**
     * 根据用户 ID 查找所有的历史记录
     */
    public List<ChatHistory> selectAllByID(Long userID) {
        List<ChatHistory> res = null;
        ChatHistoryExample chatHistoryExample = new ChatHistoryExample();
        chatHistoryExample.setOrderByClause("id desc");      // 按 id 倒序返回, 即离现在时间最近的 20 条记录
        ChatHistoryExample.Criteria criteria = chatHistoryExample.createCriteria();
        criteria.andUserIdEqualTo(userID);

        try {
            PageHelper.startPage(1, 20, true);
            res = chatHistoryMapper.selectByExample(chatHistoryExample);
            PageInfo<ChatHistory> downloadListPageInfo = new PageInfo<>(res);
            LOG.info("当前页: " + downloadListPageInfo.getPageNum()
                    + ", 总页数: " + downloadListPageInfo.getPages()
                    + " , 总记录数: " + downloadListPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return res;
    }

    /**
     * 根据历史记录 ID 查找对话内容
     */
    public String selectContentByID(Long historyId) {
        ChatHistory chatHistory = chatHistoryMapper.selectByPrimaryKey(historyId);
        ChatHistoryContent chatHistoryContent = chatHistoryContentMapper.selectByPrimaryKey(chatHistory.getContentId());
        return chatHistoryContent.getContent();
    }
}
