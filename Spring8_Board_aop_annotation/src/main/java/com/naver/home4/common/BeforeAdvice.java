/* ȣ��Ǵ� ����Ͻ� �޼����� ������ JoinPoint �������̽��� �� �� �ֽ��ϴ�
 * 
 * JoinPoint �������̽��� �޼���
 	1. Signature getSignature() : ȣ��Ǵ� �޼��忡 ���� ������ ���մϴ�.
 	2. Object getTarget() : ȣ��� ����Ͻ� �޼��带 �����ϴ� ����Ͻ� ��ü�� ���մϴ�.
 	3. getClass().getName() : Ŭ���� �̸��� ���մϴ�.
 	4. proceeding.getSignature().getName() : ȣ��Ǵ� �޼��� �̸��� ���մϴ�.
 */

//�������� ó���� ������ BeforeAdvice Ŭ������ beforeLog()�޼���� �����մϴ�.
//Advice : Ⱦ�� ���ɿ� �ش��ϴ� ���� ����� �ǹ��Ͽ� ������ Ŭ������ �޼���� �ۼ��˴ϴ�.
package com.naver.home4.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

//@Service
//@Aspect	//@Aspect�� ������ Ŭ�������� Pointcut�� Advice�� �����ϴ� ������ �־�� �մϴ�.
public class BeforeAdvice {
	
	@Pointcut("execution(* com.naver.home4..*Impl.get*(..))")
	public void getPointcut() {}
	
	/* 
	 * @Before : ����Ͻ� �޼��� ���� ���� �����մϴ�. 
	 * @Before("getPointcut()") : getPointcut() ���� �޼���� ������ �޼��尡 ���� �Ŀ� Advice�� �޼��� beforeLog()�� ���� ȣ��˴ϴ�.
	 */
	@Before("getPointcut()")
	public void beforeLog(JoinPoint proceeding) {
		System.out.println("======================================================");
		System.out.println("[BeforeAdvice] : ����Ͻ� ���� ���� �� �����Դϴ�.");
		System.out.println("[BeforeAdvice] : "
				+ proceeding.getTarget().getClass().getName()+"�� "+proceeding.getSignature().getName()+ " ȣ�� �� �Դϴ�.");
		System.out.println("======================================================");	
	}

}
