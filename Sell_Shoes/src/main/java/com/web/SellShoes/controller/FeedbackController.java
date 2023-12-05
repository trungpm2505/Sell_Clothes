package com.web.SellShoes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.requestDto.FeedbackDto;
import com.web.SellShoes.dto.responseDto.FeedbackPageResponseDto;
import com.web.SellShoes.dto.responseDto.FeedbackResponseDto;
import com.web.SellShoes.entity.Feedback;
import com.web.SellShoes.service.FeedbackService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/feedback")
public class FeedbackController {
	private final FeedbackService feedbackService;

	@GetMapping()
	public String list(HttpSession session,Model model) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		return "admin/feedback/feedbackform";
	}

	@GetMapping("/getFeedback")
	public ResponseEntity<?> getFeedback(@RequestParam Integer id) {
		Optional<Feedback> feedback = feedbackService.getFeedback(id);
		FeedbackResponseDto feedbackResponseDto = null;
		if (feedback.isPresent()) {
			feedbackResponseDto = new FeedbackResponseDto(id, feedback.get().getFullName(), feedback.get().getEmail(),
					feedback.get().getPhone(), feedback.get().getSubjectName(), feedback.get().getNote(),
					feedback.get().getCreateAt());
		}

		return ResponseEntity.ok(feedbackResponseDto);
	}

	@GetMapping("/getFeedbackPage")
	public ResponseEntity<FeedbackPageResponseDto> getFeedbackPage(@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword // Đọc tham số
																										// page từ URL
	) {
		Page<Feedback> feedbackPage = null;
		if (keyword == null || keyword.isEmpty()) {
			feedbackPage = feedbackService.getAllFeedback(page, size);
		} else {
			feedbackPage = feedbackService.getFeedbackByKey(page, size, keyword);
		}
		List<FeedbackResponseDto> feedbackResponseDtos = feedbackPage.stream()
				.map(feedback -> new FeedbackResponseDto(feedback.getId(), feedback.getFullName(), feedback.getEmail(),
						feedback.getPhone(), feedback.getSubjectName(), feedback.getNote(), feedback.getCreateAt()))
				.collect(Collectors.toList());
		FeedbackPageResponseDto feedbackPageResponseDto = new FeedbackPageResponseDto(feedbackPage.getTotalPages(),
				feedbackPage.getNumber(), feedbackPage.getSize(), feedbackResponseDtos);
		return ResponseEntity.ok(feedbackPageResponseDto);
	}
	
	
	@GetMapping(value = "/userfeedback")
	public String user(HttpSession session,Model model) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		return "shop/shopcontent/userfeedback";
	}
	
	@PostMapping("/saveUserfeedback")
	@ResponseBody
	public ResponseEntity<?> saveUserfeedback(@Valid @RequestBody FeedbackDto feedbackDto, BindingResult bindingResult){
		Map<String, Object> errors = new HashMap<>();
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
	}
		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}
		
		Feedback feedback = new Feedback();
		feedback.setId(feedbackDto.getId());
		feedback.setFullName(feedbackDto.getFullName());
		feedback.setEmail(feedbackDto.getEmail());
		feedback.setPhone(feedbackDto.getPhone());
		feedback.setSubjectName(feedbackDto.getSubjectName());
		feedback.setNote(feedbackDto.getNote());
		feedbackService.save(feedback);
		// return ResponseEntity.ok("Success");
		return ResponseEntity.ok().body("Send feedback successfully");
	}
}
