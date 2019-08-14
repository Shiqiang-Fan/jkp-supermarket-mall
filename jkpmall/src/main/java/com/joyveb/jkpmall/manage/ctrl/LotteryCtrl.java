package com.joyveb.jkpmall.manage.ctrl;

import com.joyveb.jkpmall.manage.bean.AddressEntity;
import com.joyveb.jkpmall.manage.bean.CoreRecord;
import com.joyveb.jkpmall.manage.service.CoreRecordService;
import com.joyveb.jkpmall.manage.service.LotteryService;
import com.joyveb.jkpmall.util.FileUtil;
import com.joyveb.jkpmall.util.JsonParserUtils;
import com.joyveb.lbos.restful.common.ListInfo;
import com.joyveb.lbos.restful.common.ReturnInfo;
import com.joyveb.lbos.restful.spring.RequestJsonParam;
import com.joyveb.jkpmall.cache.ParaSysparameCache;
import com.joyveb.jkpmall.manage.bean.CoreProduct;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.joyveb.jkpmall.common.Constant.HASHMAP_INIT_VALUE;

@Slf4j
@Controller
@RequestMapping("lottery")
public class LotteryCtrl {

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private CoreRecordService coreRecordService;

    @Autowired
    private ParaSysparameCache sysparameCache;

    @Autowired
    private YunpianClient yunpianClient;

    /*@Autowired
    private AddressService addressService;*/


    /**
     * 获取产品信息
     * @param coreProduct
     * @param skip
     * @param limit
     * @return
     */
    @RequestMapping(value = "/selectProducts",method = RequestMethod.GET)
    @ResponseBody
    public ReturnInfo getProduct(
            @RequestJsonParam(value = "query",required = false) CoreProduct coreProduct,
            @RequestParam(value = "skip",defaultValue = "0",required = false) int skip,
            @RequestParam(value = "limit",defaultValue = "10",required = false) int limit) {
        ReturnInfo returnInfo = ReturnInfo.Faild;
        try {
            Map<String,Object> params = this.getCoreProduct(coreProduct,skip,limit);
            log.info("请求参数：{},\nquery:{}", params, coreProduct);
            List<CoreProduct> result = lotteryService.getProducts(params);
            int count = lotteryService.countCoreProduct(params);
            ListInfo listInfo = new ListInfo<>(count, result, skip, limit);
            returnInfo.setSuccess(true);
            returnInfo.setDescription("success");
            returnInfo.setRetObj(listInfo);
        } catch (Exception e) {
            log.warn("查询失败:"+e);
            returnInfo.setDescription("查询失败");
        }
        return returnInfo;
    }

    /**
     * 修改产品信息
     * @param coreProduct
     * @return
     */
    @RequestMapping(value = "/updateProduct",method = RequestMethod.POST)
    @ResponseBody
    public ReturnInfo updateProduct(@RequestBody CoreProduct coreProduct){
        ReturnInfo returnInfo = new ReturnInfo(false);
        try {
            if (coreProduct == null || coreProduct.getGameCode().isEmpty()){
                returnInfo.setDescription("请选择要修改的产品！！");
                return returnInfo;
            }
            lotteryService.updateProduct(coreProduct);
            returnInfo.setSuccess(true);
            returnInfo.setDescription("产品修改成功！！");
        } catch (Exception e) {
            log.warn("更新失败:"+e);
            returnInfo.setDescription("更新失败！！");
        }
        return returnInfo;
    }

