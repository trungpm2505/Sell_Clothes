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

import com.web.SellShoes.dto.requestDto.ColorRequestDto;
import com.web.SellShoes.dto.responseDto.ColorPageResponseDto;
import com.web.SellShoes.dto.responseDto.ColorResponseDto;
import com.web.SellShoes.entity.Color;
import com.web.SellShoes.service.ColorService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/color")
public class ColorController {
	private final ColorService colorService;
	
	@GetMapping()
	public String list(HttpSession session,Model model) {
		model.addAttribute("fullName",(String) session.getAttribute("fullName"));
		return "admin/colors/colorform";
	}
	
	@GetMapping(value = "/getAll")
	public ResponseEntity<?> getAllColor() {
		List<Color> colors = colorService.getAll();
		
		List<ColorResponseDto> colorResponseDtos = colors.stream()
				.map(size -> new ColorResponseDto(size.getId(),
						size.getColor()))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(colorResponseDtos);
	}
	@GetMapping("/getColorPage")
	public ResponseEntity<ColorPageResponseDto> getColorPage(@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword // Đọc tham số
																										// page từ URL
	) {
		Page<Color> colorPage = null;
		if (keyword == null || keyword.isEmpty()) {
			colorPage = colorService.getAllColor(page, size);
		} else {
			colorPage = colorService.getColorByKey(page, size, keyword);
		}
		List<ColorResponseDto> colorResponseDtos = colorPage.stream().map(
				color1 -> new ColorResponseDto(color1.getId(), color1.getColor(), color1.getCreateAt(), color1.getUpdateAt()))
				.collect(Collectors.toList());
		ColorPageResponseDto colorPageResponseDto = new ColorPageResponseDto(colorPage.getTotalPages(),
				colorPage.getNumber(), colorPage.getSize(), colorResponseDtos);
		return ResponseEntity.ok(colorPageResponseDto);
	}
	
	@PostMapping("/saveColor")
	@ResponseBody
	public ResponseEntity<?> saveColor(@Valid @RequestBody ColorRequestDto colorRequestDto, BindingResult bindingResult) {
		Map<String, Object> errors = new HashMap<>();
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
		}
		Optional<Color> existingColor = colorService.findByColorName(colorRequestDto.getColorName());

		if (existingColor.isPresent()) {
			errors.put("nameDuplicate", "Color name already exists");
		}
		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}

		Color color = new Color();
		color.setColor(colorRequestDto.getColorName());
		colorService.save(color);
		// return ResponseEntity.ok("Success");
		return ResponseEntity.ok().body("Add color successfully");

	}
	@GetMapping("/deleteColor")
	public ResponseEntity<String> deleteColor(ModelMap model, @RequestParam("idColor") int idColor) {
		Optional<Color> entity = colorService.findById(idColor);

		if (entity.isPresent()) {
			Color color = entity.get();
			// Kiểm tra xem danh mục có sản phẩm nào không

			if (color.getVariants().isEmpty()) {
				// Nếu không có sản phẩm liên quan, thực hiện xóa danh mục
				colorService.delete(color);
				return ResponseEntity.ok("Delete successful");

			} else {
				// Nếu danh mục có sản phẩm liên quan, trả về một thông báo hoặc mã lỗi tùy theo
				// yêu cầu của bạn
				return ResponseEntity.badRequest().body("Color contains associated variant and cannot be deleted");
			}
		}
		return ResponseEntity.notFound().build();
	}
	@PostMapping("/editColor")
	@ResponseBody
	public ResponseEntity<?> editColor(@Valid @RequestBody ColorRequestDto colorRequestDto,
			BindingResult bindingResult) {
		Map<String, Object> errors = new HashMap<>();
		
		if (bindingResult.hasErrors()) {
			errors.put("bindingErrors", bindingResult.getAllErrors());
			return ResponseEntity.badRequest().body(errors);
		}
		
		Optional<Color> existingColor = colorService.findByColorName(colorRequestDto.getColorName());

		if (existingColor.isPresent() && !existingColor.get().getId().equals(colorRequestDto.getId())) {
			errors.put("nameDuplicate", "Color already exists! Please enter a new SizeName.");
			return ResponseEntity.badRequest().body(errors);
		}
		if (!colorService.getColorById(colorRequestDto.getId()).isPresent()) {
			errors.put("nameDuplicate", "This color does not exist! Update failed");
			return ResponseEntity.badRequest().body(errors);
		}

		Color color = new Color();
		color.setId(colorRequestDto.getId());
		color.setColor(colorRequestDto.getColorName());
		color.setUpdateAt(LocalDate.now());
		colorService.save(color);
		// return ResponseEntity.ok("Success");
		return ResponseEntity.ok().body("Edit color successfully");

	}
}
