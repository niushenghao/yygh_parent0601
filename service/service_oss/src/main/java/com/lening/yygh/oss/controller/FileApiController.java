package com.lening.yygh.oss.controller;

import com.lening.yygh.common.result.Result;
import com.lening.yygh.oss.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author 牛胜浩
 * @date 2021/6/8 17:25
 */
@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {

    @Resource
    private FileService fileService;

    //上传文件到阿里云oss
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file){
        String url = fileService.upload(file);
        return Result.ok(url);
    }
}
