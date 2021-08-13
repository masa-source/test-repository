package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Activity;
import com.example.demo.model.Target;
import com.example.demo.model.TargetComponent;
import com.example.demo.repository.ActivityRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ActivityService extends TargetComponentService {
	private final ActivityRepository activityRepository;

	public void delete(Activity activity) {
		activityRepository.delete(activity);
	}

	public List<Activity> findByTarget(Target target) {
		return activityRepository.findByTargetList(target);
	}

	@Override
	public Activity tmp() {
		Activity activity = new Activity();
		return activity;
	}

	@Override
	public Optional<Activity> findById(Long id) {
		return activityRepository.findById(id);
	}

	@Override
	public Activity save(TargetComponent component) {
		Activity activity = (Activity) component;
		activityRepository.save(activity);
		return activity;
	}

	@Override
	public void deleteById(Long id) {
		Activity activity = findById(id).get();
		activityRepository.delete(activity);
	}

}
