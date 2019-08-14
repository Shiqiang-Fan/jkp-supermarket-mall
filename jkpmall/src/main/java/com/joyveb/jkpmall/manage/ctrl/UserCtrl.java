package com.joyveb.jkpmall.manage.ctrl;

import com.joyveb.auth.restful.bean.AdminOperator;
import com.joyveb.jkpmall.manage.service.UserService;
import com.joyveb.lbos.restful.common.ReturnInfo;
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
 * @date 2018/6/27
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserCtrl {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public ReturnInfo getUserInfo(@RequestParam(value = "userName") String userName) {
		ReturnInfo returnInfo = new ReturnInfo(false);
		try {
			if (StringUtils.isBlank(userName)) {
				returnInfo.setDescription("参数异常");
				return returnInfo;
			}
			AdminOperator adminOperator = userService.getUserInfo(userName);
			returnInfo.setRetObj(adminOperator);
			returnInfo.setSuccess(true);
			return returnInfo;
		} catch (Exception e) {
			log.warn("查询用户信息异常", e);
			returnInfo.setDescription("查询用户信息异常，请联系管理员!");
			return returnInfo;
		}
	}
}
