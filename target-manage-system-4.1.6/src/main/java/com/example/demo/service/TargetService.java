package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Activity;
import com.example.demo.model.SiteUser;
import com.example.demo.model.Target;
import com.example.demo.model.TargetComponent;
import com.example.demo.model.TargetData;
import com.example.demo.model.TargetTask;
import com.example.demo.repository.TargetRepository;
import com.example.demo.repository.TargetTaskRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TargetService extends TargetComponentService {
	private final TargetRepository targetRepository;
	private final TargetTaskRepository taskRepository;

	private final SiteUserService userService;
	private final ActivityService activityService;
	private final TargetDataService dataService;

	public List<Target> getChildTargets(Target target) {
		SiteUser user = userService.getLoginUser();
		List<Target> targetList = targetRepository.findBySiteuserAndParentId(user, target.getId());
		return targetList;
	}

	public Target getTopTarget() {
		SiteUser user = userService.getLoginUser();
		Target target = targetRepository.findBySiteuserAndIsTop(user, true);
		return target;
	}

	@Override
	public Target tmp(SiteUser user) {
		Target target = new Target();
		target.setName("デカメロンミッション");
		target.setSiteuser(user);
		return target;
	}

	@Override
	public Optional<Target> findById(Long id) {
		return targetRepository.findById(id);
	}

	@Override
	public Target save(TargetComponent component) {
		Target target = (Target) component;
		LocalDateTime nowDateTime = LocalDateTime.now();
		target.setDateTime(nowDateTime);
		targetRepository.save(target);
		String activityText = "\"" + target.getName() + "\"を目標として登録しました。";
		//		createActivity(target, activityText);
		return target;
	}

	@Override
	public void deleteById(Long id) {
		Target target = findById(id).get();
		Target topTarget = getTopTarget();
		String activityText = "\"" + target.getName() + "\"を削除しました。";
		//		createActivity(target, activityText);
		List<Target> targetList = getChildTargets(target);
		targetList.forEach(t -> {
			t.setParentId(topTarget.getId());
			save(t);
		});

		List<TargetTask> taskList = target.getTask();
		taskList.forEach(t -> {
			t.setTarget(topTarget);
			taskRepository.save(t);
		});

		List<TargetData> dataList = target.getData();
		dataList.forEach(d -> {
			d.setTarget(topTarget);
			dataService.save(d);
		});

		List<Activity> activities = activityService.findByTarget(target);
		activities.forEach(a -> activityService.delete(a));

		targetRepository.deleteById(id);
	}

	@Override
	public List<Target> searchByNameLike(String searchText) {
		SiteUser user = userService.getLoginUser();
		return targetRepository.findBySiteuserAndIsTopAndNameLike(user, false, searchText);
	}

	//	public Activity createActivity(TargetComponent component, String value) {
	//		// Targetにキャスト
	//		Target target = (Target) component;
	//		// Activityを作成
	//		Activity activity = activityService.tmp();
	//		activity.setParentId(target.getId());
	//		activity.setValue(value);
	//		activityService.save(activity);
	//		// Targetに登録
	//		ArrayList<Target> targetList = getRouteTargetList(target);
	//		targetList.add(target);
	//		targetList.forEach(t -> {
	//			t.addActivity(activity);
	//			activity.addTarget(t);
	//			activityService.save(activity);
	//
	//		});
	//		return activity;
	//	}
}
