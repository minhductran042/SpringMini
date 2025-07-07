package com.minhductran.tutorial.minhductran.controller;

import com.minhductran.tutorial.minhductran.dto.response.ResponseEntity;
import com.minhductran.tutorial.minhductran.service.impl.FileService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${project.logo")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile file) throws IOException {
        String uploadedFile = fileService.uploadFile(path, file);
        return new ResponseEntity<>(HttpStatus.OK.value(), "File uploaded successfully", uploadedFile);
    }
}
