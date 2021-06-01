package com.lening.yygh.hosp.controller.api;


import com.lening.yygh.common.exception.YyghException;
import com.lening.yygh.common.helper.HttpRequestHelper;
import com.lening.yygh.common.result.Result;
import com.lening.yygh.common.result.ResultCodeEnum;
import com.lening.yygh.hosp.repository.HospitalRepository;
import com.lening.yygh.hosp.service.DepartmentService;
import com.lening.yygh.hosp.service.HospitalService;
import com.lening.yygh.hosp.service.ScheduleService;
import com.lening.yygh.model.hosp.Department;
import com.lening.yygh.model.hosp.Hospital;
import com.lening.yygh.model.hosp.Schedule;
import com.lening.yygh.vo.hosp.DepartmentQueryVo;
import com.lening.yygh.vo.hosp.ScheduleOrderVo;
import com.lening.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 牛胜浩
 * @date 2021/5/28 21:23
 * 远程调用，基本都是rest得 ，可以直接restcontroller
 */
@RestController
@RequestMapping("/api/hosp")
@Api(tags = "医院管理api接口")
public class ApiController {

    //医院
    @Resource
    private HospitalService hospitalService;

    //科室
    @Resource
    private DepartmentService departmentService;

    //排班
    @Resource
    private ScheduleService scheduleService;

    /**
     * 数据过来的是什么，去找
     * 发现在request里面 名字是parammap
     * 我们要从request里面获取
     * @return
     */
    @PostMapping("/saveHospital")
    @ApiOperation(value = "上传医院")
    public Result saveHospital(HttpServletRequest request){
        /**
         * 通过httprequest工具类去获取，通讯的固定方式
         */

        /**
         * 这个是直接拿出来的，也是一个map,但是map里面的值是一个数组。而我们使用object比较方便，所以通过工具类转了一下
         */
        //Map<String, String[]> parameterMap = request.getParameterMap();

        /**
         * 去service进行保存
         */
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());

        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoDataString = (String)paramMap.get("logoData");

        if(!StringUtils.isEmpty(logoDataString)) {
            String logoData = logoDataString.replaceAll(" ", "+");
            paramMap.put("logoData", logoData);
        }

        String sign = (String)paramMap.get("sign");
        System.out.println("sign========"+sign);
        /**
         * 去签名：
         * 1、先把paramMap转成  医院的实体，里面有医院编码，然后再拿着医院的编码去医院设置表里面把这个医院在平台里面的签名拿出来，然后
         * 在按照双方约定好的加密规则进行加密，MD5加密，然后再进行比较，相等鉴权通过了，不相等就鉴权没有通过
         */
        /**成
         * 可以写工具类，也可以在service处理，或者在controller处理
         */
        String hoscode = (String)paramMap.get("hoscode");
        if(StringUtils.isEmpty(hoscode)){
            //参数不正确
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }


        boolean flag = hospitalService.getSign(paramMap);
        if(!flag){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        hospitalService.saveHospital(paramMap);
        /**
         * 最好是判断没有异常返回
         */
        return Result.ok();
    }

    @ApiOperation(value = "获取医院信息")
    @PostMapping("hospital/show")
    public Result hospital(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String)paramMap.get("hoscode");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        /**
         * 我们在service自己写的鉴权签名
         */
        boolean flag = hospitalService.getSign(paramMap);
        if(!flag){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);
        return Result.ok(hospital);
    }


    @ApiOperation(value = "获取分页列表")
    @PostMapping("department/list")
    public Result department(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        /**
         * 开始鉴权
         */
        String hoscode = (String)paramMap.get("hoscode");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        /**
         * 我们在service自己写的鉴权签名
         */
        boolean flag = hospitalService.getSign(paramMap);
        if(!flag){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        /**
         * 考试获取其他的参数
         */
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String)paramMap.get("limit"));
        String depcode = (String)paramMap.get("depcode");
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        departmentQueryVo.setDepcode(depcode);

        /**
         * 开始去service
         */
        Page<Department> pageModel = departmentService.getDepartment(page,limit,departmentQueryVo);
        return Result.ok(pageModel);
    }



    @ApiOperation(value = "上传科室")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        /**
         * 开始鉴权
         */
        String hoscode = (String)paramMap.get("hoscode");
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        /**
         * 我们在service自己写的鉴权签名
         */
        boolean flag = hospitalService.getSign(paramMap);
        if(!flag){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.saveDepartment(paramMap);

        return Result.ok();
    }

    @ApiOperation(value = "删除科室")
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String) paramMap.get("hoscode");
        if (StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        /**
         * 验证签名
         */
        System.out.println(paramMap);
        boolean flag = hospitalService.getSign(paramMap);
        if(!flag){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        String depcode = (String) paramMap.get("depcode");
        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    /**
     * 排班列表
     * @param request
     * @return
     * schedule/list
     */
    @ApiOperation(value = "获取排班分页列表")
    @PostMapping("schedule/list")
    public Result schedule(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        /**
         * 我们这个方法调用的下面方法，他跑出异常就抛回来了
         */
        getJianquan(paramMap);
        /**
         * 这个方法是获取列表，一般都有分页模糊查询，要看分页是那些关键字
         * 去文档，需要设置初始化值的，
         */
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String)paramMap.get("limit"));

        /**
         * 把医院编号和科室编码取出来，方法哦vo中，等到了service里面，在copy进entity进行查询，忽略大小写的查询
         */
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        ScheduleQueryVo  scheduleQueryVo = new ScheduleQueryVo ();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        /**
         * 直接传递到service，直接把这个两个参数给entity，也没有错。
         * 文档中都是在controller中取出来装进vo在service中在把vo copy进entity。
         * 为什么？页面和controller交互使用的vo，service和数据库交互使用的entity。
         * 在互联网公司分的很明确，尤其一些老程序。
         */
        Page<Schedule> scheduleListPage = scheduleService.getScheduleListPage(page, limit, scheduleQueryVo);
        return Result.ok(scheduleListPage);
    }

    @PostMapping("/saveSchedule")
    @ApiOperation(value = "保存排班")
    public Result saveSchedule(HttpServletRequest request){
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        getJianquan(paramMap);
        scheduleService.saveSchedule(paramMap);
        return Result.ok();
    }

    private void getJianquan(Map<String, Object> paramMap) {
        /**
         * 获取医院编码
         */
        String hoscode = (String)paramMap.get("hoscode");
        //参数不正确
        if(StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        /**
         * 验证签名
         */
        boolean flag = hospitalService.getSign(paramMap);
        if(!flag){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
    }

    @ApiOperation(value = "删除排班")
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String) paramMap.get("hoscode");
        if (StringUtils.isEmpty(hoscode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        /**
         * 验证签名
         */
        System.out.println(paramMap);
        boolean flag = hospitalService.getSign(paramMap);
        if(!flag){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        String hosScheduleId = (String) paramMap.get("hosScheduleId");
        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();
    }
}
