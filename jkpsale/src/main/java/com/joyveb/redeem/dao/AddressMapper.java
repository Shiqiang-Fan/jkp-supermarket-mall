package com.joyveb.redeem.dao;

import com.joyveb.redeem.bean.AddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@Mapper
public interface AddressMapper {

    /**
     *  查询地址信息
     *  t_core_record
     * @return
     */
    AddressEntity selectAddress(String openId);

    /**
     *  查询一条地址信息
     *  t_core_record
     * @return
     */
    AddressEntity selectAdd(AddressEntity addressEntity);

    /**
     *  保存地址信息
     *  t_core_record
     * @return
     */
    void saveAddress(AddressEntity addressEntity);

    /**
     *  删除地址信息
     *  t_core_record
     * @return
     */
    void delelteAddress(AddressEntity addressEntity);

    /**
     * 修改地址
     * @param addressEntity
     */
    void updateAddress(AddressEntity addressEntity);
}
