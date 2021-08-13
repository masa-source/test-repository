package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.demo.util.Kind;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class TargetTask implements TargetComponent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 30)
	private String name;

	private LocalDateTime dateTime;

	private Kind kind = Kind.task;

	private boolean isDone = false;

	@ManyToOne
	private Target target;

	@Override
	public Long getParentId() {
		return target.getId();
	}

	@Override
	public SiteUser getSiteuser() {
		return this.target.getSiteuser();
	}
}
