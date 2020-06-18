package com.naver.home4.common;

//JoinPoint �������̽��� �޼���
//Signature getSignature() : ȣ��Ǵ� �޼��忡 ���� ������ ���Ѵ�.
//Object getTarget() : Ŭ���̾�Ʈ�� ȣ���� ����Ͻ� �޼��带 �����ϴ� ����Ͻ� ��ü�� ���Ѵ�.
//Object[] getArgs() : Ŭ���̾�Ʈ�� �޼��带 ȣ���� �� �Ѱ��� ���� ����� Object �迭�� �����Ѵ�.

//Advice : Ⱦ�� ���ɿ� �ش��ϴ� ���� ����� �ǹ��ϸ� ������ Ŭ������ �޼���� �ۼ��ȴ�.
//AfterThrowing (���� �߻����� �� ����)
//Ÿ�� �޼ҵ尡 ���� �� ���ܸ� ������ �Ǹ� �����̽� ����� ����
//BoardServiceImpl.java���� getBoardList()�ȿ� double i = 1/0; �߰��Ѵ�.
public class AfterThrowingAdvice {
	
	public void afterThrowingLog(Throwable exp) {
		System.out.println("======================================================");
		System.out.println("[AroundThrowingAdvice ] : ����Ͻ� ���� ���� �� ������ �߻��ϸ� �����մϴ�.");
		System.out.println("ex : " + exp.toString());
		System.out.println("======================================================");
	}
}
