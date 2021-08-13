package com.example.demo.service;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.repository.SiteUserRepository;
import com.example.demo.repository.TargetTaskRepository;

@SpringBootTest
@Transactional
class TargetTaskServiceTest {

	@Autowired
	SiteUserRepository siteUserRepository;

	@Autowired
	TargetTaskRepository taskRepository;

	@Autowired
	TargetTaskService taskService;

	@Autowired
	SiteUserService siteUserService;

	@Test
	@DisplayName("existUserTaskのテスト")
	void whenTaskExist_returnTrue() {
		//		//		Arrange
		//		SiteUser user = siteUserService.tmpSiteUser();
		//		siteUserRepository.save(user);
		//		SiteUser savedUser = siteUserRepository.findByUsername("瓜田");
		//
		//		TargetTask task = taskService.tmp(savedUser);
		//		taskRepository.save(task);
		//		TargetTask savedTask = taskRepository.findByName("デカメロンタスク");
		//
		//		List<TargetTask> actualList = taskRepository.findBySiteuser(savedUser);
		//		TargetTask actualTask = taskRepository.findById(savedTask.getId()).get();
		//
		//		//		Act
		//		boolean result1 = actualList.contains(actualTask);
		//		boolean result2 = !taskService.isEditable(savedUser, savedTask.getId());
		//
		//		//		Assert
		//		assertTrue(result1);
		//		assertFalse(result2);
	}

	@Test
	@DisplayName("taskRepositoryの動作確認")
	void taskRopositoryTest() {
		String name = taskRepository.findById((long) 85).get().getName();
		System.out.println(name);
	}

	@Test
	@DisplayName("done関係のテスト")
	void doneTest() {
		//		SiteUser user = siteUserService.tmpSiteUser();
		//		TargetTask task = taskService.tmp();

	}
}
