package com.lening.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lening.yygh.common.result.Result;
import com.lening.yygh.common.utils.MD5;
import com.lening.yygh.hosp.service.HospitalSetService;
import com.lening.yygh.model.hosp.HospitalSet;
import com.lening.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 创作时间：2021/4/26 16:47
 * 作者：牛胜浩
 *
 * @Api：swagger的注解，就是给前端看的
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
//@CrossOrigin
public class HospitalSetController {

    @Resource
    private HospitalSetService hospitalSetService;

    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("/findAll2")
    public List<HospitalSet> findAll2() {
        return hospitalSetService.list();
    }

    //1

    /**
     * RequestMapping：支持所有的请求方法，生成文档之后，很乱
     * 接口要遵循，restfull接口规范
     * 加入这个方法支持get请求，可以使用 getMapping来写
     * 还可以在requestmapping加配置   @RequestMapping(value = "/findAll",method = RequestMethod.GET)
     * @ApiOperation(value = "获取所有医院设置")  写实swagger的注解，就是声明这个方法是干什么的
     * 返回值是result，看看result里面都有什么
     */
    @ApiOperation(value = "获取所有医院设置")
    @RequestMapping("/findAll")
    public Result findAll() {
        List<HospitalSet> list = hospitalSetService.list();
        Result<List<HospitalSet>> ok = Result.ok(list);
        return ok;
    }

    //2

    /**
     * 和上面的方法在调用上面有区别
     * admin/hosp/hospitalSet/deleteHospById2?id=xxx
     * 400,参数类型不行
     * 405，请求方法不对
     * 415：协议头不对
     */
    @RequestMapping("/deleteHospById2")
    public Result deleteHospById2(String id) {
        boolean flag = hospitalSetService.removeById(Long.valueOf(id));
        if (flag) {
            //操作成功了，没有数据需要带回
            return Result.ok();
        } else {
            //删除失败
            return Result.fail();
        }
    }

    //3

    /**
     * PathVariable路径参数变量
     * admin/hosp/hospitalSet/xx
     * admin/hosp/hospitalSet/2
     * admin/hosp/hospitalSet/deleteHospById/xx
     */
    @DeleteMapping("/deleteHospById/{id}")
    @ApiOperation(value = "逻辑删除医院设置")
    public Result deleteHospById(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            //操作成功了，没有数据需要带回
            return Result.ok();
        } else {
            //删除失败
            return Result.fail();
        }
    }

    //4 添加医院设置
    @PostMapping("saveHospitalSet")
    @ApiOperation("添加医院")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //设置状态 1 使用 0 不能使用
        hospitalSet.setStatus(1);
        //签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        //调用service
        boolean save = hospitalSetService.save(hospitalSet);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //5 根据id获取医院设置
    //因为我们删除已经没有写方法名字了，所以这个id前面必须加方法名字了
    @GetMapping("getHospSet/{id}")
    @ApiOperation("按照id查询医院")
    public Result getHospSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //6 修改医院设置
    @PostMapping("updateHospitalSet")
    @ApiOperation("修改医院设置")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //8 医院设置锁定和解锁
    //就是把医院的状态修改一下
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    //9 发送签名秘钥
    @PutMapping("sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //使用手机短信的形式发给对方，发给对方医院的练习人
        //使用上面技术发送，阿里大于，乐信通等都可以

        return Result.ok();
    }

    //7

    /**
     * 条件查询+分页
     * current,当前页面，  pageNum
     * limit：页面大小    pageSize
     * RequestBody：异步传递对象，必须加注解
     * HospitalSetQueryVo，页面过来的，里面有多与数据库的参数，
     * 加入有年龄的，可以涉及范围查询等
     * required = false表示这个查询条件可以为空vo可以没有
     * RequestBody表示json自动把json转成实体类
     */
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result getHospListConn(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        //分页，常见page对象,把分页参数给他
        //还要检查分页的驱动有没有初始化
        Page<HospitalSet> page = new Page<>(current, limit);

        //条件查询
        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        if(hospitalSetQueryVo!=null){
            //医院名称
            String hosname = hospitalSetQueryVo.getHosname();
            //医院编号
            String hoscode = hospitalSetQueryVo.getHoscode();

            if(hosname!=null&&hosname.length()>=1){
                wrapper.like("hosname",hospitalSetQueryVo.getHosname());
            }
            //spring提供的一个string的工具类
            //StringUtils.isEmpty(hoscode) 判断等于空  null或者  ""
            if(!StringUtils.isEmpty(hoscode)){
                wrapper.like("hoscode", hospitalSetQueryVo.getHoscode());
            }
            /**
             * 列的名字，尤其有驼峰标识的时候，一定要注意
             * 建议属性名全小写
             */
            if(!StringUtils.isEmpty(hospitalSetQueryVo.getContactsName())){
                wrapper.like("contacts_name", hospitalSetQueryVo.getContactsName());
            }
        }

        //把条件丢失了
        //分页是好的，但是把查询条件丢失了，我们要是同步开发，肯定没有，但是别忘了这是异步
        //页面一般都是vue或者angular，双向绑定的，页面上有
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);
        return Result.ok(hospitalSetPage);
    }

    //10
    @DeleteMapping("/batchDeleteHospSet")
    @ApiOperation(value = "批量删除医院设置")
    public Result batchDeleteHospSet(@RequestBody Long[] ids) {
        boolean flag = hospitalSetService.removeByIds(Arrays.asList(ids));
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }
}
