package com.lening.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lening.yygh.cmn.listener.DictListener;
import com.lening.yygh.cmn.mapper.DictMapper;
import com.lening.yygh.cmn.service.DictService;
import com.lening.yygh.model.cmn.Dict;
import com.lening.yygh.vo.cmn.DictEeVo;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;

/**
 * @author 牛胜浩
 * @date 2021/5/25 11:31
 * 需要继承mp给我们提供的一个实现类，然后再实现我们的自己接口
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {



    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildDataByPid(Long parentId) {
        /**
         * 我们是 QueryWrapper<Dict> queryWrapper = new QueryWrapper();
         * QueryWrapper queryWrapper = new QueryWrapper(Dict.class);
         */
        QueryWrapper<Dict> queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",parentId);
        /**
         * 我们的mapper继承了basemapper，用我们mapper完全可以
         * 但是我们也继承mp的实现类，mp的实现类其实已经引入了basemapper，可以直接使用
         */
        //List<Dict> list = dictMapper.selectList(queryWrapper);
        List<Dict> list = baseMapper.selectList(queryWrapper);
        /**
         * 把集合查询出来，我们需要判断这个dict有没有孩子，需要给这个字段赋值
         * 开始遍历
         */
        for (Dict dict : list) {
            /**
             * 怎么样判断有没有孩子
             * 用这个id去数据按照fid去查询，结果集list大于0就有孩子，否则就没有孩子
             */
            dict.setHasChildren(isHasChildren(parentId));
        }
        return list;
    }

    @Override
    public void exportExcel(HttpServletResponse response) {
        /**
         * 首先需要去把数据查出来啊
         * 没有条件就是全查了
         * 查询出来是一个entity的集合，我们需要把他复制进vo的集合
         */
        List<Dict> dicts = baseMapper.selectList(null);

        /*HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        Cell cell = row.getCell(0);
        */

        List<DictEeVo> dictVoList = new ArrayList<>();
        for (Dict dict : dicts) {
            DictEeVo vo = new DictEeVo();

            /**
             * 待会儿在测试
             */
            BeanUtils.copyProperties(dict, vo,DictEeVo.class);
            dictVoList.add(vo);
        }
        try {
            /**
             * 设置请求头，告诉请求是文件操作，并且是excel
             */
            response.setContentType("application/vnd.ms-excel");
            /**
             * 设置编码
             */
            response.setCharacterEncoding("utf-8");
            /**
             * 设置文件名字的中文编码
             */
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            /**
             * 以什么请求处理，告诉以文件下载的形式出来，用户可以修改文件名等9
             * attachment
             */
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
            /*
             * 这个就是把poi中需要手动把list封装成excel的操作被easyexcel封装了
             * sheet工作簿里面的单元表的名字
             * fileName工作簿的名字
             */
            EasyExcel.write(response.getOutputStream(),DictEeVo.class).sheet("数据字典").doWrite(dictVoList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @CacheEvict(value = "dict", allEntries=true)
    public void importExcel(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        //如果dictCode为空，直接根据value查询
        if(StringUtils.isEmpty(dictCode)) {
            //直接根据value查询
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);
            if (StringUtils.isEmpty(dictCode)){
                return null;
            }else {
                return dict.getName();
            }
        } else {//如果dictCode不为空，根据dictCode和value查询
            //根据dictcode查询dict对象，得到dict的id值
            Dict codeDict = this.getDictByDictCode(dictCode);
            Long parent_id = codeDict.getId();
            //根据parent_id和value进行查询
            Dict finalDict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parent_id)
                    .eq("value", value));
            return finalDict.getName();
        }
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Dict dict = this.getDictByDictCode(dictCode);
        List<Dict> childDataByPid = this.findChildDataByPid(dict.getId());
        return childDataByPid;
    }

    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code",dictCode);
        Dict codeDict = baseMapper.selectOne(wrapper);
        return codeDict;
    }

    private boolean isHasChildren(Long id){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", id);
        List list = baseMapper.selectList(queryWrapper);
        if(list!=null&&list.size()>=1){
            return true;
        }
        return false;
    }
}
