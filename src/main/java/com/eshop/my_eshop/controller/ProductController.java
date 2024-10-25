package com.eshop.my_eshop.controller;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eshop.my_eshop.dto.ProductDTO;
import com.eshop.my_eshop.exception.AlreadyExistsException;
import com.eshop.my_eshop.exception.ProductNotFoundException;
import com.eshop.my_eshop.model.Product;
import com.eshop.my_eshop.request.AddProductRequest;
import com.eshop.my_eshop.request.UpdateProductRequest;
import com.eshop.my_eshop.response.ApiResponse;
import com.eshop.my_eshop.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
@CrossOrigin("http://localhost:3000")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        try{
            List<ProductDTO> productList = productService.getAllProducts();
            return ResponseEntity.ok(new ApiResponse("Categories found", productList));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", null));
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable long id){
        try {
            Product product = productService.getProductById(id);
            ProductDTO productDTO = productService.createProductDTO(product);
            return ResponseEntity.ok(new ApiResponse("Product found", productDTO));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/name/{name}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name){
        try {
            List<Product> listOfProduct = productService.getProductByName(name);
            if(listOfProduct.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products found", listOfProduct));
        } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/product/category/{category}")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category){
        try {
            List<ProductDTO> listOfProduct = productService.getProductByCategory(category);
            if(listOfProduct.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products found", listOfProduct));
        } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/product/name-brand")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brand, @RequestParam String name){
        try{
            Product product = productService.getProductByBrandAndName(brand, name);
            if(product == null){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found!", null));
            }
            return ResponseEntity.ok(new ApiResponse("Product found", product));
        } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/product")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody AddProductRequest request){
        try {
            Product product = productService.addProduct(request);
            return ResponseEntity.ok(new ApiResponse("Product created", product));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/product/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable long id, @RequestBody UpdateProductRequest request){
        try {
            Product product = productService.updateProduct(request, id);
            return ResponseEntity.ok(new ApiResponse("Product updated", product));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable long id){
        try{
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Product deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
