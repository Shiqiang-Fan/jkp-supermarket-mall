package com.joyveb.jkpmall.manage.service;

import com.joyveb.ncps.core.entity.CashReCord;
import com.joyveb.ncps.core.entity.CashReCordExample;
import com.joyveb.ncps.core.entity.CashReCordExample.Criteria;
import com.joyveb.ncps.core.entity.CashReCordKey;
import com.joyveb.ncps.core.mapper.CashReCordMapper;
import com.joyveb.jkpmall.enums.RedeemStatusEnum;
import com.joyveb.jkpmall.manage.bean.CashRecordBean;
import com.joyveb.jkpmall.manage.ctrl.CashRecordCtrl.AwardDetailsInfo;
import com.joyveb.jkpmall.manage.mapper.CustomCashRecordMapper;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Saxon
 * @date 2018/6/6
 */
@Slf4j
@Service
public class CashRecordService {

	@Autowired
	private CashReCordMapper cashRecordMapper;
	private static final Long ONE_DAY = 86399999L;


	@Autowired
	private CustomCashRecordMapper customCashRecordMapper;

	public List<Map<String, Object>> personalStatements(Map<String, Object> params) {
		return customCashRecordMapper.personalStatements(params);
	}

	public int countPersonalStatements(Map<String, Object> params) {
		return customCashRecordMapper.countPersonalStatements(params);
	}

	public List<Map<String, Object>> channelStatements(Map<String, Object> params) {
		return customCashRecordMapper.channelStatements(params);
	}

	public int countChannelStatements(Map<String, Object> params) {
		return customCashRecordMapper.countChannelStatements(params);
	}

	public List<CashReCord> getCashRecord(CashRecordBean info, int skip, int limit) {
		CashReCordExample example = this.findByMuilitParam(info);
		example.setLimit(limit);
		example.setOffset(skip);
		example.setOrderByClause("REDEEM_TIME DESC");
		return cashRecordMapper.selectByExample(example);
	}

	public List<CashReCord> getAwardRecord(AwardDetailsInfo info, int skip, int limit) {
		CashReCordExample example = this.awardRecordParam(info);
		example.setLimit(limit);
		example.setOffset(skip);
		example.setOrderByClause("REDEEM_TIME DESC");
		return cashRecordMapper.selectByExample(example);
	}

	public int getCountByAward(AwardDetailsInfo info) {
		CashReCordExample example = this.awardRecordParam(info);
		return cashRecordMapper.countByExample(example);
	}

	public int countByMulitParam(CashRecordBean info) {
		CashReCordExample example = this.findByMuilitParam(info);
		return cashRecordMapper.countByExample(example);
	}

	private CashReCordExample awardRecordParam(AwardDetailsInfo info) {
		CashReCordExample example = new CashReCordExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(info.getGameName())) {
			criteria.andGameNameEqualTo(info.getGameName());
		}
		if (StringUtils.isNotBlank(info.getTicketNum())) {
			criteria.andTicketNumEqualTo(info.getTicketNum());
		}
		if (info.getCashStatus() != null) {
			criteria.andCashStatusEqualTo(info.getCashStatus());
		}
		if (info.getPrizeAmount() != null) {
			criteria.andPrizeAmountGreaterThanOrEqualTo(info.getPrizeAmount());
		}
		return example;
	}

	private CashReCordExample findByMuilitParam(CashRecordBean param) {
		CashReCordExample example = new CashReCordExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getOpenId())) {
			criteria.andOpenIdEqualTo(param.getOpenId());
		}
		if (StringUtils.isNotBlank(param.getBookNum())) {
			criteria.andBookNumEqualTo(param.getBookNum());
		}
		if (StringUtils.isNotBlank(param.getTicketNum())) {
			criteria.andTicketNumEqualTo(param.getTicketNum());
		}
		if (StringUtils.isNotBlank(param.getGameCode())) {
			criteria.andGameCodeEqualTo(param.getGameCode());
		}
		if (StringUtils.isNotBlank(param.getGameName())) {
			criteria.andGameNameEqualTo(param.getGameName());
		}
		if (StringUtils.isNotBlank(param.getPaperNum())) {
			criteria.andPaperNumEqualTo(param.getPaperNum());
		}
		if (StringUtils.isNotBlank(param.getId())) {
			criteria.andIdEqualTo(param.getId());
		}
		if (StringUtils.isNotBlank(param.getProductionBatch())) {
			criteria.andProductionBatchEqualTo(param.getProductionBatch());
		}
		if (StringUtils.isNotBlank(param.getSaleChannelId())) {
			criteria.andSaleChannelIdEqualTo(param.getSaleChannelId());
		}
		if (StringUtils.isNotBlank(param.getTelephone())) {
			criteria.andTelephoneEqualTo(param.getTelephone());
		}
		if (param.getRedeemStatus() != null) {
			criteria.andRedeemStatusEqualTo(param.getRedeemStatus());
		}
		if (param.getCashType() != null) {
			criteria.andCashTypeEqualTo(param.getCashType());
		}
		if (StringUtils.isNotBlank(param.getUserName())) {
			criteria.andUserNameEqualTo(param.getUserName());
		}
		if (param.getCashStatus() != null) {
			criteria.andCashStatusEqualTo(param.getCashStatus());
		}
		if (param.getStartTime() != null && param.getEndTime() != null) {
			criteria.andCashTimeBetween(param.getStartTime(), param.getEndTime());
		}
		if (param.getCashTime() != null) {
			criteria.andCashTimeBetween(param.getCashTime(), param.getCashTime() + ONE_DAY);
		}
		if (param.getRedeemTime() != null) {
			criteria.andRedeemTimeBetween(param.getRedeemTime(), param.getRedeemTime() + ONE_DAY);
		}
		return example;
	}

	public void upCashRecordByTicket(String ticketNumber) {
		CashReCordKey cashReCordKey = new CashReCordKey();
		cashReCordKey.setTicketNum(ticketNumber);
		CashReCord cashReCord = cashRecordMapper.selectByPrimaryKey(cashReCordKey);
		cashReCord.setRedeemStatus(RedeemStatusEnum.待返奖.getType());
		cashRecordMapper.updateByPrimaryKey(cashReCord);
	}
}
