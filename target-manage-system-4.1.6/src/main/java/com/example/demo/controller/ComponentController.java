package com.example.demo.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.facade.ComponentFacade;
import com.example.demo.model.Target;
import com.example.demo.model.TargetComponent;
import com.example.demo.model.TargetData;
import com.example.demo.model.TargetTask;
import com.example.demo.util.Kind;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ComponentController {
	private final ComponentFacade componentFacade;

	//一覧表示
	@GetMapping("/top")
	public String showTopComponent(Model model) {
		//		attributeを用意;
		HashMap<String, Object> map = new HashMap<String, Object>();
		TargetTask task = new TargetTask();
		TargetData data = new TargetData();
		Target target = componentFacade.getTopTarget();

		// attributeをmodelに登録
		map.put("target", target);
		map.put("parent", componentFacade.getParentTarget(target));
		map.put("parentId", target.getId());
		map.put("targetList", componentFacade.getChildTargets(target));
		map.put("task", task);
		map.put("data", data);
		model.addAllAttributes(map);

		// requestを返す
		return "top";
	}

	//詳細表示
	@GetMapping({ "/detail", "/detail/{tab}" })
	public String detailCompoment(@PathVariable(required = false) String tab, @RequestParam String kind,
			@RequestParam Long id, @RequestParam(defaultValue = "0") Long position,
			@RequestParam(required = false) String page, Model model) {
		// attributeを用意
		HashMap<String, Object> map = new HashMap<String, Object>();
		TargetComponent component = componentFacade.getById(id, kind);
		if (!componentFacade.isEditable(component)) {
			throw new IllegalArgumentException("編集権限がありません。");
		}

		// Param kind によって変化する部分
		switch (kind) {
		case "target":
			// topTargetならtopにリダイレクトする
			if (((Target) component).isTop() == true) {
				return "redirect:/top";
			}
			List<Target> targetList = componentFacade.getChildTargets(component);
			TargetTask task = new TargetTask();
			TargetData data = new TargetData();
			map.put("task", task);
			map.put("data", data);
			map.put("target", component);
			map.put("targetList", targetList);
			break;
		case "task":
			break;
		case "data":
			break;
		default:
			throw new IllegalArgumentException();
		}

		// attributeをmodelに登録
		map.put("parent", componentFacade.getParentTarget(component));
		map.put("parentId", component.getId());
		map.put("position", position);
		map.put("page", page);
		model.addAllAttributes(map);

		// requestを返す
		String request = String.format("%s_detail", kind);
		if (Arrays.asList("overview", "history", "setting")
				.contains(tab)) {
			request = String.format("%s_%s", request, tab);
		}
		return request;
	}

	//TODO Componentをインターフェイスの型で扱えないか？
	//	component追加（GET）
	@GetMapping("/add")
	public String addCompoment(@RequestParam String kind, @RequestParam(required = false) Long parentId, Target target,
			TargetTask task, TargetData data, Model model) {
		// attributeを用意
		HashMap<String, Object> map = new HashMap<String, Object>();
		// Param parentId がない時の対応（topTargetをparentとして登録）
		Target parent = (parentId == null) ? componentFacade.getTopTarget()
				: (Target) componentFacade.getById(parentId, kind);

		// Param kind によって変化する部分
		switch (kind) {
		case "target":
			map.put(kind, target);
			break;
		case "task":
			map.put(kind, task);
			break;
		case "data":
			map.put(kind, data);
			break;
		default:
			throw new IllegalArgumentException();
		}

		// attributeをmodelに登録
		map.put("parent", parent);
		model.addAllAttributes(map);

		// requestを返す
		return String.format("%s_form", kind);
	}

	//	component追加（POST・TARGET）
	@PostMapping("/add/target")
	public String targetProcess(Model model,
			@Validated @ModelAttribute("target") Target target, BindingResult result) {
		// validated処理
		if (result.hasErrors()) {
			Target parent = componentFacade.getParentTarget(target);
			model.addAttribute("parent", parent);
			model.addAttribute("parentId", parent.getId());
			return "target_form";
		}

		// repositoryに登録
		componentFacade.save(target, Kind.target.toString());

		// requestを返す
		return String.format("redirect:/detail?kind=target&id=%d", target.getId());
	}

	//	component追加（POST・TASK）
	@PostMapping("/add/task")
	public String taskProcess(Model model, @RequestParam(required = false) Long position,
			@Validated @ModelAttribute("task") TargetTask task, BindingResult result) {
		// requestの文字列を用意
		Target parent = componentFacade.getParentTarget(task);
		String request = String.format("redirect:/detail?kind=target&id=%d&position=%d", parent.getId(), position);

		// validated処理
		if (result.hasErrors()) {
			model.addAttribute("parent", parent);
			return request;
		}

		// repositoryに登録
		componentFacade.save(task, Kind.task.toString());

		// requestを返す
		return request;
	}

	//	component追加（POST・DATA）
	@PostMapping("/add/data")
	public String dataProcess(Model model, @RequestParam(required = false) Long position,
			@Validated @ModelAttribute("data") TargetData data, BindingResult result) {
		// requestの文字列を用意
		Target parent = componentFacade.getParentTarget(data);
		String request = String.format("redirect:/detail?kind=target&id=%d&position=%d&page=history", parent.getId(),
				position);

		// validated処理
		if (result.hasErrors()) {
			model.addAttribute("parent", parent);
			return request;
		}

		// repositoryに登録
		componentFacade.save(data, Kind.data.toString());

		// requestを返す
		return request;
	}

	//	component編集
	@GetMapping("/edit")
	public String editCompoment(@RequestParam String kind, @RequestParam Long id, Model model) {
		// attributeを用意
		HashMap<String, Object> map = new HashMap<String, Object>();

		// 編集権限のチェック
		TargetComponent component = componentFacade.getById(id, kind);
		if (!componentFacade.isEditable(component)) {
			throw new IllegalArgumentException("編集権限がありません。");
		}
		Target parent = componentFacade.getParentTarget(component);

		// attributeをmodelに登録
		map.put(kind, component);
		map.put("parent", parent);
		model.addAllAttributes(map);

		// requestを返す
		return String.format("%s_form", kind);
	}

	//	component削除
	@GetMapping("/delete")
	public String deleteCompoment(@RequestParam String kind, @RequestParam Long id,
			@RequestParam(defaultValue = "0") Long position, Model model) {
		// 編集権限のチェック
		TargetComponent component = componentFacade.getById(id, kind);
		if (!componentFacade.isEditable(component)) {
			throw new IllegalArgumentException("編集権限がありません。");
		}

		// repositoryに登録
		componentFacade.deleteById(id, kind);

		// requestを返す
		return String.format("redirect:/detail?kind=target&id=%d&position=%d", component.getParentId(), position);
	}

	@PostMapping("/search")
	public String searchComponent(@RequestParam String searchText, Model model) {
		// attributeをmodelに登録する
		model.addAttribute("searchText", searchText);

		//requestを返す
		return "search";
	}

	@GetMapping("/search/{tab}")
	public String getSearchContent(@PathVariable String tab, @RequestParam String searchText, Authentication loginUser,
			Model model) {
		// attributeを用意
		HashMap<String, Object> map = new HashMap<String, Object>();
		searchText = "%" + searchText + "%";
		List<? extends TargetComponent> list = componentFacade.searchByNameLike(searchText, tab);

		// attributeを登録
		map.put(tab + "List", list);
		model.addAllAttributes(map);

		// requestを返す
		String search = "search";
		if (Arrays.asList("target", "task").contains(tab)) {
			search = String.format("search_%s", tab);
		}
		return search;
	}
}
