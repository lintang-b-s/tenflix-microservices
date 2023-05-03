package com.lintang.netflik.movieservice.controller;


import com.lintang.netflik.movieservice.service.CloudinaryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value="/api/v1/upload")
@AllArgsConstructor
public class UploadController {

//    private GoogleDriveManager googleDriveManager;
    private CloudinaryService cloudinaryService;

    @PostMapping("/video")
    public ResponseEntity<String> uploadFileWithPath(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("folderPath") String folderPath) {
        log.info("upload file path: {}",folderPath);
        String fileId = cloudinaryService.upload(file);
        if (fileId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        String res=  "success upload , fileId: " + fileId;
        return ok(fileId);
    }
}
