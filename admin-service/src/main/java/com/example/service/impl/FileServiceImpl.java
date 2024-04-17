package com.example.service.impl;

import com.example.common.BaseResponse;
import com.example.service.FileService;
import com.example.utils.ResultUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final JdbcTemplate jdbcTemplate;
    @Value("${spring.ai.openai.api-key}")
    private String defaultApiKey;
    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;
    private final TokenTextSplitter tokenTextSplitter;
    @Override
    public BaseResponse updateFile(MultipartFile file) {

        String fileName = file.getOriginalFilename();
        try {
            byte[] bytes = file.getBytes();
            Path tempFile = Files.createTempFile("temp-",fileName);
            Files.write(tempFile,bytes);

            Resource resource = new FileSystemResource(tempFile.toFile());
            TextReader textReader = new TextReader(resource);
            textReader.getCustomMetadata().put(resource.getFilename(),resource.getFile());
            VectorStore vectorStore = randomGetVectorStore();
            vectorStore.accept(tokenTextSplitter.apply(textReader.get()));
            return ResultUtils.success("添加成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultUtils.error(400,"添加失败");
    }

    public VectorStore randomGetVectorStore(){
        OpenAiApi openAiApi = new OpenAiApi(baseUrl, defaultApiKey);
        EmbeddingClient openAiEmbeddingClient = new OpenAiEmbeddingClient(openAiApi);
        return new PgVectorStore(jdbcTemplate,openAiEmbeddingClient);
    }

}
