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
	 * @Around : 비즈니스 메서드 실행 전, 후에 동작합니다.
	 * @Around("getPointcut()")	: getPointcut() 참조 메서드로 지정한 메서드가 실행 전과 후에
	 * 							  Advice의 메서드 aroundLog()가 호출 됩니다.
	 */
	@Around("getPointcut()")
	public Object aroundLog(ProceedingJoinPoint proceeding) throws Throwable {
		System.out.println("======================================================");
		System.out.println("[Around Advice의 before] : 비즈니스 메서드 수행 전입니다.");
		StopWatch sw = new StopWatch();
		sw.start();
		
		//이 코드의 이전과 이후에 공통 기능을 위한 코드를 위치 시키면 됩니다.
		//대상 객체의 메서드 BoardServiceImpl.getListCount([]) 를 호출합니다.
		Object result = proceeding.proceed();	//반환값이 뭐가 올지 모르기 때문에 Object로 받는다.
		sw.stop();
		
		System.out.println("[Around Advice의 after]: 비즈니스 메서드 수행 후 입니다.");
		Signature sig = proceeding.getSignature();
		
		//Object[] getArge() : 클라이언트가 메서드를 호출할 때 넘겨준 인자 목록을 Object 배열로 리턴합니다.
		System.out.printf("[Around Advice의 after]: %s.%s(%s) \n",
				proceeding.getTarget().getClass().getSimpleName(),sig.getName(),Arrays.deepToString(proceeding.getArgs()));
		
		System.out.println("[Around Advice의 after]: " + proceeding.getSignature().getName() + "() 메소드 수행 시간 :"
						+ sw.getTotalTimeMillis() + "(ms)초");
		
		System.out.println("[Around Advice의 after]: " + proceeding.getTarget().getClass().getName()); // com.json.jsonroot.dao.ServiceImpl
		
		System.out.println("proceeding.proceed() 실행 후 반환값 :" + result);
		System.out.println("======================================================");	
		return result;
	}
}
