package com.web.SellShoes.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.SellShoes.entity.Color;
import com.web.SellShoes.entity.Feedback;
import com.web.SellShoes.repository.FeedbackRepository;
import com.web.SellShoes.service.FeedbackService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
	private final FeedbackRepository feedbackRepository;

	@Override
	public List<Feedback> getAll() {
		List<Feedback> feedbacks = feedbackRepository.findAll();
		return feedbacks;
	}

	@Override
	public List<Feedback> findAll() {
		List<Feedback> feedbacks = feedbackRepository.findAll();
		return feedbacks;
	}

	@Override
	public Optional<Feedback> getFeedback(Integer feedbackId) {
		Optional<Feedback> feedback = feedbackRepository.getFeedbackById(feedbackId);
		return feedback;
	}

	@Override
	public Optional<Feedback> findById(Integer id) {
		
		return feedbackRepository.findById(id);
	}

	@Override
	public Optional<Feedback> getFeedbackById(Integer feedbackId) {
		
		return feedbackRepository.getFeedbackById(feedbackId);
	}


	@Override
	public Page<Feedback> getAllFeedback(int pagenumber, int feedback) {
		PageRequest feedbackPageable = PageRequest.of(pagenumber, feedback, Sort.by(Sort.Direction.ASC,"id"));
		Page<Feedback> feedbackPage = feedbackRepository.findFeedbackPage(feedbackPageable);
		return feedbackPage;
	}

	@Override
	public Page<Feedback> getFeedbackByKey(int pagenumber, int feedback, String keyWord) {
		PageRequest feedbackPageable = PageRequest.of(pagenumber, feedback, Sort.by(Sort.Direction.ASC,"id"));
		return feedbackRepository.findByKeyWord(feedbackPageable, keyWord);
	}

	@Override
	public Page<Feedback> findFeedbackPage(Pageable pageable) {
		
		return feedbackRepository.findFeedbackPage(pageable);
	}


	@Override
	public <S extends Feedback> S save(S entity) {
		return feedbackRepository.save(entity);
	}

	@Override
	public Optional<Feedback> findByFeedbackName(String feedbackName) {
		return feedbackRepository.findByFeedbackName(feedbackName);
	}
}
