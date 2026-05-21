package com.galgallery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.galgallery.mapper")
@SpringBootApplication
public class GalGalleryApplication {

    public static void main(String[] args) {
        SpringApplication.run(GalGalleryApplication.class, args);
    }
}

