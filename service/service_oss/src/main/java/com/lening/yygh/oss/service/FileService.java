package com.lening.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 牛胜浩
 * @date 2021/6/8 17:28
 */
public interface FileService {

    //上传文件
    String upload(MultipartFile file);
}
