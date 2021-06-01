package com.lening.yygh.hosp.service;

import com.lening.yygh.model.hosp.Schedule;
import com.lening.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/5/31 19:49
 */
public interface ScheduleService {
    Page<Schedule> getScheduleListPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void saveSchedule(Map<String, Object> paramMap);

    void remove(String hoscode, String hosScheduleId);
}
