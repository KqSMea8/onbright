package com.bright.apollo.controller;


import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.service.TopicServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("aliService")
public class aliServerController {

    @Autowired
    private TopicServer topicServer;


    @RequestMapping(value = "/toAli", method = RequestMethod.POST)
    @ResponseBody
    public String toAliService(@PathVariable CMDEnum cmd,  @PathVariable String inMsg,@PathVariable String deviceSerial) {
        byte [] inMsgByte = inMsg.getBytes();
        try {
            return  topicServer.request(cmd,inMsgByte,deviceSerial).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "send msg error";
    }
}
