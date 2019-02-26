package com.pershing.security.model;

/**
 * 交易用userData
 * @author Jack
 *
 */
public class PershingUser {

	/** 唯一識別key */
	private String uniqueKey;
	
	private String id;
	
	private String pwd;
	
	/** 用來驗證一個JWT只能使用一次 */
	private int jwtCount = 0;

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getJwtCount() {
		return jwtCount;
	}

	public void setJwtCount(int jwtCount) {
		this.jwtCount = jwtCount;
	}
	
	
	
}
