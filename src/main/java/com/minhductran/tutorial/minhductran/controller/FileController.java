package com.minhductran.tutorial.minhductran.controller;

import com.minhductran.tutorial.minhductran.dto.response.ApiResponse;
import com.minhductran.tutorial.minhductran.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${project.logo")
    private String path;

    @PostMapping("/upload")
    public ApiResponse<String> uploadFile(@RequestPart MultipartFile file) throws IOException {
        String uploadedFile = fileService.uploadFile(path, file);
        return new ApiResponse<>(HttpStatus.OK.value(), "File uploaded successfully", uploadedFile);
    }
}
