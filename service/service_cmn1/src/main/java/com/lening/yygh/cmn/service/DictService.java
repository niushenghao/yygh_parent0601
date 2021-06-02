package com.lening.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lening.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 牛胜浩
 * @date 2021/5/25 11:31
 */
public interface DictService extends IService<Dict> {

    //根据数据id查询子数据列表
    List<Dict> findChildDataByPid(Long id);

    //导出数据字典接口
    void exportExcel(HttpServletResponse response);

    //导入数据字典
    void importExcel(MultipartFile file);

    //根据dictcode和value查询
    String getDictName(String parentDictCode, String value);

    //根据dictCode获取下级节点
    List<Dict> findByDictCode(String dictCode);
}