    /**
     * 新增产品
     * @param product
     * @param file
     * @return
     */
    @RequestMapping(value = "/insertProduct",method = RequestMethod.POST)
    @ResponseBody
    public ReturnInfo insertProduct( String product, MultipartFile file){
        ReturnInfo returnInfo = new ReturnInfo(false);

        if (file.isEmpty()){
            returnInfo.setDescription("请选择文件！！！");
            return returnInfo;
        }

        //开始时间
        Long begin = System.currentTimeMillis();

        //获取文件名称
        String fileName = file.getOriginalFilename();

        try {

            Date date = new Date();
            fileName = date.getTime() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
            FileUtil.fileUpload(file.getBytes(), sysparameCache.getPicturePath(), fileName);

            log.info("图片上传成功！路径：/image/"+fileName);

            returnInfo.setRetObj("http://192.168.23.41:8089/image/" + fileName);
            returnInfo.setSuccess(true);
        }catch (Exception e){
            log.warn("图片上传异常："+e);
            returnInfo.setDescription("图片上传异常，请联系管理员！！");
            return returnInfo;
        }

        //结束时间
        Long end = System.currentTimeMillis();

        log.info("图片上传成功，共耗时：["+(end - begin) + "]毫秒！");

        CoreProduct coreProduct = JsonParserUtils.toBean(product,CoreProduct.class);
        coreProduct.setPicturePath("/image/" + fileName);

        try {
            if (coreProduct == null || coreProduct.getGameCode().isEmpty()){
                returnInfo.setDescription("请添加产品信息");
                return returnInfo;
            }


            CoreProduct result = lotteryService.getProduct(coreProduct.getGameCode());
            if (result != null){
                returnInfo.setDescription("该产品已存在");
                return returnInfo;
            }
            lotteryService.insertProduct(coreProduct);
            returnInfo.setSuccess(true);
            returnInfo.setDescription("添加成功");
        } catch (Exception e) {
            log.warn("添加失败:"+e);
            returnInfo.setDescription("添加失败");

            log.info("数据添加失败，删除上传的文件！");
            File newFile = new File(fileName);
            if (newFile.exists() && newFile.isFile()){
                newFile.delete();
                log.info("文件删除成功，");
            }

        }
        return returnInfo;
    }

    /**
     * 删除产品
     * @param gameCode
     * @return
     */
    @RequestMapping(value = "/deleteProduct",method = RequestMethod.GET)
    @ResponseBody
    public ReturnInfo deleteProduct(String gameCode){
        ReturnInfo returnInfo = new ReturnInfo(false);
        try {
            if (gameCode.isEmpty()){
                returnInfo.setDescription("请选择要删除的产品");
                return returnInfo;
            }
            lotteryService.deleteProduct(gameCode);
            returnInfo.setSuccess(true);
            returnInfo.setDescription("产品删除成功");
        } catch (Exception e) {
            log.warn("产品删除失败:"+e);
            returnInfo.setDescription("产品删除失败");
        }
        return returnInfo;
    }

    /**
     * 查询订单信息
     * @param coreRecord
     * @param skip
     * @param limit
     * @return
     */
    @RequestMapping(value = "/selectRecord",method = RequestMethod.GET)
    @ResponseBody
    public ReturnInfo selectRecord(
            @RequestJsonParam(value = "query",required = false) CoreRecord coreRecord,
            @RequestParam(value = "skip",defaultValue = "0",required = false) int skip,
            @RequestParam(value = "limit",defaultValue = "10",required = false) int limit){
        ReturnInfo returnInfo = ReturnInfo.Faild;
        try {
            Map<String,Object> params = this.getCoreRecord(coreRecord,skip,limit);
            List<CoreRecord> result = coreRecordService.selectRecord(params);
            log.info("请求参数：{},\nquery:{}", params, coreRecord);
            int count = coreRecordService.countCoreRecord(params);
            ListInfo listInfo = new ListInfo<>(count, result, skip, limit);
            returnInfo.setRetObj(listInfo);
            returnInfo.setSuccess(true);
            returnInfo.setDescription("success");
        } catch (Exception e) {
            log.warn("查询失败");
            returnInfo.setDescription("查询失败");
        }
        return returnInfo;
    }

    private Map<String, Object> getCoreRecord(CoreRecord coreRecord, int index, int limit) {

        Map<String,Object> map = new HashMap<>();
        if (coreRecord != null){
            map.put("uuid",coreRecord.getUuid());
            map.put("address",coreRecord.getAddress());
            map.put("gameCode",coreRecord.getGameCode());
            map.put("logistics",coreRecord.getLogistics());
            map.put("logisticsNum",coreRecord.getLogisticsNum());
            map.put("saleNum",coreRecord.getSaleNum());
            map.put("telephone",coreRecord.getTelephone());
            map.put("userName",coreRecord.getUserName());
            map.put("openid",coreRecord.getOpenid());
            map.put("amount",coreRecord.getAmount());
            map.put("dateTime",coreRecord.getDateTime());
        }
        map.put("index",index);
        map.put("limit",limit);

        return map;
    }

