package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Target;
import com.example.demo.model.TargetData;

public interface TargetDataRepository extends JpaRepository<TargetData, Long> {
	List<TargetData> findByTarget(Target target);

	List<TargetData> findByTargetAndDateTimeGreaterThanEqual(Target target, LocalDateTime dateTime);

	List<TargetData> findByTargetAndDateTimeLessThan(Target target, LocalDateTime maxDateTime);

	Optional<TargetData> findTopByTargetOrderByDateTimeDesc(Target target);

	Optional<TargetData> findTopByTargetAndDateTimeGreaterThanEqualOrderByDateTimeDesc(Target target,
			LocalDateTime dateTime);

	Optional<TargetData> findTopByTargetAndDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTimeDesc(Target target,
			LocalDateTime minDateTime, LocalDateTime maxDateTime);
}
