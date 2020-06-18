package com.naver.home4.common;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

@Service
@Aspect	
public class AfterReturningAdvice {
	@Pointcut("execution(* com.naver.home4..*Impl.get*(..))")
	public void getPointcut() {}
	
	@AfterReturning(pointcut="getPointcut()", returning="obj")
	public void afterReturningLog(Object obj) {
		System.out.println("===========================5===========================");
		System.out.println("[AfterReturningAdvice ] : ����Ͻ� ���� ���� �� �����Դϴ�.");
		System.out.println("[AfterReturningAdvice ] obj : " + obj.toString());
		System.out.println("===========================5===========================");
	}
}
