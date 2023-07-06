package com.lintang.netflik.movieservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Service
public class CloudinaryService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Cloudinary cloudinary = Singleton.getCloudinary();




    public String upload(MultipartFile file) {
            try {
                Map params = ObjectUtils.asMap(
                        "folder", "netflik",
                        "resource_type", "video"
                );
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
                String publicId = uploadResult.get("public_id").toString();
                logger.info(" successfully uploaded the file: " + publicId);
                return publicId;
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                return null;
            }

    }
}
