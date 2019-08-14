package com.joyveb.jkpmall.manage.service;


import com.joyveb.ncps.core.entity.TransferAccountRecord;
import com.joyveb.ncps.core.entity.TransferAccountRecordExample;
import com.joyveb.ncps.core.entity.TransferAccountRecordExample.Criteria;
import com.joyveb.ncps.core.entity.TransferAccountRecordKey;
import com.joyveb.ncps.core.mapper.TransferAccountRecordMapper;
import com.joyveb.jkpmall.manage.bean.TransferRecordBean;
import com.joyveb.jkpmall.manage.ctrl.TransferRecordCtrl.TransferRecordInfo;
import com.joyveb.jkpmall.manage.mapper.ChannelTransferRecordMapper;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Saxon
 * @date 2018/6/7
 */
@Service
public class TransferRecordService {

	@Autowired
	private TransferAccountRecordMapper transferRecordMapper;

	@Autowired
	private ChannelTransferRecordMapper channelTransferRecordMapper;

	public List<TransferAccountRecord> getTransferRecord(TransferRecordInfo info, int skip,
		int limit) {
		TransferAccountRecordExample example = this.findByMuilitParam(info);
		example.setOffset(skip);
		example.setLimit(limit);
		example.setOrderByClause("REDEEM_TIME DESC");
		return transferRecordMapper.selectByExample(example);
	}

	public int countByMulitParam(TransferRecordInfo info) {
		TransferAccountRecordExample example = this.findByMuilitParam(info);
		return transferRecordMapper.countByExample(example);
	}

	public List<TransferRecordBean> getChannelTransferRecord(Map<String, Object> info) {
		return channelTransferRecordMapper.getChannelTransferRecord(info);
	}

	public int getCount(Map<String, Object> info) {
		return channelTransferRecordMapper.getCount(info);
	}

	private TransferAccountRecordExample findByMuilitParam(TransferRecordInfo info) {
		TransferAccountRecordExample example = new TransferAccountRecordExample();
		Criteria criteria = example.createCriteria();
		if (info.getRedeemStatus() != null) {
			criteria.andRedeemStatusEqualTo(info.getRedeemStatus());
		}
		if (info.getCashType() != null) {
			criteria.andCashTypeEqualTo(info.getCashType());
		}
		if (info.getStartTime() != null && info.getEndTime() != null) {
			criteria.andRedeemTimeBetween(info.getStartTime(), info.getEndTime());
		}
		return example;
	}

	public TransferAccountRecord getByTicket(String ticketNumber) {
		TransferAccountRecordKey key = new TransferAccountRecordKey();
		key.setTicketNum(ticketNumber);
		return transferRecordMapper.selectByPrimaryKey(key);
	}

	public void reAward(TransferAccountRecord transferAccountRecord) {
		transferRecordMapper.updateByPrimaryKey(transferAccountRecord);
	}
}
