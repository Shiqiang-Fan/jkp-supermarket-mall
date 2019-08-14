package com.joyveb.jkpmall.manage.service;

import com.joyveb.auth.restful.bean.AdminOperator;
import com.joyveb.auth.restful.bean.AdminOperatorExample;
import com.joyveb.auth.restful.bean.AdminOperatorExample.Criteria;
import com.joyveb.auth.restful.bean.AdminOperatorKey;
import com.joyveb.auth.restful.mapper.AdminOperatorMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Saxon
 * @date 2018/6/27
 */
@Slf4j
@Service
public class UserService {

	@Autowired
	private AdminOperatorMapper adminOperatorMapper;

	public AdminOperator getUserInfo(String userName) {
		AdminOperatorKey adminOperatorKey = new AdminOperatorKey();
		adminOperatorKey.setUserName(userName);
		return adminOperatorMapper.selectByPrimaryKey(adminOperatorKey);
	}
}
