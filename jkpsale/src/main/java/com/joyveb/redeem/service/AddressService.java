package com.joyveb.redeem.service;

import com.joyveb.redeem.bean.AddressEntity;
import com.joyveb.redeem.common.ReturnInfo;
import com.joyveb.redeem.dao.AddressMapper;
import com.joyveb.redeem.dao.LotteryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    /**
     * 查询地址信息
     * t_user_address
     *
     * @return
     */
    public AddressEntity selectAddress(String openId) {
        return addressMapper.selectAddress(openId);
    }

    /**
     * 根据全部信息查询一个地址
     * @param addressEntity
     * @return
     */
    public AddressEntity selectAdd(AddressEntity addressEntity) {
        return addressMapper.selectAdd(addressEntity);
    }

    /**
     * 保存地址信息
     * t_user_address
     */
    public void saveAddress(AddressEntity addressEntity) {
        try {
            AddressEntity currentShippingAddress = addressMapper.selectAdd(addressEntity);
            if (null != currentShippingAddress) {
                addressMapper.delelteAddress(addressEntity);
            }
            addressMapper.saveAddress(addressEntity);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * 删除地址信息
     *
     * @param addressEntity
     */
    public void delelteAddress(AddressEntity addressEntity) {
        addressMapper.delelteAddress(addressEntity);
    }

    /**
     *修改地址信息
     * @param addressEntity
     */
    public void updateAddress(AddressEntity addressEntity) {
        addressMapper.updateAddress(addressEntity);
    }
}
