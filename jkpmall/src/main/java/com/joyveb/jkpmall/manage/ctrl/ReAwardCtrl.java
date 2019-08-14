package com.joyveb.jkpmall.manage.ctrl;

import com.joyveb.jkpmall.manage.service.CashRecordService;
import com.joyveb.jkpmall.manage.service.TransferRecordService;
import com.joyveb.jkpmall.util.JsonParserUtils;
import com.joyveb.ncps.core.entity.TransferAccountRecord;
import com.joyveb.jkpmall.common.Constant;
import com.joyveb.jkpmall.common.ReturnInfo;
import com.joyveb.jkpmall.enums.RedeemStatusEnum;
import com.joyveb.jkpmall.kafka.KafkaProducer;
import com.joyveb.jkpmall.manage.bean.ReceiveBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/reaward")
public class ReAwardCtrl {


    @Autowired
    private TransferRecordService transferRecordService;
    @Autowired
    private CashRecordService cashRecordService;
    @Autowired
    private KafkaProducer reAwardProducer;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
        public ReturnInfo auditRefuse(@RequestParam(value = "ticketNumber") String ticketNumber) {
        ReturnInfo returnInfo = new ReturnInfo();
        try {
            TransferAccountRecord record = transferRecordService.getByTicket(ticketNumber);
            record.setRedeemStatus(RedeemStatusEnum.待返奖.getType());
            reAwardProducer.send(Constant.KAFKA_TOPIC_AWARD, JsonParserUtils.toJson(buildAwardMsg(record)));
            transferRecordService.reAward(record);
            cashRecordService.upCashRecordByTicket(ticketNumber);
            returnInfo.setSuccess(true);
            return returnInfo;
        } catch (Exception e) {
            log.error("重新兑奖失败，异常数据:{}", e);
            returnInfo.setDescription("请联系管理员，我们会尽快处理！");
            return returnInfo;
        }
    }

    private Object buildAwardMsg(TransferAccountRecord record) {
        ReceiveBean receiveBean = new ReceiveBean();
        receiveBean.setOpenId(record.getOpenId());
        receiveBean.setSecurityCode(record.getTicketNum());
        receiveBean.setPrizeAmount(record.getPrizeAmount().toString());
        return receiveBean;
    }
}
