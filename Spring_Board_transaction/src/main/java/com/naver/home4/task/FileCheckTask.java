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
					System.out.println(file.getPath()+" ������");					
				}
			}else {
				System.out.println(file.getPath() + " ������ �������� ����");
			}
		}
	}
	
	//�����ٷ��� �̿��ؼ� �ֱ������� ����, ����, �ſ� ���α׷� ������ ���� �۾��� �ǽ��Ѵ�.
	@Scheduled(fixedDelay=1000)//������ ����� Task ���� �ð����κ��� ���ǵ� �ð���ŭ ���� �� Task�� �����Ѵ�.
	//�и������� ������
	public void test() throws Exception{
		//System.out.println("test");
	}
}
