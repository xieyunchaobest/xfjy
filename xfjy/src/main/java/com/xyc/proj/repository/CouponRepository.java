package com.xyc.proj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.xyc.proj.entity.Coupon;


public interface CouponRepository extends JpaRepository<Coupon, Long> {
	
}
