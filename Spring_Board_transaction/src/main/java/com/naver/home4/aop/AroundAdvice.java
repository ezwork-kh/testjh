package com.naver.home4.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.util.StopWatch;

/*
 *	ProceedingJoinPoint �������̽��� JoinPoint�� ����߱� ������
 	JoinPoint�� ���� ��� �޼��带 �����Ѵ�.
 *	Around Advice������ ProceedingJoinPoint�� �Ű������� ����ϴµ�
 	�� ������ proceed()�޼��尡 �ʿ��ϱ� �����̴�.
 *	Around Advice�� ��� ����Ͻ� �޼��� ���� ���� �Ŀ� ���� �Ǵµ�
 	����Ͻ� �޼��带 ȣ���ϱ� ���ؼ���
 	ProceedingJoinPoint�� proceed()�޼��尡 �ʿ��ϴ�.
 *	��, Ŭ���̾�Ʈ�� ��û�� ����æ �����̽���
 	Ŭ���̾�Ʈ�� ȣ���� ����Ͻ� �޼���(ServiceImpl�� get���� �����ϴ� �޼���)��
 	ȣ���ϱ� ����
 	ProceedingJoinPoint ��ü�� �Ű� ������ �޾� proceed()�޼��带 ���ؼ�
 	����Ͻ� �޼��带 ȣ���� �� �ִ�.
 *	proceed()�޼��� ���� �� �޼����� ��ȯ���� �����ؾ� �Ѵ�.	
 	 
 * */
public class AroundAdvice {
	public Object aroundLog(ProceedingJoinPoint proceeding) throws Throwable{
		System.out.println("=========================================");
		System.out.println("[Around Advice�� before] : ����Ͻ� �޼��� ���� ���� �� ����");
		StopWatch sw= new StopWatch();
		sw.start();
		
		//�� �ڵ��� ������ ���Ŀ� ���� ����� ���� �ڵ带 ��ġ ��Ű�� �ȴ�.
		//��� ��ü�� �޼��� BoardServiceImpl.getListCount([]) �� ȣ���Ѵ�.
		Object result = proceeding.proceed();
		sw.stop();
		
		System.out.println("[Around Advice�� after] : ����Ͻ� �޼��� ���� �� ����");
		
		Signature sig = proceeding.getSignature();
		
		//Object[] getArgs() : Ŭ���̾�Ʈ�� �޼��带 ȣ���� �� �Ѱ��� ���� �����
		//Object �迭�� �������մϴ�.
		System.out.printf("[Around advice�� after] : %s.%s(%s) \n",
					proceeding.getTarget().getClass().getSimpleName(),
					sig.getName(),
					Arrays.toString(proceeding.getArgs()));
		
		System.out.println("[Around advice�� after] : "
					+ proceeding.getSignature().getName()+"() �޼ҵ� ���� �ð� : "
					+ sw.getTotalTimeMillis()+"(ms)��"
				);
		System.out.println("[Around advice�� after] : "
					+ proceeding.getTarget().getClass().getName()
					//com.json.jsonroot.dao.ServiceImpl
				);
		
		System.out.println("proceeding.proceed() ������ ��ȯ�� : "+result);
		System.out.println("=========================================");
		return result;
	}
}
