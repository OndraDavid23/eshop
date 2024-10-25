package com.eshop.my_eshop.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eshop.my_eshop.dto.ImageDTO;
import com.eshop.my_eshop.exception.ResourceNotFoundException;
import com.eshop.my_eshop.model.Image;
import com.eshop.my_eshop.model.Product;
import com.eshop.my_eshop.repository.ImageRepository;
import com.eshop.my_eshop.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Image not found with id " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ResourceNotFoundException("Image not found with id " + id);
        });

    }

    @Override
    public List<ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product not found with id " + productId));
        List<ImageDTO> savedImageDTOS = new ArrayList<>();
        for (MultipartFile file : files) {
            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String downloadURL = "/api/v1/images/image/download/" + image.getId();
                image.setDownloadUrl(downloadURL);
                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl("/api/v1/images/image/download/" + image.getId());
                imageRepository.save(savedImage);

                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setId(savedImage.getId());
                imageDTO.setImageName(savedImage.getFileName());
                imageDTO.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDTOS.add(imageDTO);


            }   catch (IOException | SQLException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            return savedImageDTOS;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }
        catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
