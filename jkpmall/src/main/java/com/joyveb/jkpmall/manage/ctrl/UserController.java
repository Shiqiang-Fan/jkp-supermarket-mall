//package com.joyveb.redeem.manage.ctrl;
//
//import FinaStat;
//import UserEntity;
//import CashRecordMapper;
//import JsonParserUtils;
//import org.apache.ibatis.annotations.Param;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//
//import java.sql.Date;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.List;
//
///**
// * 此类主要将表中的某些字段以Json格式返回
// */
//@Controller
//@RequestMapping(value = "/user")
//public class UserController {
//    @Autowired
//    private CashRecordMapper cashRecordMapper;
//
//    /**
//     * 将t_cash_record表中的openId;count;realName;telephone;prizeAmount;以JSON的形式返回
//     * @return Json 格式的字符串
//     */
//    @ResponseBody
//    @RequestMapping(value = "")
//    public String  jsonToCashRecord() {jsonToCashRecord
//        List<UserEntity> userEntities = cashRecordMapper.jsonToCashRecord();
//        String str = JsonParserUtils.toJson(userEntities);
//        return str;
//    }
//
//    /**
//     * 将t_transfer_account_record表中的天数 总金额 以JSON的形式返回
//     * @return Json 格式的字符串
//     */
//    @ResponseBody
//    @RequestMapping(value = "/finance")
//    public String  finaStat(@Param("startTime") long startTime , @Param("endTime") long endTime) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date dateStartTime = new Date(startTime);
//        Date dateEndTime = new Date(endTime);
//        String finStartTime = sdf.format(dateStartTime).toString();
//        String finEndTime = sdf.format(dateEndTime).toString();
//        List<FinaStat> userEntities = cashRecordMapper.finaStat(finStartTime, finEndTime);
//        String str= JsonParserUtils.toJson(userEntities);
//        return  str;
//    }
//
//
//}
