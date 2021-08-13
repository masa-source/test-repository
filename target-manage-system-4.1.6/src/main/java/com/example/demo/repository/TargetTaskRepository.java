package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.TargetTask;

public interface TargetTaskRepository extends JpaRepository<TargetTask, Long> {
	List<TargetTask> findByNameLike(String name);
}