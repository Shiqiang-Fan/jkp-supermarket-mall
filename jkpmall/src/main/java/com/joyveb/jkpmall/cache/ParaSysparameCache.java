package com.joyveb.jkpmall.cache;

import com.joyveb.jkpmall.service.CommonParaSysparameService;
import com.joyveb.ncps.para.entity.ParaSysparame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yangqiju
 * @date 2018-03-12 上午11:48
 */
@Slf4j
@Component
public class ParaSysparameCache implements Cache {

	private static final int DEFAULT_LOGIN_FAILD_MAX_TIMES = 5;
	private static final String DEFAULT_AUTH_ROLE = "psuser,medium,bank";

	private static final String CONNECT_PICTURE_PATH = "/home/codes/dist/images/";

	private Map<String, ParaSysparame> cache = new HashMap<>();


	@Autowired
	private CommonParaSysparameService commonParaSysparameService;

	@PostConstruct
	@Override
	public void reload() {
		List<ParaSysparame> paraSysparames = commonParaSysparameService.selectAll();
		cache.clear();
		for (ParaSysparame info : paraSysparames) {
			cache.put(info.getThkey(), info);
		}
	}


	/**
	 * 当天登录的最大错误次数
	 */
	public int getLoginFaildMaxTimes() {
		ParaSysparame paraSysparame = cache.get("login.faild.max.times");
		if (paraSysparame == null) {
			return DEFAULT_LOGIN_FAILD_MAX_TIMES;
		}
		return NumberUtils.toInt(paraSysparame.getValue());
	}


	/**
	 * 获取奖励申请审核角色
	 */
	public String getAuthRoles() {
		ParaSysparame paraSysparame = cache.get("rent.rental.audit.role");
		if (paraSysparame == null) {
			return DEFAULT_AUTH_ROLE;
		}
		return paraSysparame.getValue();
	}

	public String getValueByKey(String key) {
		return cache.get(key).getValue();
	}

	/**
	 * 图片地址
	 */
	public String getPicturePath(){
		ParaSysparame paraSysparam = cache.get("connect.picture.path");
		if (paraSysparam == null){
			return CONNECT_PICTURE_PATH;
		}
		return paraSysparam.getValue();
	}
}