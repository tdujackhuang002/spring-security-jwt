package com.pershing.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.pershing.security.model.UserTokenDetail;
import com.pershing.security.service.JwtUserDetailsService;

/**
 * 登入檢查身分 1.登入時先使用帳號去查詢 2.將查出的結果封裝在AuthenticationToken
 * 
 * @author Jack
 *
 */
@Component
public class JwtUseFormAuthentiactionProvider implements AuthenticationProvider {

	private final Logger logger = LoggerFactory.getLogger(JwtUseFormAuthentiactionProvider.class);

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		logger.info(">>> execute JwtUseFormAuthentiactionProvider.authenticate...");

		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

		UserTokenDetail userDetails = (UserTokenDetail) userDetailsService.loadUserByUsername(token.getName());

		if (userDetails.getPassword().equals(token.getCredentials())) {
			return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
					userDetails.getAuthorities());
		} else {
			throw new DisabledException("密碼錯誤");
		}
	
	}

	@Override
	public boolean supports(Class<?> authentication) {

		System.out.println("isSupports : " + UsernamePasswordAuthenticationToken.class.equals(authentication));
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
