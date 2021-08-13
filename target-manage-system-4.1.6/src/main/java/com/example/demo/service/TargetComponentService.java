package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.SiteUser;
import com.example.demo.model.TargetComponent;

@Service
public abstract class TargetComponentService {

	public TargetComponent tmp() {
		throw new UnsupportedOperationException();
	}

	public TargetComponent tmp(SiteUser user) {
		throw new UnsupportedOperationException();
	}

	public abstract Optional<? extends TargetComponent> findById(Long id);

	public abstract TargetComponent save(TargetComponent component);

	public abstract void deleteById(Long id);

	public List<? extends TargetComponent> searchByNameLike(String searchText) {
		throw new UnsupportedOperationException();
	}

	//	public abstract Activity createActivity(TargetComponent component, String value);

}
