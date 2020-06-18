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
	
	//cron ����
	//seconds(��:0~59) minutes(0~59) hours(0~23) day(1~31)
	//months(1~12) day of week(0~6) year(optional)
	//				  ��   ��   ��  
	@Scheduled(cron="0 32 14 * * *")
	public void checkFiles() throws Exception{
		System.out.println("checkFiles");
		List<String> deleteFileList = boardService.getDeleteFileList();
		
		//for(String filename : deleteFileList) {
		for (int i = 0; i <deleteFileList.size(); i++) {
			String filename = deleteFileList.get(i);
			File file = new File(saveFolder + filename);
			 if(file.exists()) {
				 if(file.delete()) {
					 System.out.println(file.getPath() + "�����Ǿ����ϴ�.");
				 }
			 }else {
					System.out.println(file.getPath() + "������ �������� �ʽ��ϴ�.");
				}
		}
	}
	
	//�����ٷ��� �̿��ؼ� �ֱ������� ����, ����, �ſ� ���α׷� ������ ���� �۾��� �ǽ��Ѵ�.
	//@Scheduled(fixedDelay=1000)//������ ����� Task ���� �ð����κ��� ���ǵ� �ð���ŭ ���� �� Task�� �����Ѵ�.
	//�и������� ������
	public void test() throws Exception{
		System.out.println("test");
	}
}
