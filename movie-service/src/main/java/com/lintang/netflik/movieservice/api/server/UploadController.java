package com.lintang.netflik.movieservice.api.server;


import com.lintang.netflik.movieservice.command.service.CloudinaryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value="/api/v1/movie-service/upload")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ROLE_default-roles-tenflix')")
public class UploadController {

//    private GoogleDriveManager googleDriveManager;
    private CloudinaryService cloudinaryService;

    @PostMapping("/video")
    public ResponseEntity<String> uploadFileWithPath(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("folderPath") String folderPath) {
        log.info("upload file path: {}",folderPath);
        // harusnya tambahin settingan protokol streaming sama format berbagai resolusi, biar pas get videonya bisa streaming kaya youtube

        String fileId = cloudinaryService.upload(file);
        if (fileId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        String res=  "success upload , fileId: " + fileId;
        return ok(fileId);
    }
}
