package com.joyveb.jkpmall.manage.mapper;

import com.joyveb.jkpmall.manage.bean.AddressEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressMapper {

    void insertAddress(@Param("address") AddressEntity address);
}
