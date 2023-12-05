package com.web.SellShoes.controller;

import java.time.LocalDate;
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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.SellShoes.dto.requestDto.SizeRequestDto;
import com.web.SellShoes.dto.responseDto.SizePageResponseDto;
import com.web.SellShoes.dto.responseDto.SizeResponseDto;
import com.web.SellShoes.entity.Size;
import com.web.SellShoes.service.SizeService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/size")
public class SizeController {
	private final SizeService sizeService;

	@GetMapping()
	public String list(HttpSession session,Model model) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		return "admin/size/sizeform";
	}

	@GetMapping(value = "/getAll")
	public ResponseEntity<?> getAllSize() {
		List<Size> sizes = sizeService.getAll();

		List<SizeResponseDto> sizeResponseDtos = sizes.stream()
				.map(size -> new SizeResponseDto(size.getId(), size.getSize())).collect(Collectors.toList());

		return ResponseEntity.ok(sizeResponseDtos);
	}

	@GetMapping("/getSizePage")
	public ResponseEntity<SizePageResponseDto> getSizePage(@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword // Đọc tham số
																										// page từ URL
	) {
		Page<Size> sizePage = null;
		if (keyword == null || keyword.isEmpty()) {
			sizePage = sizeService.getAllSize(page, size);
		} else {
			sizePage = sizeService.getSizeByKey(page, size, keyword);
		}
		List<SizeResponseDto> sizeResponseDtos = sizePage.stream().map(
				size1 -> new SizeResponseDto(size1.getId(), size1.getSize(), size1.getCreateAt(), size1.getUpdateAt()))
				.collect(Collectors.toList());
		SizePageResponseDto sizePageResponseDto = new SizePageResponseDto(sizePage.getTotalPages(),
				sizePage.getNumber(), sizePage.getSize(), sizeResponseDtos);
		return ResponseEntity.ok(sizePageResponseDto);
	}

	@PostMapping("/saveSize")
	@ResponseBody
	public ResponseEntity<?> saveSize(@Valid @RequestBody SizeRequestDto sizeRequestDto, BindingResult bindingResult) {
		Map<String, Object> errors = new HashMap<>();
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}
		Optional<Size> existingSize = sizeService.findBySizeName(sizeRequestDto.getSizeName());

		if (existingSize.isPresent()) {
			errors.put("nameDuplicate", "Size name already exists");
		}
		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}

		Size size = new Size();
		size.setSize(sizeRequestDto.getSizeName());
		sizeService.save(size);
		// return ResponseEntity.ok("Success");
		return ResponseEntity.ok().body("Add size successfully");

	}

	@GetMapping("/deleteSize")
	public ResponseEntity<String> deleteSize(ModelMap model, @RequestParam("idSize") int idSize) {
		Optional<Size> entity = sizeService.findById(idSize);

		if (entity.isPresent()) {
			Size size = entity.get();
			// Kiểm tra xem danh mục có sản phẩm nào không

			if (size.getVariants().isEmpty()) {
				// Nếu không có sản phẩm liên quan, thực hiện xóa danh mục
				sizeService.delete(size);
				return ResponseEntity.ok("Delete successful");

			} else {
				// Nếu danh mục có sản phẩm liên quan, trả về một thông báo hoặc mã lỗi tùy theo
				// yêu cầu của bạn
				return ResponseEntity.badRequest().body("Size contains associated variant and cannot be deleted");
			}
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/editSize")
	@ResponseBody
	public ResponseEntity<?> editSize(@Valid @RequestBody SizeRequestDto sizeRequestDto,
			BindingResult bindingResult) {
		Map<String, Object> errors = new HashMap<>();
		
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
			return ResponseEntity.badRequest().body(errors);
		}
		
		Optional<Size> existingSize = sizeService.findBySizeName(sizeRequestDto.getSizeName());

		if (existingSize.isPresent() && !existingSize.get().getId().equals(sizeRequestDto.getId())) {
			errors.put("nameDuplicate", "Size already exists! Please enter a new SizeName.");
			return ResponseEntity.badRequest().body(errors);
		}
		if (!sizeService.getSizeById(sizeRequestDto.getId()).isPresent()) {
			errors.put("nameDuplicate", "This size does not exist! Update failed");
			return ResponseEntity.badRequest().body(errors);
		}

		Size size = new Size();
		size.setId(sizeRequestDto.getId());
		size.setSize(sizeRequestDto.getSizeName());
		size.setUpdateAt(LocalDate.now());
		sizeService.save(size);
		// return ResponseEntity.ok("Success");
		return ResponseEntity.ok().body("Edit size successfully");

	}
}
