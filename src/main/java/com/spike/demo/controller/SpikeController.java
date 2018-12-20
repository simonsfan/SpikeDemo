package com.spike.demo.controller;

import com.spike.demo.annotation.AccessLimit;
import com.spike.demo.bean.Product;
import com.spike.demo.service.SpikeService;
import com.spike.demo.util.ResultEnum;
import com.spike.demo.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName SpikeController
 * @Description TODO
 * @Author simonsfan
 * @Date 2018/12/17
 * Version  1.0
 */
@Slf4j
@Controller
public class SpikeController {

    @Autowired
    private SpikeService spikeService;

    @AccessLimit(time = 1, threshold = 5)
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/spike")
    public String spike(@RequestParam(value = "id", required = false) String id,
                        @RequestParam(value = "username", required = false) String userName) {
        log.info("/spike params:id={},username={}", id, userName);
        Product product = new Product();
        try {
            if (StringUtils.isEmpty(id)) {
                return ResultUtil.fail();
            }
            Integer productId = Integer.valueOf(id);
            product.setId(productId);
        } catch (Exception e) {
            log.error("spike fail username={},e={}", userName, e.toString());
            return ResultUtil.success(ResultEnum.SPIKEFAIL.getCode(), ResultEnum.SPIKEFAIL.getMessage());
        }
        return spikeService.spike(product, userName);
    }

}
