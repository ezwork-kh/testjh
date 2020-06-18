package com.naver.home4.common;

//JoinPoint 인터페이스의 메서드
//Signature getSignature() : 호출되는 메서드에 대한 정보를 구한다.
//Object getTarget() : 클라이언트가 호출한 비즈니스 메서드를 포함하는 비즈니스 객체를 구한다.
//Object[] getArgs() : 클라이언트가 메서드를 호출할 때 넘겨준 인자 목록을 Object 배열로 리턴한다.

//Advice : 횡단 관심에 해당하는 공통 기능을 의미하며 독립된 클래스의 메서드로 작성된다.
//AfterThrowing (예외 발생했을 때 실행)
//타겟 메소드가 수행 중 예외를 던지게 되면 어드바이스 기능을 수행
//BoardServiceImpl.java에서 getBoardList()안에 double i = 1/0; 추가한다.
public class AfterThrowingAdvice {
	
	public void afterThrowingLog(Throwable exp) {
		System.out.println("======================================================");
		System.out.println("[AroundThrowingAdvice ] : 비즈니스 로직 수행 중 오류가 발새하면 동작합니다.");
		System.out.println("ex : " + exp.toString());
		System.out.println("======================================================");
	}
}
