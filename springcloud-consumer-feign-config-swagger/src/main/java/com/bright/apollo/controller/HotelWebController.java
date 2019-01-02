package com.bright.apollo.controller;

import com.bright.apollo.redis.RedisBussines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/OBHotel")
@Controller
public class HotelWebController {
    private static final Logger logger = LoggerFactory.getLogger(HotelWebController.class);

    @Autowired
    private RedisBussines redisBussines;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String login(HttpServletRequest request) throws Exception {
        logger.info(" ====== holtel login ====== ");
        return "index";
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "/checkIn", method = RequestMethod.GET)
    public String checkIn(HttpServletRequest request) throws Exception {
        logger.info(" ====== holtel checkIn ====== ");
        return "checkIn";
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "/checkOut", method = RequestMethod.GET)
    public String checkOut(HttpServletRequest request) throws Exception {
        logger.info(" ====== holtel checkOut ====== ");
        return "checkOut";
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "/roomStatus", method = RequestMethod.GET)
    public String roomStatus(HttpServletRequest request, Model model) throws Exception {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i =0;i<10;i++){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put(""+i,"10"+i);
            list.add(map);
        }
        model.addAttribute("roomList",list);
        logger.info(" ====== holtel roomStatus ====== ");
        return "roomStatus";
    }
}
