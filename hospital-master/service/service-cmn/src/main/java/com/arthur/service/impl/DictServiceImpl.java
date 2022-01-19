package com.arthur.service.impl;

import com.alibaba.excel.EasyExcel;
import com.arthur.listener.EasyExcelListener;
import com.arthur.mapper.DictMapper;
import com.arthur.model.cmn.Dict;
import com.arthur.service.DictService;
import com.arthur.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @authur arthur
 * @desc
 */
@Service(value = "dictService")
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dicts = baseMapper.selectList(wrapper);
        dicts.stream().forEach(d->{
            if(hasChildren(d.getId())){

                d.setHasChildren(true);
            }else {
                d.setHasChildren(false);
            }
        });
        return dicts;
    }

    @Override
    public void exportDict(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = null;
        try {
            fileName = URLEncoder.encode("数据字典", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

        List<Dict> dicts = baseMapper.selectList(null);
        ArrayList<DictEeVo> dictEeVos = new ArrayList<>();

        dicts.stream().forEach(dict -> {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict,dictEeVo,DictEeVo.class);
            dictEeVos.add(dictEeVo);
        });
        try {
            EasyExcel.write(response.getOutputStream(),DictEeVo.class).sheet("数据字典").doWrite(dictEeVos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @CacheEvict(value = "dict",allEntries = true)
    public void importDict(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new EasyExcelListener(baseMapper)).sheet("数据字典").doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String findName(String s, String value) {
        if(StringUtils.isEmpty(s)){
            //根据value查询
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }else {
            //根据dictCode与value查询
            Long parentId = getParentId(s);
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value",value);
            wrapper.eq("parent_id",parentId);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }

    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Long parentId = getParentId(dictCode);
        List<Dict> list = findChildData(parentId);
        return list;
    }

    private Long getParentId(String dictCode){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code",dictCode);
        Dict dict = baseMapper.selectOne(wrapper);
        return dict.getId();
    }

    private boolean hasChildren(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count>0;
    }
}
