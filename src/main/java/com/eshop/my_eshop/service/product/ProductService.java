package com.eshop.my_eshop.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eshop.my_eshop.dto.ImageDTO;
import com.eshop.my_eshop.dto.ProductDTO;
import com.eshop.my_eshop.exception.AlreadyExistsException;
import com.eshop.my_eshop.exception.ProductNotFoundException;
import com.eshop.my_eshop.model.Category;
import com.eshop.my_eshop.model.Product;
import com.eshop.my_eshop.repository.CategoryRepository;
import com.eshop.my_eshop.repository.ProductRepository;
import com.eshop.my_eshop.request.AddProductRequest;
import com.eshop.my_eshop.request.UpdateProductRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
        });
        request.setCategory(category);
        if(productExists(request)){
           throw new AlreadyExistsException(request.getBrand() + " " + request.getName() + " already exists.");

        }
        return productRepository.save(createProduct(request));
    }

    private Product createProduct(AddProductRequest request) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                request.getCategory()
        );
    }

    private boolean productExists(AddProductRequest request){
        return productRepository.existsByBrandAndName(request.getBrand(), request.getName());
    }   

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(()-> new ProductNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct ,UpdateProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(existingProduct.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;

    }

    @Override
    public Product getProduct(Product product) {
        return null;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = createProductDTO(product);
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    @Override
    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product not found"));

    }


    @Override
    public List<ProductDTO> getProductByCategory(String category) {
        List<Product> products = productRepository.findByCategoryName(category);
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = createProductDTO(product);
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return  productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return  productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Product getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, ()->{throw new ProductNotFoundException("Product not found");});

    }

    @Override
    public ProductDTO createProductDTO(Product product) {
        List<ImageDTO> imageDTOS = product.getImages().stream()
                .map(image-> new ImageDTO(image.getId(), image.getFileName(), image.getDownloadUrl()))
                .toList();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setBrand(product.getBrand());
        productDTO.setPrice(product.getPrice());
        productDTO.setInventory(product.getInventory());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategory(product.getCategory());
        productDTO.setImages(imageDTOS);

        return productDTO;
    }

}
