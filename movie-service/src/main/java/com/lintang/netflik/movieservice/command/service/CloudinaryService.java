package com.lintang.netflik.movieservice.command.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import com.lintang.netflik.movieservice.command.action.CloudinaryAction;
import com.lintang.netflik.movieservice.exception.InternalServerEx;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Service
public class CloudinaryService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CloudinaryAction cloudinaryAction;




    public String upload(MultipartFile file) {
           return cloudinaryAction.upload(file);

    }
}
