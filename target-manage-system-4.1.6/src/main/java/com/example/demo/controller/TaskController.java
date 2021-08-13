package com.example.demo.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.TargetTaskService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class TaskController {
	private final TargetTaskService taskService;

	@PutMapping("/save/task")
	public void saveTask(@RequestParam Long id, @RequestParam(required = false) Boolean isDone) {
		if (isDone != null) {
			taskService.updateIsDone(id, isDone);
		}
	}
}
