package com.pershing.security;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.pershing.security.util.AbortUtils;

public class JwtAccessHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		Map<String, Object> responseMessage = new HashMap<>();

		responseMessage.put("returnCode", "E003");

		responseMessage.put("data", "");
		responseMessage.put("returnMessage", "身分驗證有誤");

		responseMessage.put("serverTime", Long.toString(new Date().getTime()));
		responseMessage.put("success", false);

		AbortUtils.renderJson(response, responseMessage);
	}

}
