/* 호출되는 비즈니스 메서드의 정보를 JoinPoint 인터페이스로 알 수 있습니다
 * 
 * JoinPoint 인터페이스의 메서드
 	1. Signature getSignature() : 호출되는 메서드에 대한 정보를 구합니다.
 	2. Object getTarget() : 호출된 비즈니스 메서드를 포함하는 비즈니스 객체를 구합니다.
 	3. getClass().getName() : 클래스 이름을 구합니다.
 	4. proceeding.getSignature().getName() : 호출되는 메서드 이름을 구합니다.
 */

//공통으로 처리할 로직을 BeforeAdvice 클래스에 beforeLog()메서드로 구현합니다.
//Advice : 횡단 관심에 해당하는 공통 기능을 의미하여 독립된 클래스의 메서드로 작성됩니다.
package com.naver.home4.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

//@Service
//@Aspect	//@Aspect가 설정된 클래스에는 Pointcut과 Advice를 결합하는 설정이 있어야 합니다.
public class BeforeAdvice {
	
	@Pointcut("execution(* com.naver.home4..*Impl.get*(..))")
	public void getPointcut() {}
	
	/* 
	 * @Before : 비즈니스 메서드 실행 전에 동작합니다. 
	 * @Before("getPointcut()") : getPointcut() 참조 메서드로 지정한 메서드가 실행 후에 Advice의 메서드 beforeLog()가 먼저 호출됩니다.
	 */
	@Before("getPointcut()")
	public void beforeLog(JoinPoint proceeding) {
		System.out.println("======================================================");
		System.out.println("[BeforeAdvice] : 비즈니스 로직 수행 전 동작입니다.");
		System.out.println("[BeforeAdvice] : "
				+ proceeding.getTarget().getClass().getName()+"의 "+proceeding.getSignature().getName()+ " 호출 전 입니다.");
		System.out.println("======================================================");	
	}

}
