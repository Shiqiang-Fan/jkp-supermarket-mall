package com.joyveb.jkpmall.manage.ctrl;

import com.joyveb.jkpmall.manage.service.ParaControlService;
import com.joyveb.lbos.restful.common.ReturnInfo;
import com.joyveb.ncps.core.entity.ParaControl;
import com.joyveb.jkpmall.cache.SysParaControlCache;
import com.joyveb.jkpmall.common.Constant;
import com.joyveb.jkpmall.kafka.KafkaProducer;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Saxon
 * @date 2018/7/27
 */
@Slf4j
@Controller
@RequestMapping("/paraControl")
public class ParaControlCtrl {

	@Autowired
	private ParaControlService paraControlService;
	private @Autowired
	KafkaProducer noticeProducer;
	@Autowired
	private SysParaControlCache sysParaControlCache;
	private static final String TABLE_NAME = "ParaControl";

	@RequestMapping(value = "/selectAll", method = RequestMethod.GET)
	@ResponseBody
	public ReturnInfo selectAll() {
		ReturnInfo returnInfo = new ReturnInfo(false);
		try {
			List<ParaControl> list = paraControlService.getParaControlAll();
			returnInfo.setRetObj(list);
			returnInfo.setSuccess(true);
			return returnInfo;
		} catch (Exception e) {
			log.error("查询参数配置信息出现异常", e);
			returnInfo.setDescription("服务器出现问题，请联系管理员，我们会尽快解决");
			return returnInfo;
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	@ResponseBody
	public ReturnInfo update(@RequestBody ParaControl info) {
		ReturnInfo returnInfo = new ReturnInfo(false);
		try {
			paraControlService.updateParaControl(info);
			sysParaControlCache.reload();
			noticeProducer.send(Constant.KAFKA_TOPIC, TABLE_NAME);
			returnInfo.setSuccess(true);
			return returnInfo;
		} catch (Exception e) {
			log.error("修改参数配置信息出现异常", e);
			returnInfo.setDescription("服务器出现问题，请联系管理员，我们会尽快解决");
			return returnInfo;
		}
	}
}
