package com.galgallery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan("com.galgallery.mapper")
@SpringBootApplication
public class GalGalleryApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GalGalleryApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GalGalleryApplication.class);
    }
}
