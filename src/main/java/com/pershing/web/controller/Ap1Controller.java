package com.pershing.web.controller;

import java.util.HashMap;
import java.util.Map;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping("/ap1")
public class Ap1Controller {

	@GetMapping("/action1")
	public String action1() {
		Gson gson = new Gson();
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("p1", "aaa");
		resultMap.put("p2", "bbb");

		return gson.toJson(resultMap);
	}
}
