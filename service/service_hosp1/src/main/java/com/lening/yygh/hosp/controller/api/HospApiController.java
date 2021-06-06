package com.lening.yygh.hosp.controller.api;

import com.lening.yygh.common.result.Result;
import com.lening.yygh.hosp.service.DepartmentService;
import com.lening.yygh.hosp.service.HospitalService;
import com.lening.yygh.model.hosp.Hospital;
import com.lening.yygh.vo.hosp.DepartmentVo;
import com.lening.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
}
