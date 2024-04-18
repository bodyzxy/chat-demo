package com.example.service.impl;

import com.example.common.BaseResponse;
import com.example.common.CustomResponseErrorHandler;
import com.example.pojo.AiImage;
import com.example.service.ChatService;
import com.example.utils.ResultUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.*;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.ChatClient;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
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

    @Override
    public BaseResponse aiImage(AiImage aiImage) {
        ImageClient imageClient = createImageClient();
        OpenAiImageOptions options = OpenAiImageOptions.builder()
                .withModel(aiImage.getModel())
                .withHeight(aiImage.getHeight())
                .withWidth(aiImage.getWidth())
                .withResponseFormat(aiImage.getFormat())
                .build();
        ImageResponse imageResponse = imageClient.call(new ImagePrompt(aiImage.getPrompt(),options));
        return ResultUtils.success(imageResponse.getResult());
    }

    private ImageClient createImageClient() {
        OpenAiImageApi openAiImageApi = new OpenAiImageApi(
                baseUrl,
                defaultApiKey,
                RestClient.builder()
        );
        return new OpenAiImageClient(openAiImageApi);
    }

    @Override
    public BaseResponse chatVoid(MultipartFile file) {
        try {
            Resource resource = resourceCreate(file);
            RestClient.Builder builder = builderCreate();
            OpenAiAudioApi openAiAudioApi = new OpenAiAudioApi(baseUrl,defaultApiKey,builder,new CustomResponseErrorHandler());
            transcriptionClient = new OpenAiAudioTranscriptionClient(openAiAudioApi);
            //转换的类型
            OpenAiAudioApi.TranscriptResponseFormat transcriptResponseFormat = OpenAiAudioApi.TranscriptResponseFormat.TEXT;
            OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                    .withLanguage("zh-CN")
                    .withTemperature(0f)
                    .withResponseFormat(transcriptResponseFormat)
                    .build();
            AudioTranscriptionPrompt transcriptionPrompt = new AudioTranscriptionPrompt(resource,transcriptionOptions);
            AudioTranscriptionResponse response = transcriptionClient.call(transcriptionPrompt);
            System.out.println(response.getResult());
            return ResultUtils.success(response);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResultUtils.error(400,"转换失败");
    }

    /**
     * 通常用于自定义 RestTemplate 的配置，如超时设置、消息转换器、拦截器等
     * @return
     */
    private RestClient.Builder builderCreate() {
        ClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory(){{
            setConnectTimeout(5000); // 设置连接超时时间为5000毫秒
            setReadTimeout(5000); // 设置读取超时时间为5000毫秒
        }};
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        return RestClient.builder(restTemplate);
    }

    private Resource resourceCreate(MultipartFile file) throws Exception{
        //获取文件名
        String fileName = file.getName();
        //获取字节数
        byte[] bytes = file.getBytes();
        //获取文件名类型
        String containType = file.getContentType();
        //创建用于存储文件的虚拟路径
        Path path = Files.createTempFile("-temp",fileName);
        Files.write(path,bytes);
        return new FileSystemResource(path.toFile());
    }
}
