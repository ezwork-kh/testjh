package com.naver.home4.domain;

public class MailVO {
	private String from="34733473@naver.com";
	private String to;
	private String subject = "ȸ�������� �����մϴ�. - ����";
	private String content = "ȸ�������� �����մϴ�. - ����";
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
