package com.naver.home4.aop;

import org.aspectj.lang.JoinPoint;

//JoinPoint �������̽��� �޼���
//Signature getSignature() : ȣ��Ǵ� �޼��忡 ���� ������ ���մϴ�.
//Object getTarget() : Ŭ���̾�Ʈ�� ȣ���� ����Ͻ� �޼��带 �����ϴ� ����Ͻ� ��ü�� ���մϴ�.
//Object[] getArgs() : Ŭ���̾�Ʈ�� �޼��带 ȣ���� �� �Ѱ��� ���� ����� Object �迭�� �����մϴ�.

//Advice : Ⱦ�� ���ɿ� �ش��ϴ� ���� ����� �ǹ��ϸ� ������ Ŭ������ �޼���� �ۼ��˴ϴ�.
//AfterReturningAdvice:Ÿ�� �޼ҵ尡 ���������� ������� ��ȯ �Ŀ� �����̽� ����� ����
public class AfterReturningAdvice {
	public void afterRetruningLog(Object obj) {
		System.out.println("=========================================");
		System.out.println("[AfterReturningAdvice] : obj");
		System.out.println("[AfterReturningAdvice] : "
							+ obj.toString());
		System.out.println("=========================================");		
	}
}
