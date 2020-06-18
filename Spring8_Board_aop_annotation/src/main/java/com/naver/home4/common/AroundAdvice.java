package com.naver.home4.common;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

/*@Service
@Aspect	*/
public class AroundAdvice {
	
	@Pointcut("execution(* com.naver.home4..*Impl.get*(..))")
	public void getPointcut() {}
	
	/*
	 * @Around : ����Ͻ� �޼��� ���� ��, �Ŀ� �����մϴ�.
	 * @Around("getPointcut()")	: getPointcut() ���� �޼���� ������ �޼��尡 ���� ���� �Ŀ�
	 * 							  Advice�� �޼��� aroundLog()�� ȣ�� �˴ϴ�.
	 */
	@Around("getPointcut()")
	public Object aroundLog(ProceedingJoinPoint proceeding) throws Throwable {
		System.out.println("======================================================");
		System.out.println("[Around Advice�� before] : ����Ͻ� �޼��� ���� ���Դϴ�.");
		StopWatch sw = new StopWatch();
		sw.start();
		
		//�� �ڵ��� ������ ���Ŀ� ���� ����� ���� �ڵ带 ��ġ ��Ű�� �˴ϴ�.
		//��� ��ü�� �޼��� BoardServiceImpl.getListCount([]) �� ȣ���մϴ�.
		Object result = proceeding.proceed();	//��ȯ���� ���� ���� �𸣱� ������ Object�� �޴´�.
		sw.stop();
		
		System.out.println("[Around Advice�� after]: ����Ͻ� �޼��� ���� �� �Դϴ�.");
		Signature sig = proceeding.getSignature();
		
		//Object[] getArge() : Ŭ���̾�Ʈ�� �޼��带 ȣ���� �� �Ѱ��� ���� ����� Object �迭�� �����մϴ�.
		System.out.printf("[Around Advice�� after]: %s.%s(%s) \n",
				proceeding.getTarget().getClass().getSimpleName(),sig.getName(),Arrays.deepToString(proceeding.getArgs()));
		
		System.out.println("[Around Advice�� after]: " + proceeding.getSignature().getName() + "() �޼ҵ� ���� �ð� :"
						+ sw.getTotalTimeMillis() + "(ms)��");
		
		System.out.println("[Around Advice�� after]: " + proceeding.getTarget().getClass().getName()); // com.json.jsonroot.dao.ServiceImpl
		
		System.out.println("proceeding.proceed() ���� �� ��ȯ�� :" + result);
		System.out.println("======================================================");	
		return result;
	}
}
