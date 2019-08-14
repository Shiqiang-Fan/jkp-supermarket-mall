package com.joyveb.jkpmall.cache;

import com.joyveb.jkpmall.manage.service.SysParaControlService;
import com.joyveb.ncps.core.entity.ParaControl;
import com.unlto.twls.commonutil.component.CommonUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Saxon
 * @date 2018/7/31
 */
@Slf4j
@Component("sysParaControlCache")
public class SysParaControlCache implements Cacheable {

	@Autowired
	private SysParaControlService sysParaControlService;
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private Map<String, Integer> sysParaMap = new HashMap<>();

	@Override
	public void reload() throws Exception {
		lock.writeLock().lock();
		try {
			List<ParaControl> records = sysParaControlService.selectAll();
			if (CommonUtils.isNotEmpty(records)) {
				sysParaMap.clear();
				for (ParaControl record : records) {
					if (StringUtils.isNotBlank(record.getControlCode())
						&& record.getControlMoney() != null) {
						sysParaMap.put(record.getControlCode(), record.getControlMoney());
					}
				}
				log.debug("SysParaControlMap数据:{}", sysParaMap);
			}
		} finally {
			lock.writeLock().unlock();
		}
	}


	public Integer getValueByKey(String key) {
		return sysParaMap.get(key);
	}
}
