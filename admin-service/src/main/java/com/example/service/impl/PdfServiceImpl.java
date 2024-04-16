package com.example.service.impl;

import com.example.common.BaseResponse;
import com.example.service.PdfService;
import com.example.utils.ResultUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfServiceImpl implements PdfService {
    private final JdbcTemplate jdbcTemplate;
    @Value("${spring.ai.openai.api-key}")
    private String defaultApiKey;
    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;
    private final TokenTextSplitter tokenTextSplitter;

    @Override
    public BaseResponse update(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            Path tempFile = Files.createTempFile("temp-",fileName);
            Files.write(tempFile,bytes);

            FileSystemResource resource = new FileSystemResource(tempFile.toFile());

            //阅读pdf
            PdfDocumentReaderConfig readerConfig = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(
                            new ExtractedTextFormatter
                                    .Builder()
                                    .withNumberOfTopPagesToSkipBeforeDelete(1)
                                    .withNumberOfBottomTextLinesToDelete(3)
                                    .build()
                    )
                    .withPagesPerDocument(1)
                    .build();
            PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(resource,readerConfig);
            VectorStore vectorStore = randomVectorStore();
            vectorStore.accept(tokenTextSplitter.apply(pagePdfDocumentReader.get()));
            return ResultUtils.success("添加成功");
        } catch (Exception e){
            log.info(e.getMessage());
        }
        return ResultUtils.error(400,"添加错误");
    }

    private VectorStore randomVectorStore() {
        OpenAiApi openAiApi = new OpenAiApi(baseUrl, defaultApiKey);
        EmbeddingClient embeddingClient = new OpenAiEmbeddingClient(openAiApi);
        return new PgVectorStore(jdbcTemplate, embeddingClient);
    }
}
