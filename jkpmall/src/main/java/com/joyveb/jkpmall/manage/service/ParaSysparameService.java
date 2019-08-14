package com.joyveb.jkpmall.manage.service;


import com.joyveb.ncps.para.entity.ParaSysparame;
import com.joyveb.ncps.para.entity.ParaSysparameExample;
import com.joyveb.ncps.para.entity.ParaSysparameExample.Criteria;
import com.joyveb.ncps.para.entity.ParaSysparameKey;
import com.joyveb.ncps.para.mapper.ParaSysparameMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author: saxon
* @description:
* @date: 11:46 2018/3/27
*/
@Slf4j
@Service
public class ParaSysparameService {

	@Autowired
	private ParaSysparameMapper dbmapper;

	public int insert(ParaSysparame paraSysparame) {
		return dbmapper.insert(paraSysparame);
	}

	public int update(ParaSysparame paraSysparame) {
		return dbmapper.updateByPrimaryKey(paraSysparame);
	}

	public int delete(List<ParaSysparameKey> keys) {
		for (ParaSysparameKey key : keys) {
			dbmapper.deleteByPrimaryKey(key);
		}
		return keys.size();
	}

	public List<ParaSysparame> find(ParaSysparame param, int skip, int limit) {
		ParaSysparameExample paraSysparameExample = findByParaSysparame(param);
		paraSysparameExample.setLimit(limit);
		paraSysparameExample.setOffset(skip);
		return dbmapper.selectByExample(paraSysparameExample);
	}

	public int findCount(ParaSysparame param) {
		ParaSysparameExample example = findByParaSysparame(param);
		return dbmapper.countByExample(example);
	}

	public boolean keyIsBlank(String thkey) {
		ParaSysparameExample example = new ParaSysparameExample();
		Criteria criteria = example.createCriteria();
		criteria.andThkeyEqualTo(thkey);
		int count = dbmapper.countByExample(example);
		if (count > 0) {
			return false;
		}
		return true;
	}

	private ParaSysparameExample findByParaSysparame(ParaSysparame paraSysparame) {
		ParaSysparameExample example = new ParaSysparameExample();
		Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(paraSysparame.getThkey())) {
			criteria.andThkeyEqualTo(paraSysparame.getThkey());
		}
		if (StringUtils.isNotBlank(paraSysparame.getModule())) {
			criteria.andModuleEqualTo(paraSysparame.getModule());
		}
		if (StringUtils.isNotBlank(paraSysparame.getValue())) {
			criteria.andValueLike("%" + paraSysparame.getValue() + "%");
		}
		if (StringUtils.isNotBlank(paraSysparame.getDes())) {
			criteria.andDesLike("%" + paraSysparame.getDes() + "%");
		}
		return example;
	}
}
