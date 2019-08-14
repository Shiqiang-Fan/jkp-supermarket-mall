package com.joyveb.jkpmall.manage.service;

import com.joyveb.jkpmall.manage.bean.CoreRecord;
import com.joyveb.jkpmall.manage.mapper.CoreRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "coreRecordService")
public class CoreRecordService {

    @Autowired
    private CoreRecordMapper coreRecordMapper;

    public void insertOrder(CoreRecord coreRecord) {

        coreRecordMapper.insertOrder(coreRecord);
    }

    public void updateRecord(CoreRecord coreRecord) {
        coreRecordMapper.updateRecord(coreRecord);
    }

    public void deleteRecord(String uuid) {
        coreRecordMapper.deleteRecord(uuid);
    }

    public List<CoreRecord> selectRecord(Map<String, Object> params) {
        return coreRecordMapper.selectRecord(params);
    }

    public int countCoreRecord(Map<String, Object> params) {
        return coreRecordMapper.countCoreRecord();
    }

    public CoreRecord selectOrderByUuid(String orderId) {
        return coreRecordMapper.selectOrderByUuid(orderId);
    }
}
