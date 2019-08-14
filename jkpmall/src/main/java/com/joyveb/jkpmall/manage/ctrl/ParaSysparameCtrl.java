package com.joyveb.jkpmall.manage.ctrl;

import com.joyveb.jkpmall.manage.service.ParaSysparameService;
import com.joyveb.lbos.restful.common.ListInfo;
import com.joyveb.lbos.restful.common.ReturnInfo;
import com.joyveb.lbos.restful.spring.RequestJsonParam;
import com.joyveb.ncps.para.entity.ParaSysparame;
import com.joyveb.ncps.para.entity.ParaSysparameKey;
import com.joyveb.jkpmall.common.Constant;
import com.joyveb.jkpmall.kafka.KafkaProducer;
import com.joyveb.jkpmall.cache.ParaSysparameCache;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/manage/paraSysparame")
/**
 * @author zhaxin
 * @date
 */
public class ParaSysparameCtrl {

	private @Resource
	KafkaProducer noticeProducer;
	private @Resource
	ParaSysparameService dbService;
	private @Resource
	ParaSysparameCache paraSysparameCache;
	private static final String TABLE_NAME = "ParaSysparame";

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ReturnInfo insert(@RequestBody ParaSysparame info) {
		try {
			if (dbService.insert(info) > 0) {
				paraSysparameCache.reload();
				noticeProducer.send(Constant.KAFKA_TOPIC, TABLE_NAME);
				return ReturnInfo.Success;
			}
		} catch (DuplicateKeyException e) {
			log.trace("主键冲突", e);
			return ReturnInfo.Faild;
		} catch (Exception e) {
			log.warn("系统异常", e);
			return ReturnInfo.Faild;
		}
		return ReturnInfo.Faild;
	}

	@RequestMapping(value = "/check_key", method = RequestMethod.GET)
	@ResponseBody
	public ReturnInfo checkKey(@RequestParam String thkey) {
		ReturnInfo returnInfo = new ReturnInfo(false);
		try {
			boolean result = dbService.keyIsBlank(thkey);
			if (!result) {
				returnInfo.setDescription("配置key已存在！");
				return returnInfo;
			}
		} catch (Exception e) {
			log.warn("key校验异常", e);
			returnInfo.setDescription("系统异常");
			return returnInfo;
		}
		returnInfo.setSuccess(true);
		return returnInfo;
	}


	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ReturnInfo delete(@RequestBody List<String> query) {
		ReturnInfo returnInfo = new ReturnInfo(false);
		try {
			List<ParaSysparameKey> keys = new ArrayList<>(Constant.HASHMAP_INIT_VALUE);
			for (String thkey : query) {
				ParaSysparameKey key = new ParaSysparameKey();
				key.setThkey(thkey);
				keys.add(key);
			}
			dbService.delete(keys);
			paraSysparameCache.reload();
			noticeProducer.send(Constant.KAFKA_TOPIC, TABLE_NAME);
			returnInfo.setSuccess(true);
			return returnInfo;
		} catch (Exception e) {
			log.warn("删除出错", e);
			returnInfo.setDescription("服务器出错!");
			return returnInfo;
		}
	}

	@RequestMapping(value = "", method = RequestMethod.PUT)
	@ResponseBody
	public ReturnInfo update(@RequestBody ParaSysparame info,
		HttpServletRequest req) {
		try {
			if (dbService.update(info) > 0) {
				paraSysparameCache.reload();
				noticeProducer.send(Constant.KAFKA_TOPIC, TABLE_NAME);
				return ReturnInfo.Success;
			}
		} catch (DuplicateKeyException e) {
			log.trace("主键冲突", e);
			return ReturnInfo.Faild;
		} catch (Exception e) {
			log.warn("系统异常", e);
			return ReturnInfo.Faild;
		}
		return ReturnInfo.Faild;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public Object newGet(@RequestJsonParam(value = "query", required = false) ParaSysparame query,
		@RequestParam(value = "skip", defaultValue = "0", required = false) int skip,
		@RequestParam(value = "limit", defaultValue = "10", required = false) int limit) {
		try {
			ParaSysparame info = new ParaSysparame();
			if (query != null) {
				info = query;
			}
			List<ParaSysparame> infos = dbService.find(info, skip, limit);
			int count = dbService.findCount(info);
			return new ListInfo<>(count, infos, skip, limit);
		} catch (Exception e) {
			log.warn("系统异常", e);
			return null;
		}
	}
}
