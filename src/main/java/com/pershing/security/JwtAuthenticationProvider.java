package com.pershing.security;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.pershing.security.model.PershingUser;
import com.pershing.security.model.UserTokenDetail;
import com.pershing.security.util.JwtToolUtil;

/**
 * jwt檢核Provider 1.將Authentication中存的jwt token取出做檢查 2.將資料封裝到UserDetail中
 * 
 * @author Jack
 *
 */
@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {


	@Autowired
	private JwtToolUtil jwtTokenTool;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

		JwtAuthenticationToken jtwAuthToken = (JwtAuthenticationToken) authentication;

		PershingUser user = null;

		String jwtToken = jtwAuthToken.getJwtToken();

		Map<String, Object> jwtResultMap = jwtTokenTool.validateToken(jwtToken);

		user = (PershingUser) jwtResultMap.get("User");

		if (user == null) {
			throw new DisabledException("JWT驗證失敗");
//			throw new InternalAuthenticationServiceException("aaaaa");
		}

		String role = "USER";

		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role);

		return new UserTokenDetail(user, grantedAuthorities);
	}

}
