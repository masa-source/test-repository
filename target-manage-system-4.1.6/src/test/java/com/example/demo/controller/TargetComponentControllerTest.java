package com.example.demo.controller;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.Notice;
import com.example.demo.model.SiteUser;
import com.example.demo.repository.NoticeRepository;
import com.example.demo.repository.SiteUserRepository;
import com.example.demo.repository.TargetRepository;
import com.example.demo.repository.TargetTaskRepository;
import com.example.demo.service.NoticeService;
import com.example.demo.service.SiteUserService;
import com.example.demo.service.TargetDataService;
import com.example.demo.service.TargetService;
import com.example.demo.service.TargetTaskService;

@SpringBootTest
@Transactional
class TargetComponentControllerTest {
	@Autowired
	SiteUserService siteUserService;

	@Autowired
	SiteUserRepository siteUserRepository;

	@Autowired
	TargetService targetService;

	@Autowired
	TargetRepository targetRepository;

	@Autowired
	TargetTaskService taskService;

	@Autowired
	TargetTaskRepository taskRepository;

	@Autowired
	TargetDataService dataService;

	@Autowired
	NoticeRepository noticeRepository;

	@Autowired
	NoticeService noticeService;

	@Test
	@DisplayName("printのテスト")
	void printTest() {
		//		System.out.println("**********test***********");
		//		//		Arrange
		//		SiteUser siteUser = siteUserService.tmpSiteUser();
		//		siteUserRepository.save(siteUser);
		//
		//		Target topTarget = targetService.tmp(siteUser);
		//		targetRepository.save(topTarget);
		//
		//		TargetTask taskA = taskService.tmp();
		//		taskRepository.save(taskA);
		//
		//		Target targetA = targetService.tmp(siteUser);
		//		targetA.setName("ターゲットA");
		//		Target targetB = targetService.tmp(siteUser);
		//		targetB.setName("ターゲットB");
		//		Target targetC = targetService.tmp(siteUser);
		//		targetC.setName("ターゲットＣ");
		//		targetRepository.save(targetA);
		//		targetRepository.save(targetB);
		//		targetRepository.save(targetC);
		//
		//		topTarget.print();
		//
		//		System.out.println("**********test2***********");
		//		Iterator<TargetComponent> iterator = topTarget.createIterator();
		//		while (iterator.hasNext()) {
		//			TargetComponent targetComponent = iterator.next();
		//			if (targetComponent.getName() == "デカメロンタスク") {
		//				targetComponent.print();
		//			}
		//		}
		//
		//		Target parenTarget = targetService.searchParentTarget(topTarget);
		//		System.out.println(parenTarget.getName());
	}

	@Test
	@DisplayName("componentの処理のテスト")
	void componentTest() {
		SiteUser user = siteUserService.tmpSiteUser();
		Notice notice = noticeService.createNotice(user, "notice1");
		Notice notice2 = noticeService.createNotice(user, "notice2");
		System.out.println(user.getNotices());
		System.out.println(user.getId());
		System.out.println(notice.getId());
		System.out.println(noticeRepository.findById(notice.getId()));
		System.out.println(noticeRepository.findAll());
	}
}
