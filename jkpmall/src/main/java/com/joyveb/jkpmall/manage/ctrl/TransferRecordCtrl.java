package com.joyveb.jkpmall.manage.ctrl;

import static com.joyveb.jkpmall.common.Constant.HASHMAP_INIT_VALUE;

import com.joyveb.jkpmall.manage.service.TransferRecordService;
import com.joyveb.lbos.restful.common.ListInfo;
import com.joyveb.lbos.restful.common.ReturnInfo;
import com.joyveb.lbos.restful.spring.RequestJsonParam;
import com.joyveb.ncps.core.entity.TransferAccountRecord;
import com.joyveb.jkpmall.manage.bean.TransferRecordBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Saxon
 * @date 2018/6/7
 */
@Slf4j
@Controller
@RequestMapping("/transfer_record")
public class TransferRecordCtrl {

	@Autowired
	private TransferRecordService transferRecordService;

	@RequestMapping(value = "/get_transfer_record", method = RequestMethod.GET)
	@ResponseBody
	public ReturnInfo getTransferRecord(
		@RequestJsonParam(value = "query", required = false) TransferRecordInfo query,
		@RequestParam(value = "skip", defaultValue = "0", required = false) int skip,
		@RequestParam(value = "limit", defaultValue = "10", required = false) int limit
	) {
		ReturnInfo returnInfo = new ReturnInfo(false);
		try {
			if (query == null) {
				query = new TransferRecordInfo();
			}
			List<TransferAccountRecord> list = transferRecordService
				.getTransferRecord(query, skip, limit);
			int count = transferRecordService
				.countByMulitParam(query);
			ListInfo listInfo = new ListInfo<>(count, list, skip, limit);
			returnInfo.setRetObj(listInfo);
			returnInfo.setSuccess(true);
			return returnInfo;
		} catch (Exception e) {
			log.warn("查询转账记录异常", e);
			returnInfo.setDescription("服务器异常");
			return returnInfo;
		}
	}

	@RequestMapping(value = "/get_channel_transfer_record", method = RequestMethod.GET)
	@ResponseBody
	public ReturnInfo getChannelTransferRecord(
		@RequestJsonParam(value = "query", required = false) TransferRecordInfo query,
		@RequestParam(value = "skip", defaultValue = "0", required = false) int skip,
		@RequestParam(value = "limit", defaultValue = "10", required = false) int limit) {
		ReturnInfo returnInfo = new ReturnInfo(false);
		try {
			Map<String, Object> params = new HashMap<>(HASHMAP_INIT_VALUE);
			if (query != null) {
				params = this.getQueryMap(query);
			}
			params.put("cashType", 1);
			params.put("skip", skip);
			params.put("limit", limit);
			List<TransferRecordBean> list = transferRecordService.getChannelTransferRecord(params);
			int count = transferRecordService.getCount(params);
			ListInfo listInfo = new ListInfo<>(count, list, skip, limit);
			returnInfo.setRetObj(listInfo);
			returnInfo.setSuccess(true);
			return returnInfo;
		} catch (Exception e) {
			log.warn("查询渠道转账记录异常", e);
			returnInfo.setDescription("查询渠道转账记录失败，请稍后再试!");
			return returnInfo;
		}
	}

	private Map<String, Object> getQueryMap(TransferRecordInfo query) {
		Map<String, Object> params = new HashMap<>(HASHMAP_INIT_VALUE);
		if (StringUtils.isNotBlank(query.getPrizeChannelName())) {
			params.put("prizeChannelName", query.getPrizeChannelName());
		}
		if (StringUtils.isNotBlank(query.getPrizeChannelId())) {
			params.put("prizeChannelId", query.getPrizeChannelId());
		}
		if (query.getProvinceNum() != null) {
			params.put("provinceNum", query.getProvinceNum());
		}
		if (query.getRedeemStatus() != null) {
			params.put("redeemStatus", query.getRedeemStatus());
		}
		if (query.getStartTime() != null) {
			params.put("startTime", query.getStartTime());
		}
		if (query.getEndTime() != null) {
			params.put("endTime", query.getEndTime());
		}
		return params;
	}

	@Data

	public static class TransferRecordInfo {

		private Long startTime;
		private Long endTime;
		private String prizeChannelName;
		private String prizeChannelId;
		private Integer provinceNum;
		private Integer redeemStatus;
		private Integer cashType;
	}
}
