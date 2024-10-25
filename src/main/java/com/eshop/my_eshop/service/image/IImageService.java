package com.eshop.my_eshop.service.image;

import com.eshop.my_eshop.dto.ImageDTO;
import com.eshop.my_eshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDTO> saveImages(List<MultipartFile> file, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
