package com.lening.yygh.hosp.service;


import com.lening.yygh.model.hosp.Hospital;
import com.lening.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/5/28 21:20
 */
public interface HospitalService {

    //上传医院接口
    void saveHospital(Map<String, Object> paramMap);

    boolean getSign(Map<String, Object> paramMap);

    //实现根据医院编号查询
    Hospital getHospitalByHoscode(String hoscode);

    //医院列表(条件查询分页)
    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    //更新医院上线状态
    void updateStatus(String id, Integer status);

    //医院详情信息
    Map<String, Object> show(String id);

    //获取医院名称
    String getHospName(String hoscode);

    //根据医院名称查询
    List<Hospital> findByHosName(String hosname);

    //根据医院编号获取医院预约挂号详情
    Map<String, Object> item(String hoscode);
}
