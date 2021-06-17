package com.lening.yygh.hosp.controller.api;

import com.lening.yygh.common.result.Result;
import com.lening.yygh.hosp.service.DepartmentService;
import com.lening.yygh.hosp.service.HospitalService;
import com.lening.yygh.hosp.service.HospitalSetService;
import com.lening.yygh.hosp.service.ScheduleService;
import com.lening.yygh.model.hosp.Hospital;
import com.lening.yygh.model.hosp.Schedule;
import com.lening.yygh.vo.hosp.DepartmentVo;
import com.lening.yygh.vo.hosp.HospitalQueryVo;
import com.lening.yygh.vo.hosp.ScheduleOrderVo;
import com.lening.yygh.vo.order.SignInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/6/3 8:58
 */
@RestController
@RequestMapping("/api/hosp/hospital")
@Api(tags = "医院列表api接口")
public class HospApiController {

    @Resource
    private HospitalService hospitalService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private ScheduleService scheduleService;

    @Resource
    private HospitalSetService hospitalSetService;

    @ApiOperation(value = "查询医院列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page, @PathVariable Integer limit ,HospitalQueryVo hospitalQueryVo){
        Page<Hospital> hospitalList = hospitalService.selectPage(page, limit, hospitalQueryVo);
        List<Hospital> content = hospitalList.getContent();
        int totalPages = hospitalList.getTotalPages();
        return Result.ok(hospitalList);
    }

    @ApiOperation(value = "根据医院医院名称查询")
    @GetMapping("findByHosName/{hosname}")
    public Result findByHosName(@PathVariable String hosname){
        List<Hospital> hospital = hospitalService.findByHosName(hosname);
        return Result.ok(hospital);
    }

    @ApiOperation(value = "获取科室列表")
    @GetMapping("department/{hoscode}")
    public Result index(@PathVariable String hoscode){
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }

    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("findHospDetail/{hoscode}")
    public Result item(@PathVariable String hoscode){
        Map<String,Object> map = hospitalService.item(hoscode);
        return Result.ok(map);
    }

    //获取可预约排班的数据
    @ApiOperation(value = "获取可预约排班的数据")
    @GetMapping("auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getBookingScheduleRule(@ApiParam(name = "page", value = "当前页码", required = true)
                         @PathVariable Integer page,
                         @ApiParam(name = "limit", value = "每页记录数", required = true)
                         @PathVariable Integer limit,
                         @ApiParam(name = "hoscode", value = "医院code", required = true)
                         @PathVariable String hoscode,
                         @ApiParam(name = "depcode", value = "科室code", required = true)
                         @PathVariable String depcode){
        return Result.ok(scheduleService.getBookingScheduleRule(page,limit,hoscode,depcode));
    }

    //获取排班数据
    @ApiOperation(value = "获取排班数据")
    @GetMapping("auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result findScheduleList(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode,
            @ApiParam(name = "workDate", value = "排班日期", required = true)
            @PathVariable String workDate){
        return Result.ok(scheduleService.getDetailSchedule(hoscode,depcode,workDate));
    }

    @ApiOperation(value = "根据排班id获取排班数据")
    @GetMapping("getSchedule/{scheduleId}")
    public Result getSchedule(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable String scheduleId) {
        Schedule schedule = scheduleService.getscheduleId(scheduleId);
        return Result.ok(schedule);
    }

    @ApiOperation(value = "根据排班id获取预约下单数据")
    @GetMapping("inner/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable("scheduleId") String scheduleId) {
        return scheduleService.getScheduleOrderVo(scheduleId);
    }

    //获取下单引用签名信息
    @ApiOperation(value = "获取医院签名信息")
    @GetMapping("inner/getSignInfoVo/{hoscode}")
    public SignInfoVo getSignInfoVo(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable("hoscode") String hoscode) {
        return hospitalSetService.getSignInfoVo(hoscode);
    }

}
