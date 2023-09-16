package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.ChatHistory;
import com.jxm.yiti.domain.ChatHistoryContent;
import com.jxm.yiti.domain.ChatHistoryExample;
import com.jxm.yiti.mapper.ChatHistoryContentMapper;
import com.jxm.yiti.mapper.ChatHistoryMapper;
import com.jxm.yiti.mapper.cust.ChatHistoryMapperCust;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GptService {

    @Resource
    private ChatHistoryMapper chatHistoryMapper;

    @Resource
    private ChatHistoryContentMapper chatHistoryContentMapper;

    @Resource
    ChatHistoryMapperCust chatHistoryMapperCust;

    private static final Integer REMAINCOUNT = 20;

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
            PageHelper.startPage(1, REMAINCOUNT, true);
            res = chatHistoryMapper.selectByExample(chatHistoryExample);
            PageInfo<ChatHistory> downloadListPageInfo = new PageInfo<>(res);
            LOG.info("当前页: " + downloadListPageInfo.getPageNum()
                    + ", 总页数: " + downloadListPageInfo.getPages()
                    + " , 总记录数: " + downloadListPageInfo.getTotal());

            if (downloadListPageInfo.getTotal() > REMAINCOUNT) {
                chatHistoryMapperCust.deleteChatHistory(userID, REMAINCOUNT);
            }
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
