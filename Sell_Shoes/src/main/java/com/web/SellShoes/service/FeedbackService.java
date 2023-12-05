package com.web.SellShoes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.web.SellShoes.entity.Color;
import com.web.SellShoes.entity.Feedback;

public interface FeedbackService {
	public List<Feedback> getAll();
	public List<Feedback> findAll();
	public Optional<Feedback> getFeedback(Integer feedbackId);
	public Optional<Feedback> findById(Integer id);
	public Optional<Feedback> findByFeedbackName(String feedbackName);
	public Optional<Feedback> getFeedbackById(Integer feedbackId);
	public Page<Feedback> getAllFeedback(int pagenumber, int feedback);
	public Page<Feedback> getFeedbackByKey(int pagenumber, int feedback, String keyWord);
	public Page<Feedback> findFeedbackPage(Pageable pageable);
	public <S extends Feedback> S save(S entity);
}
