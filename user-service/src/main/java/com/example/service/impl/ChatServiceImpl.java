package com.example.service.impl;

import com.example.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.OpenAiAudioTranscriptionClient;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.ChatClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final JdbcTemplate jdbcTemplate;
    @Value("${spring.ai.openai.api-key}")
    private String defaultApiKey;
    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;
    private OpenAiAudioTranscriptionClient transcriptionClient;


    // 系统提示词
    private final static String SYSTEM_PROMPT = """
            你需要使用文档内容对用户提出的问题进行回复，同时你需要表现得天生就知道这些内容，
            不能在回复中体现出你是根据给出的文档内容进行回复的，这点非常重要。
            
            当用户提出的问题无法根据文档内容进行回复或者你也不清楚时，回复不知道即可。
                    
            文档内容如下:
            {documents}
            
            """;


    @Override
    public String chatByPdf(String message) {
        VectorStore vectorStore = randomGetVectorStore();
        //根据内容进行相关性检索
        List<Document> documents = vectorStore.similaritySearch(message);
        //将Document列表中每个conntent内容进行拼接
        String collect = documents.stream().map(Document::getContent).collect(Collectors.joining());
        //使用Spring AI提供的模块方式构造SystemMessage对象
        Message message1 = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage(Map.of("documents",collect));
        UserMessage userMessage = new UserMessage(message);
        ChatResponse call = chatClient.call(new Prompt(List.of(message1,userMessage)));
//        ChatResponse call = chatClient.call(new Prompt(List.of(userMessage)));

        return call.getResult().getOutput().getContent();
    }

    public VectorStore randomGetVectorStore(){
        OpenAiApi openAiApi = new OpenAiApi(baseUrl, defaultApiKey);
        EmbeddingClient openAiEmbeddingClient = new OpenAiEmbeddingClient(openAiApi);
        return new PgVectorStore(jdbcTemplate,openAiEmbeddingClient);
    }
}
