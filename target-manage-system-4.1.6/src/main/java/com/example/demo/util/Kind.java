package com.example.demo.util;

import lombok.Getter;

@Getter
public enum Kind {
	target("目標"), task("課題"), data("データ"), activity("活動");

	private String name;

	private Kind(String name) {
		this.name = name;
	}
}
