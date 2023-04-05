package com.jxm.yiti.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jxm.yiti.domain.ChatHistory;
import com.jxm.yiti.domain.ChatHistoryContent;
import com.jxm.yiti.domain.ChatHistoryExample;
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

    @Resource
    private SnowFlakeIdWorker snowFlakeIdWorker;

    private static final Logger LOG = LoggerFactory.getLogger(GptService.class);

    private final String OPENAI_TOKEN = "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";

    public String sendPost(String data) {
        String res = "";
//        String proxyHost = "127.0.0.1";
//        int proxyPort = 7890;
//
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
//        factory.setProxy(proxy);
//        RestTemplate client = new RestTemplate(factory);
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer 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");
        httpHeaders.add("Content-Type", "application/json"); // 传递请求体时必须设置

        String requestJson = String.format(
                "{\n" +
                        "    \"model\": \"gpt-3.5-turbo-0301\",\n" +
                        "    \"messages\":" +
                            "[{\"role\": \"user\", \"content\": \"%s\"}],\n" +
                        "    \"temperature\": 0, \n" +
                        "    \"max_tokens\": 2048\n" +
                        "}",data
        );
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,httpHeaders);
        ResponseEntity<String> response = client.exchange("https://api.openai.com/v1/chat/completions", HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        if (jsonObject != null) {
            JSONArray choices = jsonObject.getJSONArray("choices");
            res = choices.getJSONObject(0).getJSONObject("message").getString("content");

        }
        return res;
    }

    public String sendPost2(String data) {

        String OPENAI_TOKEN = "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";
        // 构建openai api对象，由于处理时间比较长，建议设置一个合理的超时时间
        OpenAiService service = new OpenAiService(OPENAI_TOKEN, Duration.ofSeconds(60));
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("如果有一辆车，车里面坐了小明小红小黄，请问车是谁的？")
                .model("text-davinci-003")
                .temperature(0.9d)
                .maxTokens(150)
                .stop(Arrays.asList("Human:", "AI:"))
                .echo(true)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);

        return null;
    }

    public String chatCompletion(ChatCplQueryReq chatCplQueryReq) {
        return null;
//        OpenAiService service = new OpenAiService(OPENAI_TOKEN, Duration.ofSeconds(30));
//
//        String historyMes = chatCplQueryReq.getHistoryID() == 0
//                ? ""
//                : chatHistoryMapper.selectByPrimaryKey(chatCplQueryReq.getHistoryID()).getContent();
//
////        JSONObject jsonObject = JSON.parseObject(chatHistoryMapper.selectByPrimaryKey(chatCplQueryReq.getHistoryID()).getContent());
//
//        ArrayList<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
//        chatMessages.add(new ChatMessage(ChatMessageRole.USER.value(), chatCplQueryReq.getQueryStr()));
//        chatMessages.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), historyMes));
//
//
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
//                .builder()
//                .model("gpt-3.5-turbo")
//                .messages(chatMessages)
//                .n(1)
//                .maxTokens(150)
//                .logitBias(new HashMap<>())
//                .build();
//
//        String resContent = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
//
//        ChatHistory chatHistory = new ChatHistory();
//        chatHistory.setUserId(chatCplQueryReq.getUserID());
//        chatHistory.setContent(historyMes +
//                String.format("{\"role\": \"user\", \"content\": %s}", chatCplQueryReq.getQueryStr()) +
//                String.format("{\"role\": \"assistant\", \"content\": %s}", resContent));
//        chatHistoryMapper.insert(chatHistory);
//
//        service.shutdownExecutor();
//        return resContent;
    }

    public ChatCplQueryResp chatCompletion2(ChatCplQueryReq chatCplQueryReq) {
        ChatCplQueryResp chatCplQueryResp = null;
        LOG.info(chatCplQueryReq.toString());
        String resContent = "";
        String queryStr = chatCplQueryReq.getQueryStr();


        ChatHistory historyMes = new ChatHistory();           // 历史记录 obj
        ChatHistoryContent historyMesContent = new ChatHistoryContent("");          // 历史记录内容 obj
        if (chatCplQueryReq.getHistoryID() != -1) {           // 获取本次对话的历史记录
            historyMes = chatHistoryMapper.selectByPrimaryKey(chatCplQueryReq.getHistoryID());
            historyMesContent = chatHistoryContentMapper.selectByPrimaryKey(historyMes.getContentId());
        LOG.info(historyMes.toString());
        LOG.info(historyMesContent.toString());
        }

        // 设置请求体
        RestTemplate client = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer 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");
        httpHeaders.add("Content-Type", "application/json");  // 传递请求体时必须设置

        // 在 content 字段中, 添加本次对话的历史记录
        String requestJson = String.format(
                "{\n" +
                        "    \"model\": \"gpt-3.5-turbo-0301\",\n" +
                        "    \"messages\":" +
                        "[" + historyMesContent.getContent() + "{\"role\": \"user\", \"content\": %s}],\n" +
                        "    \"temperature\": 0, \n" +
                        "    \"max_tokens\": 2048\n" +
                        "}", queryStr
        );

        LOG.info(requestJson);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,httpHeaders);
        ResponseEntity<String> response = client.exchange("https://api.openai.com/v1/chat/completions", HttpMethod.POST, entity, String.class);
