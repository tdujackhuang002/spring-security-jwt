package com.pershing.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * jwt token
 * 
 * @author Jack
 *
 */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private String jwtToken = "";

	public JwtAuthenticationToken(String jwtToken) {
		super(null, null);
		this.jwtToken = jwtToken;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

}
