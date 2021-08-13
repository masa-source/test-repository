package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.model.SiteUser;
import com.example.demo.model.TargetComponent;
import com.example.demo.model.TargetTask;
import com.example.demo.repository.TargetTaskRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TargetTaskService extends TargetComponentService {
	private final TargetTaskRepository taskRepository;
	private final SiteUserService userService;

	public void updateIsDone(Long id, Boolean isDone) {
		TargetTask task = taskRepository.findById(id).get();
		task.setDone(isDone);
		taskRepository.save(task);
	}

	@Override
	public List<TargetTask> searchByNameLike(String searchText) {
		List<TargetTask> taskList = taskRepository.findByNameLike(searchText);
		SiteUser user = userService.getLoginUser();
		taskList = taskList.stream().filter(t -> user.equals(t.getSiteuser())).collect(Collectors.toList());
		return taskList;
	}

	@Override
	public TargetTask tmp() {
		TargetTask task = new TargetTask();
		task.setName("デカメロンタスク");
		return task;
	}

	@Override
	public Optional<TargetTask> findById(Long id) {
		return taskRepository.findById(id);
	}

	@Override
	public TargetTask save(TargetComponent component) {
		TargetTask task = (TargetTask) component;
		task.setDateTime(LocalDateTime.now());
		taskRepository.save(task);
		String activityText = "\"" + task.getName() + "\"を課題として登録しました。";
		//		createActivity(task, activityText);
		return task;
	}

	@Override
	public void deleteById(Long id) {
		TargetTask task = findById(id).get();
		String activityText = "\"" + task.getName() + "\"を削除しました。";
		//		createActivity(task, activityText);
		taskRepository.delete(task);
	}
}
