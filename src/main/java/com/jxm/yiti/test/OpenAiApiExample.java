package com.jxm.yiti.test;


import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.Image;
import com.theokanning.openai.image.ImageResult;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenAiApiExample {
    public static void main(String... args) {
        String OPENAI_TOKEN = "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";

        OpenAiService service = new OpenAiService(OPENAI_TOKEN, Duration.ofSeconds(30));

//        System.out.println("\nCreating completion...");
//        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
//                .model("gpt-3.5-turbo")
//                .messages("How are you")
//                .echo(true)
//                .user("user")
//                .n(1)
//                .build();
//        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
//        service.createChatCompletion()

        System.out.println("\nCreating Image...");
        CreateImageRequest request = CreateImageRequest.builder()
                .prompt("a close up, studio photographic portrait of a white siamese cat that looks curious, backlit ears")
                .build();

        System.out.println("\nImage is located at:");
        ImageResult image = service.createImage(request);
        System.out.println(image.toString());
        System.out.println(image.getData().get(0).getUrl());
//
//        System.out.println("Streaming chat completion...");
//        final List<ChatMessage> messages = new ArrayList<>();
//        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.USER.value(), "You are a dog and will speak as such.");
//        messages.add(systemMessage);
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
//                .builder()
//                .model("gpt-3.5-turbo")
//                .messages(messages)
//                .n(1)
//                .maxTokens(150)
//                .logitBias(new HashMap<>())
//                .build();
//
//        ChatCompletionResult chatCompletion = service.createChatCompletion(chatCompletionRequest);
//        System.out.println(chatCompletion.getChoices().get(0).getMessage().getContent());
//        service.streamChatCompletion(chatCompletionRequest)
//                .doOnError(Throwable::printStackTrace)
//                .blockingForEach(System.out::println);

//        final List<ChatMessage> messages = new ArrayList<>();
//        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
//        messages.add(systemMessage);
//
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
//                .builder()
//                .model("gpt-3.5-turbo")
//                .messages(messages)
//                .n(1)
//                .maxTokens(50)
//                .logitBias(new HashMap<>())
//                .stream(true)
//                .build();
//
//        List<ChatCompletionChunk> chunks = new ArrayList<>();
//        service.streamChatCompletion(chatCompletionRequest).blockingForEach(chunks::add);
//
//        System.out.println(chunks);

        service.shutdownExecutor();
    }
}