//        System.out.println(response.getBody());
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        if (jsonObject != null) {
            resContent = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");

            // 下面更新本次对话的历史记录
            LOG.info(historyMes.toString());
            if (chatCplQueryReq.getHistoryID() == -1) {                                   // 此次对话为新对话
                historyMesContent = new ChatHistoryContent();                                 // 无论是新旧对话, 都要更新 content
                historyMesContent.setId(snowFlakeIdWorker.nextId());
                historyMesContent.setContent(String.format("{\"role\": \"user\", \"content\": %s}", JSON.toJSONString(queryStr)) +
                                             String.format(",{\"role\": \"assistant\", \"content\": %s},", JSON.toJSONString(resContent)));
                LOG.info(historyMesContent.toString());
                chatHistoryContentMapper.insert(historyMesContent);

                queryStr = queryStr.length() > 50 ? queryStr.substring(0, 50) : queryStr;    // title 的长度限制在 50
                historyMes.setId(snowFlakeIdWorker.nextId());                                // 设置 id
                historyMes.setUserId(chatCplQueryReq.getUserID());                           // 设置历史记录所属 user
                historyMes.setTitle(queryStr);                                               // 设置这次对话的 title
                historyMes.setContentId(historyMesContent.getId());                          // 设置历史记录内容 id

                LOG.info(historyMes.toString());
                chatHistoryMapper.insert(historyMes);
            } else {                                                                      // 此次对话为旧对话, 只用更新 content
                historyMesContent.setContent(historyMesContent.getContent() +
                        String.format("{\"role\": \"user\", \"content\": %s}", JSON.toJSONString(queryStr)) +
                        String.format(",{\"role\": \"assistant\", \"content\": %s},", JSON.toJSONString(resContent)));
                chatHistoryContentMapper.updateByPrimaryKeyWithBLOBs(historyMesContent);
                LOG.info("lala" + historyMesContent.toString());
            }
            chatCplQueryResp = new ChatCplQueryResp();
            chatCplQueryResp.setContent(resContent);
            chatCplQueryResp.setHistoryID(historyMes.getId());

        }
        return chatCplQueryResp;
    }

    public List<ChatHistory> selectAllByID(Long userID) {
        List<ChatHistory> res = null;
        ChatHistoryExample chatHistoryExample = new ChatHistoryExample();
        ChatHistoryExample.Criteria criteria = chatHistoryExample.createCriteria();
        criteria.andUserIdEqualTo(userID);

        try {
            res = chatHistoryMapper.selectByExample(chatHistoryExample);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String selectContentByID(Long historyId) {
        ChatHistory chatHistory = chatHistoryMapper.selectByPrimaryKey(historyId);
        ChatHistoryContent chatHistoryContent = chatHistoryContentMapper.selectByPrimaryKey(chatHistory.getContentId());
        return chatHistoryContent.getContent();
    }
}
