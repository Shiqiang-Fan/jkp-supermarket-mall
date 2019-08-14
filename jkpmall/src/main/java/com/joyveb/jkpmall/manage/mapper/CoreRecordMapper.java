package com.joyveb.jkpmall.manage.mapper;


import com.joyveb.jkpmall.manage.bean.CoreRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CoreRecordMapper {

    void insertOrder(CoreRecord coreRecord);

    void updateRecord(CoreRecord coreRecord);

    void deleteRecord(String uuid);

    List<CoreRecord> selectRecord(Map<String, Object> params);

    int countCoreRecord();

    CoreRecord selectOrderByUuid(@Param("uuid") String orderId);
}
