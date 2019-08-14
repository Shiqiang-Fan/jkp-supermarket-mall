package com.joyveb.jkpmall.manage.mapper;

import com.joyveb.jkpmall.manage.bean.FinaStat;
import com.joyveb.jkpmall.manage.bean.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 此类主要对多个表中所需要的值做一个关联查询,并返回
 */
public interface CashRecordMapper {

    /**
     * 返回值为 t_user_info t_cash_record 表中需要的值
     * @return List<UserEntity>类型
     */
    List<UserEntity>jsonToCashRecord();
    /**
     * 返回值为  t_transfer_account_record表中需要的值
     * @return List<FinaStat>类型
     */
    List<FinaStat> finaStat(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
