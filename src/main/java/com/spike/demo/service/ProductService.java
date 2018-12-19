package com.spike.demo.service;

import com.spike.demo.bean.Product;

public interface ProductService {

    int updateStock(Integer pid);

    int updateStockVersion(Product product);

    Product checkStock(Integer pid);

}
