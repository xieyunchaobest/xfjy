package com.xyc.proj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyc.proj.entity.Community;



public interface CommunityRepository extends JpaRepository<Community, Long> {
	List<Community> findByAreaId(Long areaId);
	
	@Query("select  c from Community c where c.areaId = ?1 and (name like ?2 or spellName like ?2 )   ") 
	List findListByAreaIdAndName(Long mobileNo,String authCode);
}
