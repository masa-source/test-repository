package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.example.demo.util.Kind;
import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Activity implements TargetComponent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String value;

	private LocalDateTime dateTime = LocalDateTime.now();

	private Kind kind = Kind.activity;

	private Long parentId;

	@ManyToMany
	private List<Target> targetList = new ArrayList<Target>();

	public Boolean addTarget(Target target) {
		return this.targetList.add(target);
	}

	@Override
	public SiteUser getSiteuser() {
		return this.targetList.get(0).getSiteuser();
	}
}
