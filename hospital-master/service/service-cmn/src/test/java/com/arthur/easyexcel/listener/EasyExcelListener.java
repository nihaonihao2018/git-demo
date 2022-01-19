package com.arthur.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.arthur.easyexcel.mode.UserRead;

import java.util.Map;

/**
 * @authur arthur
 * @desc
 */
public class EasyExcelListener extends AnalysisEventListener<UserRead> {
    @Override
    public void invoke(UserRead userRead, AnalysisContext analysisContext) {
        System.out.println(userRead);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        //super.invokeHeadMap(headMap, context);
        System.out.println("表头信息"+headMap);
    }

    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        //super.invokeHead(headMap, context);
        System.out.println("表头信息......"+headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
