package com.arthur.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.arthur.mapper.DictMapper;
import com.arthur.model.cmn.Dict;
import com.arthur.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;

/**
 * @authur arthur
 * @desc
 */
public class EasyExcelListener extends AnalysisEventListener<DictEeVo> {

    private DictMapper dictMapper;
    public EasyExcelListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict,Dict.class);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