    /**
     * 修改订单信息
     * @param coreRecord
     * @return
     */
    @RequestMapping(value = "/updateRecord",method = RequestMethod.POST)
    @ResponseBody
    public ReturnInfo updateRecord(@RequestBody CoreRecord coreRecord){
        ReturnInfo returnInfo = new ReturnInfo(false);
        try {
            if (coreRecord == null || coreRecord.getUuid().isEmpty()){
                returnInfo.setDescription("请选择要修改的订单！！");
                return returnInfo;
            }
            coreRecord.setStatus("2");
            coreRecordService.updateRecord(coreRecord);
            returnInfo.setSuccess(true);
            returnInfo.setDescription("订单修改成功！！");
            sendMessage(coreRecord);

        } catch (Exception e) {
            log.warn("订单更新失败");
            returnInfo.setDescription("订单更新失败");
        }
        return returnInfo;
    }

    private Map<String,Object> getCoreProduct(CoreProduct coreProduct,int index,int limit){
        Map<String,Object> map = new HashMap<>(HASHMAP_INIT_VALUE);

        if (coreProduct != null){
            map.put("gameCode",coreProduct.getGameCode());
            map.put("gameName",coreProduct.getGameName());
            map.put("paperNum",coreProduct.getPaperNum());
            map.put("picturePath",coreProduct.getPicturePath());
            map.put("prizeAmount",coreProduct.getPrizeAmount());
            map.put("storeNum",coreProduct.getStoreNum());
            map.put("details",coreProduct.getDetail());
            map.put("parameter",coreProduct.getParameter());
            map.put("service",coreProduct.getService());
        }
        map.put("index",index);
        map.put("limit",limit);

        return map;
    }

    private boolean sendMessage(CoreRecord coreRecord) {

        //发送短信API
        log.warn("正在发送短信! 请稍等");
        Map<String, String> param = yunpianClient.newParam(2);
        param.put(YunpianClient.MOBILE, coreRecord.getTelephone());
        param.put(YunpianClient.TEXT, "【环球鑫彩】您的订单已发货，订单编号:"+coreRecord.getUuid()+
                ",物流公司："+coreRecord.getLogistics()+",快递单号："+coreRecord.getLogisticsNum()+
                "。若有疑问，请拨打01053226934。祝您购票愉快！");
        Result<SmsSingleSend> smsSingleSendResult = yunpianClient.sms().single_send(param);
        if (smsSingleSendResult.isSucc()) {
            log.warn("发送成功! 发送结果:" + smsSingleSendResult);
            return true;
        }
        log.warn("发送失败! 发送结果:" + smsSingleSendResult);
        return false;
    }

    /**
     * 新增订单信息
     */
    @RequestMapping(value = "/singlepageCommit",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ReturnInfo singlepageCommit(@RequestBody CoreRecord coreRecord){

        log.debug("开始下订单：coreRecord：[{}]",coreRecord);

        ReturnInfo returnInfo = new ReturnInfo(false);
        AddressEntity address = new AddressEntity();

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateformat = format.format(date);
        String dateformat1 = format1.format(date);

        Random rdm = new Random();
        int number = rdm.nextInt(1000) % (1000 - 100 + 1) + 100;
        String uuid = "J" + dateformat1 + number;

        /*try {
            address.setOpenId(uuid);
            address.setUserName(coreRecord.getUserName());
            address.setTelephone(coreRecord.getTelephone());
            address.setAddress(coreRecord.getAddress());
            //addressService.insertAddress(address);
            log.info("地址保存成功！！");
        }catch (Exception e){
            log.warn("地址保存失败！！",e);
            returnInfo.setDescription("下单失败address！！");
        }*/

        try {
            coreRecord.setUuid(uuid);
            coreRecord.setOpenid(uuid);
            coreRecord.setDateTime(dateformat);
            coreRecord.setAmount((Integer.parseInt(coreRecord.getAmount()) * 100) + "");
            coreRecord.setStatus("1");

            coreRecordService.insertOrder(coreRecord);
            log.info("订单保存成功");
            returnInfo.setSuccess(true);
            returnInfo.setDescription("下单成功！！");
        }catch (Exception e){
            log.info("订单保存失败！！",e);
            returnInfo.setDescription("下单失败order！！");
        }
        return returnInfo;
    }
}
