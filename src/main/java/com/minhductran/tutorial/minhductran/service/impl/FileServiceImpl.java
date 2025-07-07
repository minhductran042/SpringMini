package com.minhductran.tutorial.minhductran.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        //get File Path
        String filePath = path + File.separator + fileName;

        //file object
        File fileObj = new File(path);
        if(!fileObj.exists()) {
            fileObj.mkdirs(); //create directory if not exists
        }

        //copy the file or upload file
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
        String filePath  = path + File.separator + fileName;
        return new FileInputStream(filePath);
    }
}
