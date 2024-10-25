package com.eshop.my_eshop.service.product;

import java.util.List;

import com.eshop.my_eshop.dto.ProductDTO;
import com.eshop.my_eshop.model.Product;
import com.eshop.my_eshop.request.AddProductRequest;
import com.eshop.my_eshop.request.UpdateProductRequest;

public interface IProductService {
    Product addProduct(AddProductRequest request);
    Product updateProduct(UpdateProductRequest request, Long productId);
    Product getProduct(Product product);
    List<ProductDTO> getAllProducts();

    Product getProductById(Long id);
    List<ProductDTO>  getProductByCategory(String category);
    List<Product>  getProductByBrand(String brand);
    List<Product>  getProductByCategoryAndBrand(String category, String brand);
    List<Product>  getProductByName(String name);
    Product getProductByBrandAndName(String brand, String name);
    public ProductDTO createProductDTO(Product product);

    Long countProductsByBrandAndName(String brand, String name);

    void deleteProductById(Long id);

}
