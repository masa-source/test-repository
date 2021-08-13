package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.SiteUser;
import com.example.demo.model.Target;

public interface TargetRepository extends JpaRepository<Target, Long> {

	Target findBySiteuserAndIsTop(SiteUser user, boolean isParent);

	List<Target> findBySiteuserAndParentId(SiteUser user, Long id);

	List<Target> findBySiteuserAndIsTopAndNameLike(SiteUser user, boolean isParent, String name);

	List<Target> findBySiteuser(SiteUser siteuser);
}