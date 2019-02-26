package com.pershing.security.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pershing.repository.FakeResository;
import com.pershing.security.model.FakerUser;
import com.pershing.security.model.PershingUser;
import com.pershing.security.model.UserTokenDetail;

/**
 * 查詢db 1.這邊可以做如果找不到拋出exception
 * 
 * @author Jack
 *
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private FakeResository fakeResository;

	@Override
	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {

		FakerUser userinfo = fakeResository.loadById(userid);

		if (userinfo == null) {
			throw new UsernameNotFoundException("User " + userid + " not found.");
		}
		// 此帳號擁有權限
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(userinfo.getRole());

		PershingUser user = new PershingUser();
		user.setId(userinfo.getUser_id());
		user.setJwtCount(0);
		user.setPwd(userinfo.getUser_psd());
		user.setUniqueKey(UUID.randomUUID().toString());

		return new UserTokenDetail(user, grantedAuthorities);
	}

}
