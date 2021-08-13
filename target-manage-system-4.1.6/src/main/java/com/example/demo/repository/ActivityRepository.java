package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Activity;
import com.example.demo.model.Target;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
	List<Activity> findByTargetList(Target target);
}
