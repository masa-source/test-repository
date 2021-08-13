package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Notice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String value;

	private LocalDateTime dateTime = LocalDateTime.now();

	@ManyToOne
	private SiteUser siteuser;
}
