package com.web.SellShoes.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.SellShoes.dto.responseDto.ImageResponseDto;
import com.web.SellShoes.entity.Image;
import com.web.SellShoes.entity.Product;
import com.web.SellShoes.repository.ImageRepository;
import com.web.SellShoes.service.ImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{
	private final ImageRepository imageRepository;
	private static String UPLOADED_FOLDER = System.getProperty("user.dir") + "//src//main//resources//static//upload//";

	@Override
	public void save(Image image) {
		imageRepository.save(image);
	}

	@Override
	public void uploadFile(MultipartFile[] files, int defaultFileIndex, Product product) {
		for (int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			String nameForShow = file.getOriginalFilename();
			if (file.isEmpty()) {
				continue;
			}
			try {
				// Lưu ảnh vào thư mục tạm thời
				byte[] bytes = file.getBytes();// Lấy dữ liệu của tệp trong dạng mảng byte.

				String randomFileName = UUID.randomUUID().toString() + "."
						+ FilenameUtils.getExtension(file.getOriginalFilename());
				Path dir = Paths.get(UPLOADED_FOLDER);
				Path path = Paths.get(UPLOADED_FOLDER + randomFileName);
				//Tạo thư mục  nếu nó chưa tồn tại.
				if (!Files.exists(dir)) {

					Files.createDirectories(dir);
				}
				Files.write(path, bytes);
				// Tạo đối tượng Image và lưu xuống cơ sở dữ liệu

				Image image = new Image();
				image.setInmageForShow(nameForShow);
				image.setInmageForSave(randomFileName);
				if (i == defaultFileIndex) {
					image.setIsDefault(true);
				}
				
				image.setProduct(product);

				save(image);

			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		
	}

	@Override
	public List<ImageResponseDto> getImageByProduct(Product product) {
		List<Image> images = imageRepository.getImageByProduct(product);
		List<ImageResponseDto> imageResponseDtos = images.stream()
				.map(image -> new ImageResponseDto(image.getId(), image.getInmageForShow(), image.getInmageForSave(), image.getIsDefault()))
				.collect(Collectors.toList());
		return imageResponseDtos;
	}

	@Override
	public void deleteByProduct(Product product) {
		List<Image> images = imageRepository.getImageByProduct(product);
		for (Image image : images) {
			imageRepository.delete(image);
		}
	}

	@Override
	public void uploadFileList(List<String> fileNames, int defaultFileIndex, Product product) {
	    for (int i = 0; i < fileNames.size(); i++) {
	        String fileName = fileNames.get(i);
	        String nameForShow = fileName;

	        try {
	            // Đọc dữ liệu tệp từ thư mục tạm thời
	            Path path = Paths.get(UPLOADED_FOLDER + fileName);
	            byte[] bytes = Files.readAllBytes(path);

	            // Tạo đối tượng Image và lưu xuống cơ sở dữ liệu
	            Image image = new Image();
	            image.setInmageForShow(nameForShow);
	            image.setInmageForSave(fileName);
	            if (i == defaultFileIndex) {
	                image.setIsDefault(true);
	            }
	            
	            image.setProduct(product);

	            save(image);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

}
