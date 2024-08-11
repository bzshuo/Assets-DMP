package com.mengs.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.mengs.springboot.config.ApplicationConfig;
import com.mengs.springboot.utils.BusinessException;
import com.mengs.springboot.utils.RMap;
import com.mengs.springboot.utils.SHAUtils;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.MethodWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;

/**
 * 文件上传相关接口
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private ApplicationConfig applicationConfig;

    /**
     * 待签章PDF文件上传
     *
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload/pdf")
    public Map uploadPDF(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        if (!"pdf".equals(type)) {
            return RMap.asMap("codeMsg", 500, "msg", "失败，请上传PDF文件");
        }

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;

        File uploadFile = new File(applicationConfig.getPdfOriginPath() + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        // 上传文件到磁盘
        file.transferTo(uploadFile);
        String url = "http://" + applicationConfig.getServerIp() + ":9090/file/pdfOld/" + fileUUID;
        String sha = SHAUtils.pdfFileSHA(fileUUID, applicationConfig.getPdfOriginPath());
        return RMap.asMap("codeMsg", 200, "originFileUrl", url, "fileSha", sha);
    }

    /**
     * 待验证PDF文件上传
     *
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/verify/pdf")
    public Map verifyPdf(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);

        if (!"pdf".equals(type)) {
            return RMap.asMap("codeMsg", 500, "msg", "请上传PDF文件");
        }

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;

        File uploadFile = new File(applicationConfig.getPdfVerifyPath() + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        // 上传文件到磁盘
        file.transferTo(uploadFile);
        return RMap.asMap("codeMsg", 200, "fileName", fileUUID);
    }



    /**
     * 用章图片上传
     *
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload/seal")
    public Map upload(@RequestParam MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;

        File uploadFile = new File(applicationConfig.getSignPicPath() + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        // 上传文件到磁盘
        file.transferTo(uploadFile);
        String url = "http://" + applicationConfig.getServerIp() + ":9090/file/seal/" + fileUUID;

        return RMap.asMap("codeMsg", 200, "msg", "上传成功", "url", url);
    }


    /**
     * 文件下载接口   http://localhost:9090/{fileType}/{fileUUID}
     *
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileType}/{fileUUID}")
    public void download(@PathVariable String fileUUID, @PathVariable String fileType, HttpServletResponse response) {
        try {
            // 根据文件的唯一标识码获取文件
            File uploadFile = null;
            if ("pdfOld".equals(fileType)) {
                uploadFile = new File(applicationConfig.getPdfOriginPath() + fileUUID);
            } else if ("pdfNew".equals(fileType)) {
                uploadFile = new File(applicationConfig.getPdfNewPath() + fileUUID);
            } else if ("seal".equals(fileType)) {
                uploadFile = new File(applicationConfig.getSignPicPath() + fileUUID);
            } else {
                throw new BusinessException("上传文件类型有误");
            }

            // 设置输出流的格式
            ServletOutputStream os = response.getOutputStream();
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
            response.setContentType("application/octet-stream");

            // 读取文件的字节流
            os.write(FileUtil.readBytes(uploadFile));
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("文件下载失败{}", e.toString());
        }
    }

}
