package com.joyveb.jkpmall.manage.mapper;

import com.joyveb.jkpmall.manage.bean.TransferRecordBean;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * @author Saxon
 */
@Repository
public interface ChannelTransferRecordMapper {

	List<TransferRecordBean> getChannelTransferRecord(Map<String, Object> info);

	int getCount(Map<String, Object> info);
}
