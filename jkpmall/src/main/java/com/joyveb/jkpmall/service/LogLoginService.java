package com.joyveb.jkpmall.service;


import com.joyveb.jkpmall.util.DateUtil;
import com.joyveb.ncps.log.entity.LogLoginInfo;
import com.joyveb.ncps.log.entity.LogLoginInfoExample;
import com.joyveb.ncps.log.entity.LogLoginInfoExample.Criteria;
import com.joyveb.ncps.log.mapper.LogLoginInfoMapper;
import com.joyveb.jkpmall.common.LoginStatusEnum;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangqiju
 * @date 2018-03-12 下午6:44
 */
@Service
public class LogLoginService {

    @Autowired
    private LogLoginInfoMapper mapper;

    public void insert(LogLoginInfo loginLog) {
        mapper.insert(loginLog);
    }

    public int selectTodayLoginFaild(String username, Integer platform) {
        LogLoginInfoExample example = new LogLoginInfoExample();
        Date now = new Date();
        long date2StartTime = DateUtil.getDate2StartTime(now);
        long date2EndTime = DateUtil.getDate2EndTime(now);

        Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(username);
        criteria.andCreateDateBetween(date2StartTime, date2EndTime);
        criteria.andPlatformEqualTo(platform);
        criteria.andStatusEqualTo(LoginStatusEnum.FAILD.getCode());

        return mapper.countByExample(example);
    }
}
