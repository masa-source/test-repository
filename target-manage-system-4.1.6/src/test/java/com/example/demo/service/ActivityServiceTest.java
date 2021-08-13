package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.Activity;
import com.example.demo.model.SiteUser;
import com.example.demo.model.Target;
import com.example.demo.repository.SiteUserRepository;
import com.example.demo.repository.TargetRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@Slf4j
public class ActivityServiceTest {

	@Autowired
	SiteUserRepository siteUserRepository;

	@Autowired
	TargetRepository targetRepository;

	@Autowired
	TargetService targetService;

	@Autowired
	SiteUserService siteUserService;

	@Autowired
	ActivityService activityService;

	@Test
	@DisplayName("基本動作のテスト")
	void Test() {
		SiteUser user = siteUserService.tmpSiteUser();
		siteUserRepository.save(user);
		Target target = targetService.tmp(user);
		Activity activity = activityService.tmp();
		Activity savedActivity = (Activity) activityService.save(activity);
		target.addActivity(activity);
		targetRepository.save(target);
		log.info("activity.id:" + activity.getId());
		log.info("savedActivity.id:" + savedActivity.getId());
		assertEquals(activity, savedActivity);
	}

}
