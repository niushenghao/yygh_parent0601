package com.lening.yygh.hosp.service;


import com.lening.yygh.model.hosp.Hospital;
import com.lening.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/5/28 21:20
 */
public interface HospitalService {

    void saveHospital(Map<String, Object> paramMap);

    boolean getSign(Map<String, Object> paramMap);

    Hospital getHospitalByHoscode(String hoscode);

    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);
}
