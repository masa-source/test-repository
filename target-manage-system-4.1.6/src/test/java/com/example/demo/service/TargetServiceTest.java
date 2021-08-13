package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.Activity;
import com.example.demo.model.SiteUser;
import com.example.demo.model.Target;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.SiteUserRepository;
import com.example.demo.repository.TargetRepository;

@SpringBootTest
@Transactional
class TargetServiceTest {

	@Autowired
	SiteUserRepository siteUserRepository;

	@Autowired
	TargetRepository targetRepository;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	TargetService targetService;

	@Autowired
	SiteUserService siteUserService;

	@Test
	@DisplayName("existUserTargetのテスト")
	void whenTargetExist_returnTrue() {
		//		Arrange
		SiteUser user = siteUserService.tmpSiteUser();
		siteUserRepository.save(user);
		Target target = targetService.tmp(user);
		targetRepository.save(target);
		List<Target> actualList = targetRepository.findBySiteuser(user);
		Target actualTarget = targetRepository.findById(target.getId()).get();
		//		Act
		boolean result1 = actualList.contains(actualTarget);
		//		Assert
		assertTrue(result1);
	}

	@Test
	@DisplayName("createActivityのテスト")
	void createActivityTest() {
		SiteUser user = siteUserService.tmpSiteUser();
		siteUserRepository.save(user);
		ArrayList<Target> targets = new ArrayList<Target>();
		ArrayList<Activity> activities = new ArrayList<Activity>();

		Target parentTarget = null;
		for (int i = 1; i < 4; i++) {
			Target target = targetService.tmp(user);
			target.setName("target" + i);
			if (i == 1) {
				target.setTop(true);
			} else {
				target.setParentId(parentTarget.getId());
			}
			targetRepository.save(target);
			targets.add(target);
			parentTarget = target;
		}

		targets.forEach(t -> {
			//			Activity a = targetService.createActivity(t, t.getName() + "でactivityを作成しました。");
			//			activities.add(a);
		});

		targets.forEach(t -> {
			System.out.println(t.getName() + ".activity");
			t.getActivity().stream().map(a -> a.getValue()).forEach(System.out::println);
		});

		activities.forEach(a -> {
			System.out.println(
					a.getValue() + ":parentTarget->" + targetRepository.findById(a.getParentId()).get().getName());
			//			a.getTarget().stream().map(t -> t.getName()).forEach(System.out::println);
			;
		});

		List<Activity> savedActivities = activities.stream().map(a -> activityRepository.findById(a.getId()).get())
				.collect(Collectors.toList());
		for (int i = 0; i < activities.size(); i++) {
			assertTrue(activities.get(i) == savedActivities.get(i));
		}
	}
}
