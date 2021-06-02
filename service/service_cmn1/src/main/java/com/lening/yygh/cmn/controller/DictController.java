package com.lening.yygh.cmn.controller;

import com.lening.yygh.cmn.service.DictService;
import com.lening.yygh.common.result.Result;
import com.lening.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 牛胜浩
 * @date 2021/5/25 11:41
 */
@RestController
@RequestMapping("/admin/cmn/dict")
@Api(description = "数据字典接口")
//@CrossOrigin
public class DictController {

    @Resource
    private DictService dictService;

    @GetMapping("/findChildData/{id}")
    @ApiOperation(value = "根据数据id查询子数据列表")
    public Result findChildData(@PathVariable Long id){
        /**
         * 我们按照这个id去数据库查询对应的父id，也就是pid，所以需要调节查询
         * 调节可以在这里写，可以可以去service实现类里面写，
         * 医院设置在controller里面写的
         */
        List<Dict> list = dictService.findChildDataByPid(id);
        return Result.ok(list);
    }

    /**
     * 可以把条件查询的全部带过来，这样的话，就是有条件导出
     * 要是没有条件那就是全部导出
     * 导出数据使用的response导出的，相当于用response打出去
     */
    @GetMapping("/exportExcel")
    @ApiOperation(value = "导出excel数据")
    public void exportExcel(HttpServletResponse response){
        /**
         * 一般情形下我们是在controller中往出导出，但是也可以从工具类或者servcice中导出都可以
         * 教案里面从service导出的
         * 把response传递到service了
         */
        dictService.exportExcel(response);
    }

    @ApiOperation(value = "导入")
    @PostMapping("importData")
    public Result importData(MultipartFile file) {
        dictService.importExcel(file);
        return Result.ok();
    }

    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode){
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

    //根据dictcode和value查询
    @ApiOperation(value = "获取数据字典名称")
    @GetMapping(value = "/getName/{parentDictCode}/{value}")
    public String getName(@ApiParam(name = "parentDictCode", value = "上级编码", required = true)
                              @PathVariable("parentDictCode") String parentDictCode,

                          @ApiParam(name = "value", value = "值", required = true)
                              @PathVariable("value") String value){
        String dictName = dictService.getDictName(parentDictCode, value);
        return dictName;
    }

    //根据value查询
    @ApiOperation(value = "获取数据字典名称")
    @ApiImplicitParam(name = "value", value = "值", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/getName/{value}")
    public String getName(
            @ApiParam(name = "value", value = "值", required = true)
            @PathVariable("value") String value) {
        return dictService.getDictName("",value);
    }
}
