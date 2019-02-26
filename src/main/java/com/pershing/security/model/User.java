package com.pershing.security.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class User extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	public User(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

	}

	private String userid;

	private String pass;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
}
