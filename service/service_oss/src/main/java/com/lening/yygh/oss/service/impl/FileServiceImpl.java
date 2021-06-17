package com.lening.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.lening.yygh.oss.service.FileService;
import com.lening.yygh.oss.utils.ConstantOssPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author 牛胜浩
 * @date 2021/6/8 17:28
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(MultipartFile file) {

        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = ConstantOssPropertiesUtils.EDNPOINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantOssPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantOssPropertiesUtils.SECRECT;
        String bucketName = ConstantOssPropertiesUtils.BUCKET;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            //生成一个随机唯一值
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid+fileName;
            //按照当前日期，创建文件夹，上传到文件夹里
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            fileName =  timeUrl+"/"+fileName;

            // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //上传之后文件路径
            //https://yygh-nsh. oss-cn-hangzhou.aliyuncs.com/01.jpg
            String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
