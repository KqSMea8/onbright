package com.bright.apollo.controller;

import com.bright.apollo.redis.RedisBussines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("hotelweb")
@Controller
public class HotelWebController {
    private static final Logger logger = LoggerFactory.getLogger(HotelWebController.class);

    @Autowired
    private RedisBussines redisBussines;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String tmallCmd() throws Exception {
        logger.info(" ====== holtel login ====== ");
        return "index";
    }
}
