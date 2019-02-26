package com.pershing.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.pershing.security.model.PershingUser;
import com.pershing.security.model.UserTokenDetail;
import com.pershing.security.util.JwtToolUtil;

/**
 * 登入時檢核身分的filter 1.將帳號密碼資訊存入AuthenticationToken中交給AuthenticationManager進行驗證
 * 
 * @author Jack
 *
 */
public class JwtLoginProccessFilter extends AbstractAuthenticationProcessingFilter {

	private final Logger logger = LoggerFactory.getLogger(JwtLoginProccessFilter.class);

	@Autowired
	private JwtToolUtil jwtTokenTool;

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	public JwtLoginProccessFilter(String url, AuthenticationManager authManager) {
		super(url);

		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		logger.info(">>> execute JwtLoginProccessFilter.attemptAuthentication...");

		String loginName = request.getParameter("username");
		String password = request.getParameter("password");

		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(loginName, password));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		UsernamePasswordAuthenticationToken usernameAuthToken = (UsernamePasswordAuthenticationToken) authResult;

		UserTokenDetail userDetails = (UserTokenDetail) usernameAuthToken.getPrincipal();

		PershingUser loginData = userDetails.getUser();

		String token = jwtTokenTool.generateToken(loginData);

		// 塞cookie
		Cookie cookie = new Cookie("JWT", token);
		cookie.setPath("/");
		cookie.setMaxAge(6000);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				redirectStrategy.sendRedirect(request, response, "/login/index");
			}
		});

		getSuccessHandler().onAuthenticationSuccess(request, response, authResult);

//		chain.doFilter(request, response);
	}
}
