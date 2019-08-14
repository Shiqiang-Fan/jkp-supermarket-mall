package com.joyveb.jkpmall.service;



import com.joyveb.ncps.para.entity.ParaSysparame;
import com.joyveb.ncps.para.entity.ParaSysparameExample;
import com.joyveb.ncps.para.mapper.ParaSysparameMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangqiju
 * @date 2018-03-12 上午11:56
 */
@Service
public class CommonParaSysparameService {

    @Autowired
    private ParaSysparameMapper mapper;


    public List<ParaSysparame> selectAll() {
        return mapper.selectByExample(new ParaSysparameExample());
    }
}
