package com.eshop.my_eshop.repository;

import com.eshop.my_eshop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
