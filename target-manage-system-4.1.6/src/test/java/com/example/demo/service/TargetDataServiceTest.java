package com.example.demo.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.SiteUser;
import com.example.demo.model.Target;
import com.example.demo.model.TargetData;
import com.example.demo.repository.TargetDataRepository;
import com.example.demo.repository.TargetRepository;

@SpringBootTest
@Transactional
public class TargetDataServiceTest {

	@Autowired
	TargetRepository targetRepository;

	@Autowired
	TargetDataRepository dataRepository;

	@Autowired
	TargetDataService targetDataService;

	@Autowired
	SiteUserService userService;

	@Autowired
	TargetService targetService;

	@Test
	@DisplayName("progressのテスト")
	void progressTest() {
		//		Target target = targetRepository.findById((long) 2).get();
		//		System.out.println(target.getName());
		//
		//		Long progress = targetDataService.getProgress(target);
		//		System.out.println("progress:" + progress);
		//
		//		Long taskProgress = targetDataService.getTaskProgress(target);
		//		System.out.println("taskProgress:" + taskProgress);
		//
		//		Long childProgress = targetDataService.getChildProgress(target);
		//		System.out.println("childProgress:" + childProgress);
	}

	@Test
	@DisplayName("repositoryの動作テスト")
	void repositoryTest() {
		SiteUser user = userService.tmpSiteUser();
		userService.save(user);
		Target target = targetService.tmp(user);
		targetRepository.save(target);
		TargetData data = targetDataService.tmp();
		data.setTarget(target);
		data.setDateTime(LocalDateTime.now().minusYears(1));
		dataRepository.save(data);

		TargetData data2 = targetDataService.tmp();
		data2.setValue(2.0);
		data2.setDateTime(LocalDateTime.now());
		data2.setTarget(target);
		dataRepository.save(data2);

		System.out.println(data.getDateTime());
		System.out.println(data2.getDateTime());

	}
}
