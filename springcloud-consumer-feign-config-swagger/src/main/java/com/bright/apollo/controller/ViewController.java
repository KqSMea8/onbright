package com.bright.apollo.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年10月23日
 * @Version:1.1.0
 */
@Controller
@RequestMapping("view")
public class ViewController {

	/*@RequestMapping("CN")
	public String cn(HashMap<String, Object> map) {
		//return ("redirect:/html/.html");
		return ("PrivacyPolicyCN");
	}

	@RequestMapping("EN")
	public String en(HashMap<String, Object> map) {
		//return ("redirect:/html//PrivacyPolicyEN.html");
		return ("PrivacyPolicyEN");
 	}*/
	@RequestMapping("/{html}")
	public String cn(@PathVariable(required = true, value = "html") String html ) {
		//return ("redirect:/html/.html");
		return (html);
	}
}
