package com.lening.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lening.yygh.hosp.repository.ScheduleRepository;
import com.lening.yygh.hosp.service.ScheduleService;
import com.lening.yygh.model.hosp.Schedule;
import com.lening.yygh.vo.hosp.ScheduleQueryVo;
import org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/5/31 19:50
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Resource
    ScheduleRepository scheduleRepository;

    @Override
    public Page<Schedule> getScheduleListPage(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        /**
         * 分页
         */
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //0为第一页，使用的是springdata的分页，和jpa是同一个  spring-data-
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        /**
         * 把vocopy进了实体类，里面有3个字段
         * 医院的编码，科室的编码，是否删除没有删除的查询出来
         */
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setIsDeleted(0);
        //创建匹配器，即如何使用查询条件
        /**
         * 然后把我们的实体和匹配一起给查询，在查询的时候，就会使用实体类里
         面的字段通过匹配器进行查询
         */
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);//改变默认大小写忽略方式：忽略大小写
        Example<Schedule> example = Example.of(schedule,matcher);
        /**
         * rep没有继承mongo的接口，就没有方法
         */
        Page<Schedule> pages = scheduleRepository.findAll(example,pageable);
        return pages;
    }

    @Override
    public void saveSchedule(Map<String, Object> paramMap) {
        /**
         * 有id是修改没有id是保存
         * 通过传json，然后转乘实体类
         */
        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Schedule.class);
        /**
         * 去查询，按照医院的编码和排班的编码去查询
         * get(获取)Schedule(排班)By(按照)Hoscode(医院编码)And(和)HosScheduleId(医院排班编码)
         * getScheduleByHoscodeAndHosScheduleId
         */
        Schedule schedule1 = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getDepcode());
        if (schedule1==null){
            /**
             * 没有的话需要保存
             *
             */
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);
        }else {
            /**
             * 有这个排班，修改
             * 查出出来的，我们需要把查询出来的，然后我们把用户传递的过来的copy进查询出来的，然后更新
             * 用过传递过来的进行保存
             * 里面没有id，没有是否删除没有创建时间
             */
            BeanUtils.copyProperties(schedule, schedule1,Schedule.class);
            /**
             * 保存的是查询出来的
             * 把用户传递过来的放在查询出来的里面
             */
            scheduleRepository.save(schedule1);
        }
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if(schedule!=null){
            scheduleRepository.deleteById(schedule.getId());
        }
    }
}
