package com.lening.yygh.hosp.service;

import com.lening.yygh.model.hosp.Schedule;
import com.lening.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/5/31 19:49
 */
public interface ScheduleService {
    Page<Schedule> getScheduleListPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void saveSchedule(Map<String, Object> paramMap);

    void remove(String hoscode, String hosScheduleId);

    //根据医院编号 和 科室编号 ，查询排班规则数据
    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);

    //根据医院编号 、科室编号和工作日期，查询排班详细信息
    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);
}
