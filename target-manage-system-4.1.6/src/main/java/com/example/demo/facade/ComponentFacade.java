package com.example.demo.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Target;
import com.example.demo.model.TargetComponent;
import com.example.demo.service.ActivityService;
import com.example.demo.service.SiteUserService;
import com.example.demo.service.TargetComponentService;
import com.example.demo.service.TargetDataService;
import com.example.demo.service.TargetService;
import com.example.demo.service.TargetTaskService;
import com.example.demo.util.Kind;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ComponentFacade {
	private final SiteUserService userService;
	private final TargetService targetService;
	private final TargetTaskService taskService;
	private final TargetDataService dataService;
	private final ActivityService activityService;

	public TargetComponent getById(Long id, String kind) {
		return getService(kind).findById(id).get();
	}

	public TargetComponent save(TargetComponent component, String kind) {
		return getService(kind).save(component);
	}

	public void deleteById(Long id, String kind) {
		getService(kind).deleteById(id);
	}

	public Boolean isEditable(TargetComponent component) {
		return userService.getLoginUser() == component.getSiteuser();
	}

	public List<? extends TargetComponent> searchByNameLike(String searchText, String kind) {
		if (Arrays.asList("target", "task").contains(kind)) {
			return getService(kind).searchByNameLike(searchText);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public Target getTopTarget() {
		return targetService.getTopTarget();
	}

	public List<Target> getChildTargets(TargetComponent component) {
		if (component.getKind() == Kind.target) {
			return targetService.getChildTargets((Target) component);
		} else {
			return targetService.getChildTargets(getParentTarget(component));
		}
	}

	public Target getParentTarget(TargetComponent component) {
		Long parentId = component.getParentId();
		if (parentId == null) {
			parentId = getTopTarget().getId();
		}
		return targetService.findById(parentId).get();
	}

	public List<Target> getRouteTargetList(TargetComponent component) {
		List<Target> targetList = new ArrayList<Target>();
		if (component == getTopTarget()) {
			return targetList;
		}
		return getTargetList(targetList, component);
	}

	private TargetComponentService getService(String kind) {
		TargetComponentService service = null;
		switch (kind) {
		case "target":
			service = targetService;
			break;
		case "task":
			service = taskService;
			break;
		case "data":
			service = dataService;
			break;
		case "activity":
			service = activityService;
			break;
		default:
			throw new IllegalArgumentException();
		}
		return service;
	}

	private List<Target> getTargetList(List<Target> list, TargetComponent component) {
		if (component.getParentId() == getTopTarget().getId()) {
			list.add(0, getTopTarget());
			return list;
		} else {
			list.add(0, getParentTarget(component));
			getTargetList(list, getParentTarget(component));
		}
		return list;
	}
}
