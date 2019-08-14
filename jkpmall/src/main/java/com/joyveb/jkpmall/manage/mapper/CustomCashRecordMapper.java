package com.joyveb.jkpmall.manage.mapper;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * @author wenchao
 * @date 15:05 2018/6/25
 */
@Repository
public interface CustomCashRecordMapper {

    List<Map<String, Object>> personalStatements(Map<String, Object> params);

    int countPersonalStatements(Map<String, Object> params);

    List<Map<String, Object>> channelStatements(Map<String, Object> params);

    int countChannelStatements(Map<String, Object> params);
}
