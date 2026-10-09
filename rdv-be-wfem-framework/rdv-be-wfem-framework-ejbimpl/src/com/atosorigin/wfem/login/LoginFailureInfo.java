package com.atosorigin.wfem.login;

public class LoginFailureInfo {
	
	private int code = 0;
	private String page;
	private String title;
	private String reason;
	
	public String getPage() {
		return page;
	}

	public String getReason() {
		return reason;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
