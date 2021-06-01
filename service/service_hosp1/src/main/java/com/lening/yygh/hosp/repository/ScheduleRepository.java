package com.lening.yygh.hosp.repository;

import com.lening.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 牛胜浩
 * @date 2021/5/31 19:48
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode,String hosScheduleId);
}
