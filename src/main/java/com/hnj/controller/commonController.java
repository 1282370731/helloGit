package com.hnj.controller;


import com.hnj.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class commonController {
    @Value("${reggie.path}")
    private String uploadPath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString()+suffix;
        File dir=new File(uploadPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try{
            file.transferTo(new File(uploadPath+fileName));
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try{
            FileInputStream fileInputStream=new FileInputStream(new File(uploadPath+name));
            ServletOutputStream servletOutputStream=response.getOutputStream();
            response.setContentType("image/ipeg");
            int len=0;
            byte[] bytes=new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
                servletOutputStream.write(bytes,0,len);
                servletOutputStream.flush();
            }
            servletOutputStream.close();
            fileInputStream.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

}
