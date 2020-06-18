package com.naver.home4.task;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.naver.home4.service.BoardService;

@Service
public class FileCheckTask {
	
	@Value("${savefoldername}")
	private String saveFolder;
	
	@Autowired
	private BoardService boardService;
	
	@Scheduled(cron="0 50 12 * * *")
	public void checkFiles() throws Exception{
		
		System.out.println("checkFiles");
		List<String> deleteFileList = boardService.getDeleteFileList();
		
		for (int i = 0; i < deleteFileList.size(); i++) {
			String filename = deleteFileList.get(i);
			File file = new File(saveFolder + filename);
			if(file.exists()) {
				if(file.delete()) {
					System.out.println(file.getPath()+" 삭제됨");					
				}
			}else {
				System.out.println(file.getPath() + " 파일이 존재하지 않음");
			}
		}
	}
	
	//스케줄러를 이용해서 주기적으로 매일, 매주, 매월 프로그램 실행을 위한 작업을 실시한다.
	@Scheduled(fixedDelay=1000)//이전에 실행된 Task 종료 시간으로부터 정의된 시간만큼 지난 후 Task를 실행한다.
	//밀리세컨드 단위임
	public void test() throws Exception{
		//System.out.println("test");
	}
}
