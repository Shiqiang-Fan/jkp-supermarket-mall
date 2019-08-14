package com.joyveb.jkpmall.manage.service;

import com.joyveb.ncps.core.entity.ParaControl;
import com.joyveb.ncps.core.entity.ParaControlExample;
import com.joyveb.ncps.core.mapper.ParaControlMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Saxon
 * @date 2018/7/27
 */
@Slf4j
@Service
public class ParaControlService {

	@Autowired
	private ParaControlMapper paraControlMapper;

	public List<ParaControl> getParaControlAll() {
		ParaControlExample example = new ParaControlExample();
		return paraControlMapper.selectByExample(example);
	}

	public void updateParaControl(ParaControl paraControl) {
		paraControlMapper.updateByPrimaryKey(paraControl);
	}
}
