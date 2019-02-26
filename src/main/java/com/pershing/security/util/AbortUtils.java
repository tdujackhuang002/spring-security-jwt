package com.pershing.security.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class AbortUtils {

	public static boolean isAjaxReqeust(HttpServletRequest request) {
		String ajaxFlag = request.getHeader("X-Requested-With");
		return ajaxFlag != null && "XMLHttpRequest".equals(ajaxFlag);
	}

	/**
	 * 回傳畫面
	 * @throws IOException 
	 */
	public static void renderView(HttpServletResponse response) throws IOException {

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
	}

	/**
	 * 回傳Json
	 * 
	 * @param response
	 * @param obj
	 */
	public static void renderJson(HttpServletResponse response, Object obj) {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PUT");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		try {
			PrintWriter out = response.getWriter();
			out.write(new Gson().toJson(obj));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
