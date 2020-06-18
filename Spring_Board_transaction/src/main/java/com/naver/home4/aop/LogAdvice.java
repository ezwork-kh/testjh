package com.naver.home4.aop;

import org.springframework.stereotype.Component;

@Component
public class LogAdvice {
	
	public void beforeLog() {
		System.out.println("===> LogAdvice : 비즈니스 로직 수행 전 동작");
	}
}
