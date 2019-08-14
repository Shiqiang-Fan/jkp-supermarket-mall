package com.joyveb.jkpmall.manage.mapper;

import com.joyveb.jkpmall.manage.bean.CoreProduct;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LotteryMapper {


    void updateProduct(CoreProduct coreProduct);

    void insertProduct(CoreProduct coreProduct);

    void deleteProduct(String gameCode);

    CoreProduct getProduct(String gameCode);

    List<CoreProduct> getProducts(Map<String, Object> params);

    int countCoreProduct();
}
