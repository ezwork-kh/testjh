package com.naver.home4.aop;

import org.aspectj.lang.JoinPoint;

/* 호출되는 비즈니스 메서드의 정보를 JoinPoint 인터페이스로 알 수 있다.
 * 
 * JoinPoint 인터페이스의 메서드
 *  1. Signature getSignature()	: 호출 되는 메서드에 대한 정보를 구한다.
 *  2. Object getTarget() : 호출한 비즈니스 메서드를 포함하는 비즈니스 객체를 구한다.
 *  3. getClass().getName() : 클래스 이름을 구한다.
 *  4. proceeding.getSignature().getName() : 호출되는 메서드 이름을 구한다. *  
 */


//공통으로 처리할 로직을 BeforeAdvice 클래스에 beforeLog()메서드로 구현한다.
//Advice : 횡단관심에 해당하는 공통 기능을 의미하며 독립된 클래스의 메서드로 작성된다.
public class BeforeAdvice {
	public void beforeLog(JoinPoint proceeding) {
		
		
		System.out.println("=========================================");
		System.out.println("[BeforeAdvice] : 비즈니스 로직 수행 전 동작");
		System.out.println("[BeforeAdvice] : "
				+ proceeding.getTarget().getClass().getName()
				+ "의 "+proceeding.getSignature().getName() + " 호출한다.");
		System.out.println("=========================================");		
	}
}
