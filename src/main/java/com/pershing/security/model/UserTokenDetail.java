package com.pershing.security.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class UserTokenDetail implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String userName;

	private String userPwd;

	private PershingUser user;

	private Collection<? extends GrantedAuthority> authorities;

	public UserTokenDetail(PershingUser user, List<GrantedAuthority> grantedAuthorities) {

		this.authorities = grantedAuthorities;
		this.user = user;
		System.out.println("****************");
		System.out.println(user.getId());
		System.out.println(user.getPwd());
		System.out.println("****************");
		this.userName = user.getId();
		this.userPwd = user.getPwd();

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return userPwd;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public PershingUser getUser() {
		return user;
	}

	public void setUser(PershingUser user) {
		this.user = user;
	}

}
