package com.example.backendshop.service;

import com.example.backendshop.model.Product;
import com.example.backendshop.repository.ProductRepository;
import com.example.backendshop.utility.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Thêm sản phẩm
    public Product addProduct(String name, MultipartFile image, double price, String description, String category, String brand) throws IOException {
        String imageUrl = FileUploadUtil.saveFile("uploads", image);
        Product product = new Product(null, name, imageUrl, price, description, category, brand);
        return productRepository.save(product);
    }

    // Lấy danh sách tất cả sản phẩm
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Lấy sản phẩm theo ID (id là String)
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    // Cập nhật sản phẩm
    public Product updateProduct(String id, String name, MultipartFile image, double price, String description, String category, String brand) throws IOException {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setCategory(category);
            product.setBrand(brand);

            // Nếu có ảnh mới, cập nhật ảnh
            if (image != null && !image.isEmpty()) {
                String imageUrl = FileUploadUtil.saveFile("uploads", image);
                product.setImageUrl(imageUrl);
            }

            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    // Xóa sản phẩm
    public void deleteProduct(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found");
        }
    }
}
