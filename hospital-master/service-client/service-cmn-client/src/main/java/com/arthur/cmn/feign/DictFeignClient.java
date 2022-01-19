package com.arthur.cmn.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @authur arthur
 * @desc
 */
@Repository
@FeignClient(value = "service-cmn")
public interface DictFeignClient {
    @GetMapping("/admin/cmn/dict/findName/{dictCode}/{value}")
    public String findName(@PathVariable("dictCode") String dictCode,
                           @PathVariable("value") String value);
    @GetMapping("/admin/cmn/dict/findName/{value}")
    public String findName(@PathVariable("value") String value);
}
