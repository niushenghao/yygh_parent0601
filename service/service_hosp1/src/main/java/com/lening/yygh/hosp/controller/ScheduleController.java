package com.lening.yygh.hosp.controller;

import com.lening.yygh.common.result.Result;
import com.lening.yygh.hosp.service.ScheduleService;
import com.lening.yygh.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/6/2 14:27
 */
//根据医院编号 和 科室编号 ，查询排班规则数据
//@CrossOrigin
@Api(tags = "医院排班")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {

    @Resource
    private ScheduleService scheduleService;

    //根据医院编号 和 科室编号 ，查询排班规则数据
    @ApiOperation(value ="查询排班规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable long page,
                                  @PathVariable long limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode) {
        Map<String,Object> map = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }

    //根据医院编号 、科室编号和工作日期，查询排班详细信息
    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail( @PathVariable String hoscode,
                                     @PathVariable String depcode,
                                     @PathVariable String workDate) {
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return Result.ok(list);
    }
}
