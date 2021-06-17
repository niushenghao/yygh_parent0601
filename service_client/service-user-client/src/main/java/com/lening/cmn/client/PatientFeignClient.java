package com.lening.cmn.client;

import com.lening.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 牛胜浩
 * @date 2021/6/11 11:34
 */
@FeignClient(value = "service-user")
@Repository
public interface PatientFeignClient {

    //获取就诊人
    @GetMapping("/api/user/patient/inner/get/{id}")
    Patient getPatient(@PathVariable("id") Long id);

}
