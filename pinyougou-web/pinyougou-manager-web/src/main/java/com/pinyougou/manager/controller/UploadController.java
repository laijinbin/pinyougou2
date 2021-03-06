package com.pinyougou.manager.controller;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadController {

    @Value("${fileServerUrl}")
    private String fileServerUrl;

    @PostMapping("/upload")
    public Map<String,Object> upload(@RequestParam("file") MultipartFile multipartFile){
        Map<String, Object> data = new HashMap<>();
        data.put("status", 500);
        try{
            //加载配置文件
            String conf_filename=this.getClass().getResource("/fastdfs_client.conf").getPath();
           //初始化客户端连接
            ClientGlobal.init(conf_filename);
            //创建存储客户端对象
            StorageClient storageClient=new StorageClient();
            //获取文件名
            String originalFilename = multipartFile.getOriginalFilename();
            //上传文件
            String[] strings = storageClient.upload_file(multipartFile.getBytes(), FilenameUtils.getExtension(originalFilename), null);
            //拼接返回的url和IP地址
            StringBuilder url=new StringBuilder(fileServerUrl);
            for (String string : strings) {
                url.append("/"+string);
            }
            data.put("status", 200);
            data.put("url", url.toString());
        }catch (Exception e){
            e.printStackTrace();

        }
        return data;
    }
}
