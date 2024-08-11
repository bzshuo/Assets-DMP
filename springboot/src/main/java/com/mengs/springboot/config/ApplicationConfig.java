package com.mengs.springboot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApplicationConfig {
    @Value("${server.ip}")
    private String serverIp;

    //用户上传PDF文件地址
    @Value("${pdf.origin.path}")
    private String pdfOriginPath;
    //用户下载加印PDF文件地址
    @Value("${pdf.new.path}")
    private String pdfNewPath;
    //用户上传待验证PDF文件地址
    @Value("${pdf.verify.path}")
    private String pdfVerifyPath;
    //上传用章图片地址
    @Value("${signPic.path}")
    private String signPicPath;

}
