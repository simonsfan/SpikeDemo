package com.spike.demo.service;

import com.spike.demo.bean.ProOrder;
import com.spike.demo.bean.Product;
import com.spike.demo.util.ResultEnum;
import com.spike.demo.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SpikeServiceImpl
 * @Description 秒杀逻辑实现类
 * @Author simonsfan
 * @Date 2018/12/18
 * Version  1.0
 */
@Slf4j
@Service
public class SpikeServiceImpl implements SpikeService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String SPIKE_KEY = "spike:limit";
    private final String SPIKE_VALUE = "over";
    private final String SPIKE_TOPIC = "spike_topic";

    @Override
    @Transactional
    public String spike(Product pro, String userName) {
        try {
            Integer productId = Integer.valueOf(pro.getId());
            //查库存前先判断是否缓存里有库存为0的标识
            String flag = (String) redisTemplate.opsForValue().get(SPIKE_KEY);
            if (StringUtils.isNotEmpty(flag)) {
                return ResultUtil.success(ResultEnum.SPIKEFAIL.getCode(), ResultEnum.SPIKEFAIL.getMessage());
            }
            Product product = productService.checkStock(productId);
            if (product.getProductStock() <= 0) {
                redisTemplate.opsForValue().set(SPIKE_KEY, SPIKE_VALUE, 12, TimeUnit.HOURS);
                return ResultUtil.success(ResultEnum.SPIKEFAIL.getCode(), ResultEnum.SPIKEFAIL.getMessage());
            }

            //更新库存
            int updateStockCount = productService.updateStockVersion(product);
            if (updateStockCount == 0) {
                throw new RuntimeException("over limit");
            }

            //推送给kafka处理 生成订单操作
            String content = productId+":"+userName;
            kafkaTemplate.send(SPIKE_TOPIC,content);
            return ResultUtil.success(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("username={},id={}秒杀失败", userName, pro.getId());
            return ResultUtil.success(ResultEnum.SPIKEFAIL.getCode(), ResultEnum.SPIKEFAIL.getMessage());
        }
    }


    @KafkaListener(topics = SPIKE_TOPIC)
    public void messageConsumerHandler(String content){
        log.info("进入kafka消费队列==========content：{}",content);
        //生成订单记录
        Long orderId = Long.parseLong(RandomStringUtils.randomNumeric(18));
        String[] split = content.split(":");
        orderService.createOrder(new ProOrder(Integer.valueOf(split[0]), orderId, split[1]));
        log.info("生成订单success");
    }

}
