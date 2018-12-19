package com.spike.demo.service;

import com.spike.demo.bean.ProOrder;
import com.spike.demo.mapper.ProOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderServiceImpl
 * @Description TODO
 * @Author simonsfan
 * @Date 2018/12/18
 * Version  1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProOrderMapper proOrderMapper;

    @Override
    public int createOrder(ProOrder order) {
        return proOrderMapper.insert(order);
    }
}
