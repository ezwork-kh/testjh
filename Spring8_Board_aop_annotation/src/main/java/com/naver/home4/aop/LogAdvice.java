package com.naver.home4.aop;

import org.springframework.stereotype.Component;

//�������� ó���� ������ LogAdvice Ŭ������ beforeLog()�޼���� �����Ѵ�.
@Component
public class LogAdvice {
	
	//LogAdvice Ŭ������ ���� �޼��带 aop���� Advice��� �Ѵ�.
	public void beforeLog() {
		System.out.println("========>LogAdvice : ����Ͻ� ���� ���� �� �����Դϴ�.");
	}
}
