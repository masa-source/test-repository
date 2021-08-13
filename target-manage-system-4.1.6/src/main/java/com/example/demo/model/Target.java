package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.demo.util.Kind;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Target implements TargetComponent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 30)
	private String name;

	private LocalDateTime dateTime;

	private Kind kind = Kind.target;

	@ManyToOne
	private SiteUser siteuser;

	private boolean isTop = false;

	private Long parentId;

	private Double plannedValue;

	private String unit = "";

	private String note;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	private String howToUpdate = "sum";

	private boolean isManuallyProgress = false;

	private Integer manuallyProgress = 0;

	@OneToMany(mappedBy = "target")
	private List<TargetData> data;

	@OneToMany(mappedBy = "target")
	private List<TargetTask> task;

	@ManyToMany(mappedBy = "targetList")
	private List<Activity> activity;

	public void setIsManuallyProgress(Boolean isManuallyProgress) {
		this.isManuallyProgress = isManuallyProgress;
	}

	public boolean getIsManuallyProgress() {
		return this.isManuallyProgress;
	}

	public Boolean addActivity(Activity activity) {
		if (this.activity == null) {
			this.activity = new ArrayList<Activity>();
		}
		return this.activity.add(activity);
	}
}
