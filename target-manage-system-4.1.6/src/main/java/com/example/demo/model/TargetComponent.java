package com.example.demo.model;

import java.time.LocalDateTime;

import com.example.demo.util.Kind;

public interface TargetComponent {

	public abstract Long getId();

	public abstract Kind getKind();

	public abstract LocalDateTime getDateTime();

	public abstract SiteUser getSiteuser();

	public abstract Long getParentId();
}
