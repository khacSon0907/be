package com.example.backendshop.controller;

import com.example.backendshop.model.Product;
import com.example.backendshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    // API lấy danh sách tất cả sản phẩm
    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // API lấy sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        Optional<Product> product = productService.getProductById(id);
        return product.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body("Sản phẩm không tồn tại"));
    }

    // API tạo sản phẩm mới
    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createProduct(
            @RequestParam("name") String name,
            @RequestParam("image") MultipartFile image,
            @RequestParam("price") double price,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("brand") String brand) {
        try {
            Product product = productService.addProduct(name, image, price, description, category, brand);
            ResponseMessage response = new ResponseMessage("Thành công", product);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            ResponseMessage errorResponse = new ResponseMessage("Lỗi khi lưu ảnh", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // API cập nhật sản phẩm
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable String id,
            @RequestParam("name") String name,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("price") double price,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("brand") String brand) {
        try {
            Product updatedProduct = productService.updateProduct(id, name, image, price, description, category, brand);
            return ResponseEntity.ok(updatedProduct);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API xóa sản phẩm
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Xóa sản phẩm thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API để xem ảnh
    @GetMapping("/images/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get("uploads").resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                        .body(resource);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
