package com.pershing.security;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.pershing.security.util.AbortUtils;

@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		if (exception instanceof DisabledException) {
			Map<String, Object> responseMessage = new HashMap<>();

			responseMessage.put("returnCode", "E003");

			responseMessage.put("data", "");
			responseMessage.put("returnMessage", exception.getMessage());

			responseMessage.put("serverTime", Long.toString(new Date().getTime()));
			responseMessage.put("success", false);

			System.out.println("failurehandle json");
			AbortUtils.renderJson(response, responseMessage);
		} else {
			System.out.println("failurehandle view");
			AbortUtils.renderView(response);
		}

	}

}
