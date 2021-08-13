package com.example.demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.demo.validator.UniqueLogin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 2, max = 20)
	@UniqueLogin // 自作バリデーションを追加
	private String username;

	@Size(min = 4, max = 255)
	private String password;

	@NotBlank
	@Email
	private String email;

	private int gender;
	private boolean admin;
	private String role;
	private boolean active = true;

	@OneToMany(mappedBy = "siteuser")
	private List<Target> targets = new ArrayList<Target>();

	@OneToMany(mappedBy = "siteuser")
	private List<Notice> notices = new ArrayList<Notice>();

	public Boolean addNotice(Notice notice) {
		return this.notices.add(notice);
	}
}
