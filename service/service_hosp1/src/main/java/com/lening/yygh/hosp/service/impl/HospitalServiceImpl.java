package com.lening.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lening.cmn.client.DictFeignClient;
import com.lening.yygh.common.utils.MD5;
import com.lening.yygh.enums.DictEnum;
import com.lening.yygh.hosp.mapper.HospitalSetMapper;
import com.lening.yygh.hosp.repository.HospitalRepository;
import com.lening.yygh.hosp.service.HospitalService;
import com.lening.yygh.model.hosp.Hospital;
import com.lening.yygh.model.hosp.HospitalSet;
import com.lening.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 牛胜浩
 * @date 2021/5/28 21:20
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    /**
     * 相当于mapper
     */
    @Resource
    private HospitalRepository hospitalRepository;
    @Resource
    private HospitalSetMapper hospitalSetMapper;
    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void saveHospital(Map<String, Object> paramMap) {
        /**
         * 是一个map，我们最后要往数据库存储的是一个键值对，值是刚刚看到的数据模型
         */
        /**
         * 先把map转成json，在把json转成实体类
         */
        String json = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(json,Hospital.class);
        /**
         * 修改和保存是一起的
         * 需要我们进行判断，有id是修改，没有是新增
         * 医院编码是唯一，使用这个实体类里面的医院编码去数据库查询，看看有没有信息
         */
        if(hospital!=null){
            Hospital hp = hospitalRepository.findHospitalByHoscode(hospital.getHoscode());
            System.out.println(hp);
            if (hp==null){
                /**
                 * //0：未上线 1：已上线
                 */
                hospital.setStatus(0);
                hospital.setCreateTime(new Date());
                hospital.setUpdateTime(new Date());
                hospital.setIsDeleted(0);
                hospitalRepository.save(hospital);
            }else {
                /**
                 * 因为我们修改的时候，要把id存进去
                 * 我们按照医院编码去查询的数据
                 */
                hospital.setId(hp.getId());
                hospital.setStatus(hp.getStatus());
                hospital.setCreateTime(hp.getCreateTime());
                hospital.setUpdateTime(new Date());
                hospital.setIsDeleted(0);
                hospitalRepository.save(hospital);
            }
        }
        /**
         * 有的话，走修改，么有的话就是新增
         */
    }

    @Override
    public boolean getSign(Map<String, Object> paramMap) {
        /*String json = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(json,Hospital.class);*/

        /**
         * 去数据库中查询医院设置
         */
        String hsocode = (String)paramMap.get("hoscode");
        String sign = (String)paramMap.get("sign");
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper();
        queryWrapper.eq("hoscode",hsocode);
        HospitalSet hospitalSet = hospitalSetMapper.selectOne(queryWrapper);
        /*List<HospitalSet> list = hospitalSetService.list(queryWrapper);
        if(list!=null&&list.size()>=1){
            *//**
             * 平台查出来的和医院传来的进行比较  sign
             *//*
            String signKey = list.get(0).getSignKey();
            String encrypt = MD5.encrypt(signKey);
            if(encrypt.equals(sign)){
                return true;
            }

        }*/
        if (hospitalSet!=null){
            /**
             * 密码数据库密码进行加密
             */
            String encrypt = MD5.encrypt(hospitalSet.getSignKey());
            if (sign!=null&&sign.equals(encrypt)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Hospital getHospitalByHoscode(String hoscode) {
        return hospitalRepository.findHospitalByHoscode(hoscode);
    }

    //获取医院名称
    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.findHospitalByHoscode(hoscode);
        if(hospital != null) {
            return hospital.getHosname();
        }
        return null;
    }

    @Override
    public List<Hospital> findByHosName(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String,Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.packHospital(this.getHospitalByHoscode(hoscode));
        result.put("hospital", hospital);
        result.put("bookingRule", hospital.getBookingRule());
        hospital.setBookingRule(null);
        return result;
    }

    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //0为第一页
        Pageable pageable = PageRequest.of(page-1, limit, sort);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        //创建实例
        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

       pages.getContent().stream().forEach(item -> {
            this.packHospital(item);
        });
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        if(status.intValue() == 0 || status.intValue() == 1) {
            Hospital hospital = hospitalRepository.findById(id).get();
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Map<String, Object> show(String id) {
        Map<String,Object> result = new HashMap<>();
        Hospital hospital = this.packHospital(hospitalRepository.findById(id).get());
        //医院基本信息（包含医院等级）
        result.put("hospital",hospital);
        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    /**
     * 封装数据
     * @param hospital
     */
    //获取查询list集合，遍历进行医院等级封装
    private Hospital packHospital(Hospital hospital) {
        String hostypeString = dictFeignClient.getName(DictEnum.HOSTYPE.getDictCode(),hospital.getHostype());
        //查询省 市  地区
        String provinceString  = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString   = dictFeignClient.getName(hospital.getCityCode());
        String districtString   = dictFeignClient.getName(hospital.getCityCode());
        hospital.getParam().put("hostypeString",hostypeString);
        hospital.getParam().put("fullAddress",provinceString+cityString+districtString+hospital.getAddress());
        return hospital;
    }

}
