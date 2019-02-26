package com.pershing.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pershing.properties.LoginProperties;
import com.pershing.security.model.FakerUser;

@Component
public class FakeResository {

	@Autowired
	private LoginProperties loginProperties;

	public FakerUser loadById(String id) {

		if (loginProperties.getAccount().equals(id)) {
			FakerUser user = new FakerUser();
			user.setUser_id(id);
			user.setUser_psd(loginProperties.getPwd());
			return user;
		}

		return null;

	}
}
