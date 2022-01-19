package com.arthur.service;

import com.arthur.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @authur arthur
 * @desc
 */
public interface DictService extends IService<Dict> {

    List<Dict> findChildData(Long id);


    void exportDict(HttpServletResponse response);

    void importDict(MultipartFile file);

    String findName(String s, String value);

    List<Dict> findByDictCode(String dictCode);
}
