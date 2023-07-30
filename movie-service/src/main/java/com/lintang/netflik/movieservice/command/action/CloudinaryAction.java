package com.lintang.netflik.movieservice.command.action;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import com.lintang.netflik.movieservice.exception.InternalServerEx;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
public class CloudinaryAction {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Cloudinary cloudinary = Singleton.getCloudinary();

    public String upload(MultipartFile file) {
        try {
            Map params = ObjectUtils.asMap(
                    "folder", "netflik",
                    "resource_type", "video"
            );
//            byte[] tes = file.getBytes();
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
            String publicId = uploadResult.get("public_id").toString();
            logger.info(" successfully uploaded the file: " + publicId);
            return publicId;
        } catch (Exception ex) {
            throw new InternalServerEx("upload error: " + ex.getMessage());
        }
    }
}
