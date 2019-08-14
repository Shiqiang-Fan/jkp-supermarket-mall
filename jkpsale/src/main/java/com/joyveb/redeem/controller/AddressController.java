package com.joyveb.redeem.controller;

import com.joyveb.redeem.bean.AddressEntity;
import com.joyveb.redeem.common.ReturnInfo;
import com.joyveb.redeem.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author system
 * 收货地址控制器
 */
@Slf4j
@RestController
@CrossOrigin
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(value = "/selectAddress")
    @ResponseBody
    public ReturnInfo selectShippingAddress(@RequestParam String openId) {
        try {
            log.info("查询用户{}收货地址.", openId);
            AddressEntity addressEntity = addressService.selectAddress(openId);
            if (null == addressEntity) {
                log.info("未查询到用户{}收货地址", openId);
                return new ReturnInfo("未查询到用户收货地址", "", addressEntity, false);
            }
            log.info("查询成功,收货地址为:[" + addressEntity);
            return new ReturnInfo("查询成功", "", addressEntity, true);
        } catch (Exception e) {
            log.error("查询收货地址失败失败:" + e);
            return new ReturnInfo("查询失败", "", "", false);
        }
    }

    /**
     * 修改地址信息
     * @param addressEntity
     * @return
     */
    @RequestMapping(value = "/updateAddress")
    @ResponseBody
    public ReturnInfo updateAddress(@RequestBody AddressEntity addressEntity){
        try {
            if (addressEntity == null || addressEntity.getOpenId().isEmpty()){
                return new ReturnInfo("", "请选择要修改地址信息", "", true);
            }
            log.warn("开始修改地址信息,地址信息为: " + addressEntity);
            addressService.updateAddress(addressEntity);
            return new ReturnInfo("地址信息修改成功", "", addressEntity, true);
        } catch (Exception e) {
            log.warn("产品修改失败:"+e);
            return new ReturnInfo("产品修改失败", "", e, false);
        }
    }

    /**
     * 删除地址信息
     *
     * @param addressEntity
     * @return
     */
    @RequestMapping(value = "/deleteAddress")
    @ResponseBody
    public ReturnInfo deleteProduct(@RequestBody AddressEntity addressEntity) {
        try {
            if (StringUtils.isEmpty(addressEntity)) {
                log.warn("请勾选要删除的地址信息");
                return new ReturnInfo("", "请选择要删除的地址信息", "", false);
            }
            log.warn("开始进行地址删除,删除的地址信息: "+ addressEntity);
            AddressEntity addressEntity1 = addressService.selectAdd(addressEntity);
            if (addressEntity1 == null){
                return new ReturnInfo("您要删除的地址不存在", "", "", true);
            }
            addressService.delelteAddress(addressEntity1);
            log.warn("地址删除成功");
            return new ReturnInfo("地址删除成功", "", "", true);
        } catch (Exception e) {
            log.warn("地址删除失败:" + e);
            return new ReturnInfo("地址删除失败", "", e, false);
        }
    }
}