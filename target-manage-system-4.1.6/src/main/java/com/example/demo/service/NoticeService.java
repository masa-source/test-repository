package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.Notice;
import com.example.demo.model.SiteUser;
import com.example.demo.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoticeService {
	private final NoticeRepository noticeRepository;
	private final SiteUserService userService;

	public void delete(Notice notice) {
		noticeRepository.delete(notice);
	}

	public Notice createNotice(SiteUser user, String value) {
		// noticeを作成
		Notice notice = new Notice();
		notice.setSiteuser(user);
		notice.setValue(value);
		noticeRepository.save(notice);
		// userに登録
		user.addNotice(notice);
		userService.save(user);
		return notice;
	}
}
