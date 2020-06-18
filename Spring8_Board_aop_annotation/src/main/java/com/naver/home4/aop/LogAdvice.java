package com.naver.home4.aop;

import org.springframework.stereotype.Component;

//공통으로 처리할 로직을 LogAdvice 클래스에 beforeLog()메서드로 구현한다.
@Component
public class LogAdvice {
	
	//LogAdvice 클래스의 공통 메서드를 aop에서 Advice라고 한다.
	public void beforeLog() {
		System.out.println("========>LogAdvice : 비즈니스 로직 수행 전 동작입니다.");
	}
}
