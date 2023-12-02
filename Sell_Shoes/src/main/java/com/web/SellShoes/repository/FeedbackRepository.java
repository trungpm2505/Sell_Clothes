package com.web.SellShoes.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.SellShoes.entity.Feedback;
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer>{
	public Optional<Feedback> getFeedbackById(Integer feedbackId);

	@Query("SELECT c FROM Feedback c ")
	Page<Feedback> findFeedbackPage(Pageable pageable);

	@Query("SELECT c FROM Feedback c WHERE c.fullName LIKE %:keyword% or c.email LIKE %:keyword% or c.phone LIKE %:keyword% or c.subjectName LIKE %:keyword%")
	Page<Feedback> findByKeyWord(Pageable pageable, String keyword);
	
	@Query("SELECT c FROM Feedback c WHERE c.fullName = :feedbackName")
	Optional<Feedback> findByFeedbackName(String feedbackName);
}
