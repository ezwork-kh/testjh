package com.naver.home4.aop;

import org.aspectj.lang.JoinPoint;

/* ȣ��Ǵ� ����Ͻ� �޼����� ������ JoinPoint �������̽��� �� �� �ִ�.
 * 
 * JoinPoint �������̽��� �޼���
 *  1. Signature getSignature()	: ȣ�� �Ǵ� �޼��忡 ���� ������ ���Ѵ�.
 *  2. Object getTarget() : ȣ���� ����Ͻ� �޼��带 �����ϴ� ����Ͻ� ��ü�� ���Ѵ�.
 *  3. getClass().getName() : Ŭ���� �̸��� ���Ѵ�.
 *  4. proceeding.getSignature().getName() : ȣ��Ǵ� �޼��� �̸��� ���Ѵ�. *  
 */


//�������� ó���� ������ BeforeAdvice Ŭ������ beforeLog()�޼���� �����Ѵ�.
//Advice : Ⱦ�ܰ��ɿ� �ش��ϴ� ���� ����� �ǹ��ϸ� ������ Ŭ������ �޼���� �ۼ��ȴ�.
public class BeforeAdvice {
	public void beforeLog(JoinPoint proceeding) {
		
		
		System.out.println("=========================================");
		System.out.println("[BeforeAdvice] : ����Ͻ� ���� ���� �� ����");
		System.out.println("[BeforeAdvice] : "
				+ proceeding.getTarget().getClass().getName()
				+ "�� "+proceeding.getSignature().getName() + " ȣ���Ѵ�.");
		System.out.println("=========================================");		
	}
}
