package com.spike.demo.service;

import com.spike.demo.bean.Product;
import com.spike.demo.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @ClassName ProductServiceImpl
 * @Description TODO
 * @Author simonsfan
 * @Date 2018/12/18
 * Version  1.0
 */
@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    @Override
    public int updateStock(Integer pid) {
        return productMapper.updateStock(pid);
    }

    @Override
    public int updateStockVersion(Product product) {
        return productMapper.updateStockVersion(product);
    }

    @Override
    public Product checkStock(Integer pid) {
        return productMapper.selectByPrimaryKey(pid);
    }
}
