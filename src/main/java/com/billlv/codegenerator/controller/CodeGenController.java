package com.billlv.codegenerator.controller;

import com.billlv.codegenerator.service.CodeGenService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.SocketException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/codegen")
public class CodeGenController {

    private final CodeGenService codeGenService;

    public CodeGenController(CodeGenService codeGenService) {
        this.codeGenService = codeGenService;
    }


    @PostMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestBody List<Long> tableIds) {
        try {
            String fileName="generated_code.zip";
            Path zipFilePath = codeGenService.generateAndZipCodeForTables(tableIds);
            // 构建资源返回
            Resource resource = new UrlResource(zipFilePath.toUri());
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
            headers.add("Access-Control-Expose-Headers", "Content-Disposition"); // 允许浏览器访问Content-Disposition头
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (SocketException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
