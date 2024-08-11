package com.mengs.springboot.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.mengs.springboot.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;


public class PdfUtils {

    /**
     * 对PDF文件进行加印处理
     * @param
     * @throws Exception
     */
    public static void pdfAddSeal(String templatePath, String targetPath, String imagePath) {
        try {
            // 模板文件路径
//            String templatePath = "D:\\Documents\template1.pdf";
            // 生成的文件路径
//            String targetPath = "D:\\Documents\template11.pdf";
            // 图片路径
//            String imagePath = "D:\\Documents\qianzhang.png";

            // 读取模板文件
            FileInputStream input = new FileInputStream(new File(templatePath));
            PdfReader reader = new PdfReader(input);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(targetPath));
            Document document = new Document();
            // 通过域名获取所在页和坐标，左下角为起点
            float x = document.getPageSize().getWidth() - 330;
            float y = document.getPageSize().getHeight() - 750;
            // 读图片
            Image image = Image.getInstance(imagePath);
            // 获取操作的页面
            PdfContentByte under = stamper.getOverContent(1);
            // 根据域的大小缩放图片
            image.scaleToFit(document.getPageSize().getWidth() - 350, document.getPageSize().getHeight() - 290);
            // 添加图片
            image.setAbsolutePosition(x, y);
            under.addImage(image);

            stamper.close();
            reader.close();
        } catch (Exception e) {
            throw new BusinessException("PDF文件加印异常");
        }
    }
}
