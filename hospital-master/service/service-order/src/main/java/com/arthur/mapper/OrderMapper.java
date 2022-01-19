package com.arthur.mapper;

import com.arthur.model.order.OrderInfo;
import com.arthur.vo.order.OrderCountQueryVo;
import com.arthur.vo.order.OrderCountVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @authur arthur
 * @desc
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderInfo> {
    List<OrderCountVo> selectOrderCount(@Param("vo")OrderCountQueryVo orderCountQueryVo);
}
