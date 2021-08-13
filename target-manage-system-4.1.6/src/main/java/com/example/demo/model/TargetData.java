package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.example.demo.util.Kind;
import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class TargetData implements TargetComponent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private Double value;

	private LocalDateTime dateTime;

	private Kind kind = Kind.data;

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
