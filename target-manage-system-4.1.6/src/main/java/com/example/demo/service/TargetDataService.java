package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Target;
import com.example.demo.model.TargetComponent;
import com.example.demo.model.TargetData;
import com.example.demo.model.TargetTask;
import com.example.demo.repository.TargetDataRepository;
import com.example.demo.repository.TargetRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TargetDataService extends TargetComponentService {
	private final TargetRepository targetRepository;
	private final TargetDataRepository dataRepository;

	public Double getSum(Target target) {
		switch (target.getHowToUpdate()) {
		case "sum":
			List<TargetData> dataList = dataRepository.findByTarget(target);
			return dataList.stream().mapToDouble(d -> d.getValue()).sum();
		case "update":
			Optional<TargetData> data = dataRepository.findTopByTargetOrderByDateTimeDesc(target);
			Double sum = data.map(d -> d.getValue()).orElse(0.0);
			return sum;
		default:
			throw new IllegalArgumentException();
		}
	}

	public Double getDailySum(Target target) {
		LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
		switch (target.getHowToUpdate()) {
		case "sum":
			List<TargetData> dataList = dataRepository.findByTargetAndDateTimeGreaterThanEqual(target, dateTime);
			return dataList.stream().mapToDouble(d -> d.getValue()).sum();
		case "update":
			Optional<TargetData> data = dataRepository
					.findTopByTargetAndDateTimeGreaterThanEqualOrderByDateTimeDesc(target, dateTime);
			Double sum = data.map(d -> d.getValue()).orElse(0.0);
			return sum;
		default:
			throw new IllegalArgumentException();
		}
	}

	// 目標のみ
	public Long getProgress(Target target) {
		if (target.getIsManuallyProgress()) {
			return (long) target.getManuallyProgress();
		}

		if (target.getPlannedValue() == null) {
			return null;
		}

		Long parcent = Math.round((getSum(target) / target.getPlannedValue()) * 100);

		if (parcent > 1000) {
			parcent = (long) 999;
		} else if (parcent < -1000) {
			parcent = (long) -999;
		}
		return parcent;
	}

	// 課題
	public Long getTaskProgress(Target target) {
		Long taskProgress = null;
		List<TargetTask> taskList = target.getTask();
		if (!taskList.isEmpty()) {
			int taskAmount = 0;
			int doneTask = 0;
			for (Iterator<TargetTask> iterator = taskList.iterator(); iterator.hasNext();) {
				TargetTask targetTask = iterator.next();
				taskAmount++;
				if (targetTask.isDone()) {
					doneTask++;
				}
			}
			taskProgress = (long) doneTask * 100 / taskAmount;
		}
		return taskProgress;
	}

	public Long getChildProgress(Target target) {
		Long childProgress = null;
		List<Target> targetList = targetRepository.findBySiteuserAndParentId(target.getSiteuser(), target.getId());
		if (!targetList.isEmpty()) {
			int targetAmount = 0;
			int totalPercent = 0;
			for (Iterator<Target> iterator = targetList.iterator(); iterator.hasNext();) {
				Target childTarget = iterator.next();
				Long progress = getTotalProgress(childTarget);
				if (progress != null) {
					targetAmount++;
					totalPercent += progress;
				}
			}
			if (targetAmount != 0) {
				childProgress = (long) Math.round(totalPercent / targetAmount);
			}
		}
		return childProgress;
	}

	// 目標と課題と子目標
	public Long getTotalProgress(Target target) {
		int amount = 3;
		List<Long> progressList = Arrays.asList(getProgress(target), getTaskProgress(target), getChildProgress(target));
		int i = 0;
		for (Iterator<Long> iterator = progressList.iterator(); iterator.hasNext(); i++) {
			Long progress = iterator.next();
			if (progress == null) {
				progress = (long) 0;
				amount--;
			} else if (progress > 100) {
				progress = (long) 100;
			}
			progressList.set(i, progress);
		}
		long sum = progressList.stream().mapToLong(s -> (long) s).sum();

		Long totalProgress = (amount != 0) ? sum / amount : null;
		return totalProgress;
	}

	public ArrayList<Long> getProgressList(Target target) {
		ArrayList<Long> progressList = new ArrayList<Long>();

		if (target.getPlannedValue() == null) {
			for (int i = 0; i < 12; i++) {
				progressList.add((long) 0);
			}
			return progressList;
		}

		LocalDate nowDate = LocalDate.now();
		LocalDate endDate = LocalDate.of(nowDate.getYear(), nowDate.getMonth().plus(1), 1);
		LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(0, 0));
		for (int i = 0; i < 12; i++) {
			Double sum = 0.0;
			switch (target.getHowToUpdate()) {
			case "sum":
				List<TargetData> dataList = dataRepository.findByTargetAndDateTimeLessThan(target, endDateTime);
				sum = dataList.stream().mapToDouble(d -> d.getValue()).sum();
				break;
			case "update":
				Optional<TargetData> data = dataRepository
						.findTopByTargetAndDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTimeDesc(target,
								endDateTime.minusMonths(1), endDateTime);
				if (data.isPresent()) {
					sum = data.get().getValue();
				}
				break;
			default:
				throw new IllegalArgumentException();
			}
			Long parcent = Math.round((sum / target.getPlannedValue()) * 100);

			if (parcent > 1000) {
				parcent = (long) 999;
			} else if (parcent < -1000) {
				parcent = (long) -999;
			}

			progressList.add(0, parcent);

			endDateTime = endDateTime.minusMonths(1);
		}
		return progressList;
	}

	public ArrayList<Double> getActualProgressList(Target target) {
		ArrayList<Double> actualProgressList = new ArrayList<Double>();

		if (target.getPlannedValue() == null) {
			for (int i = 0; i < 12; i++) {
				actualProgressList.add(0.0);
			}
			return actualProgressList;
		}

		LocalDate nowDate = LocalDate.now();
		LocalDate endDate = LocalDate.of(nowDate.getYear(), nowDate.getMonth().plus(1), 1);
		LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(0, 0));
		for (int i = 0; i < 12; i++) {
			Double sum = 0.0;
			switch (target.getHowToUpdate()) {
			case "sum":
				List<TargetData> dataList = dataRepository.findByTargetAndDateTimeLessThan(target, endDateTime);
				sum = dataList.stream().mapToDouble(d -> d.getValue()).sum();
				break;
			case "update":
				Optional<TargetData> data = dataRepository
						.findTopByTargetAndDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTimeDesc(target,
								endDateTime.minusMonths(1), endDateTime);
				if (data.isPresent()) {
					sum = data.get().getValue();
				}
				break;
			default:
				throw new IllegalArgumentException();
			}

			actualProgressList.add(0, sum);

			endDateTime = endDateTime.minusMonths(1);
		}
		return actualProgressList;
	}

	public ArrayList<Long> getStateList(Target target) {
		ArrayList<Long> stateList = new ArrayList<Long>();
		Long completed = (long) 0;
		Long execution = (long) 0;
		Long waiting = (long) 0;
		List<Target> targetList = targetRepository.findBySiteuserAndParentId(target.getSiteuser(), target.getId());
		for (Iterator<Target> iterator = targetList.iterator(); iterator.hasNext();) {
			Target childTarget = iterator.next();
			Long targetProgress = getTotalProgress(childTarget);
			if (targetProgress == null || targetProgress <= 0) {
				waiting++;
			} else if (targetProgress >= 100) {
				completed++;
			} else {
				execution++;
			}
		}

		stateList.add(completed);
		stateList.add(execution);
		stateList.add(waiting);
		return stateList;
	}

	public ArrayList<Long> getTaskStateList(Target target) {
		ArrayList<Long> taskStateList = new ArrayList<Long>();
		Long completed = (long) 0;
		Long execution = (long) 0;
		Long waiting = (long) 0;
		List<TargetTask> taskList = target.getTask();
		for (Iterator<TargetTask> iterator = taskList.iterator(); iterator.hasNext();) {
			TargetTask targetTask = iterator.next();
			if (targetTask.isDone()) {
				completed++;
			} else {
				waiting++;
			}
		}

		taskStateList.add(completed);
		taskStateList.add(execution);
		taskStateList.add(waiting);
		return taskStateList;
	}

	@Override
	public TargetData tmp() {
		TargetData data = new TargetData();
		data.setValue(1.0);
		return data;
	}

	@Override
	public Optional<TargetData> findById(Long id) {
		return dataRepository.findById(id);
	}

	@Override
	public TargetData save(TargetComponent component) {
		TargetData data = (TargetData) component;
		data.setDateTime(LocalDateTime.now());
		dataRepository.save(data);
		String activityText = "\"" + data.getValue() + "\"をデータとして登録しました。";
		//		createActivity(data, activityText);
		return data;
	}

	@Override
	public void deleteById(Long id) {
		TargetData data = findById(id).get();
		String activityText = "\"" + data.getValue() + "\"を削除しました。";
		//		createActivity(data, activityText);
		dataRepository.delete(data);
	}
}
