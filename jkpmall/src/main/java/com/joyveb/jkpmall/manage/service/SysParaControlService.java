package com.joyveb.jkpmall.manage.service;

import com.joyveb.ncps.core.entity.ParaControl;
import com.joyveb.ncps.core.entity.ParaControlExample;
import com.joyveb.ncps.core.mapper.ParaControlMapper;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Saxon
 * @date 2018/7/31
 */
@Slf4j
@Service
public class SysParaControlService {

	@Resource
	private ParaControlMapper sysparameMapper;

	/**
	 * 查询所有系统参数
	 */
	public List<ParaControl> selectAll() {
		return sysparameMapper.selectByExample(new ParaControlExample());
	}

}
