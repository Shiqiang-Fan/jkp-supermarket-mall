package com.joyveb.jkpmall.manage.ctrl;

import static com.joyveb.jkpmall.common.Constant.HASHMAP_INIT_VALUE;

import com.joyveb.jkpmall.manage.service.CashRecordService;
import com.joyveb.lbos.restful.common.ListInfo;
import com.joyveb.lbos.restful.common.ReturnInfo;
import com.joyveb.lbos.restful.spring.RequestJsonParam;
import com.joyveb.ncps.core.entity.CashReCord;
import com.joyveb.jkpmall.cache.SysParaControlCache;
import com.joyveb.jkpmall.manage.bean.CashRecordBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Saxon
 * @date 2018/6/6
 */
@Slf4j
@Controller
@RequestMapping("/redeem_record")
public class CashRecordCtrl {

	@Autowired
	private CashRecordService cashRecordService;
	@Autowired
	private SysParaControlCache paraSysparameCache;

	@RequestMapping(value = "/get_personal_statements", method = RequestMethod.GET)
	@ResponseBody
	public ReturnInfo personalStatements(
		@RequestJsonParam(value = "query", required = false) ChannelStatementsParamsBean query,
		@RequestParam(value = "skip", defaultValue = "0", required = false) int skip,
		@RequestParam(value = "limit", defaultValue = "10", required = false) int limit) {
		ReturnInfo returnInfo = ReturnInfo.Faild;
		try {
			Map<String, Object> params = this.getStatementsParams(query, skip, limit);
			log.info("请求参数：{},\nquery:{}", params, query);
			List<Map<String, Object>> info = cashRecordService.personalStatements(params);
			int count = cashRecordService.countPersonalStatements(params);
			ListInfo listInfo = new ListInfo<>(count, info, skip, limit);
			returnInfo.setSuccess(true);
			returnInfo.setDescription("success");
			returnInfo.setRetObj(listInfo);
		} catch (Exception e) {
			returnInfo.setDescription("获取个人结算单失败，请稍候重试！");
			log.error("获取个人结算单异常", e);
		}
		return returnInfo;
	}

	private Map<String, Object> getStatementsParams(ChannelStatementsParamsBean query,
		int skip, int limit) {
		Map<String, Object> params = new HashMap<>(HASHMAP_INIT_VALUE);
		if (query != null) {
			params.put("cashType", query.getCashType());
			params.put("saleChannelName", query.getSaleChannelName());
			params.put("provinceId", query.getProvinceId());
			params.put("startTime", query.getStartTime());
			params.put("endTime", query.getEndTime());
			params.put("redeemStatus", query.getRedeemStatus());
			params.put("prizeChannelId", query.getPrizeChannelId());
			params.put("prizeChannelName", query.getPrizeChannelName());
		}
		params.put("skip", skip);
		params.put("limit", limit);

		return params;
	}

	@Data
	private static class ChannelStatementsParamsBean {

		private Integer cashType;
		private String prizeChannelId;
		private String prizeChannelName;
		private String saleChannelName;
		private Integer provinceId;
		private Long startTime;
		private Long endTime;
		private Integer redeemStatus;

	}


	@RequestMapping(value = "/get_record", method = RequestMethod.GET)
	@ResponseBody
	public ReturnInfo getCashRecordData(
		@RequestJsonParam(value = "query", required = false) CashRecordBean query,
		@RequestParam(value = "skip", defaultValue = "0", required = false) int skip,
		@RequestParam(value = "limit", defaultValue = "10", required = false) int limit) {
		ReturnInfo returnInfo = new ReturnInfo(false);
		try {
			if (query.getCashType() == null) {
				returnInfo.setDescription("兑奖类型为空");
				return returnInfo;
			}
			List<CashReCord> info = cashRecordService.getCashRecord(query, skip, limit);
			int count = cashRecordService.countByMulitParam(query);
			ListInfo listInfo = new ListInfo<>(count, info, skip, limit);
			returnInfo.setRetObj(listInfo);
			returnInfo.setSuccess(true);
			return returnInfo;
		} catch (Exception e) {
			log.warn("公众号兑奖查询异常", e);
			returnInfo.setDescription("系统异常");
			return returnInfo;
		}
	}

	@Data
	public static class AwardDetailsInfo {

		private String gameName;
		private String ticketNum;
		private Integer prizeAmount;
		private Integer provinceId;
		private Integer cashStatus;
	}
}
