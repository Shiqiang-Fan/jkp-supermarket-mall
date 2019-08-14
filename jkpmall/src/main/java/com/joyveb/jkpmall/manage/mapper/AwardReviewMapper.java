package com.joyveb.jkpmall.manage.mapper;

import com.joyveb.jkpmall.manage.bean.AwardReviewBean;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author Saxon
 */
@Repository
public interface AwardReviewMapper {

	List<AwardReviewBean> getAwardInfo();

	int updateAuditStatus(AwardReviewBean info);
}
