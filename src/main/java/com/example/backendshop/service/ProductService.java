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


    // Xóa sản phẩm
    public void deleteProduct(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found");
        }
    }
    public Product updateProduct(String id, String name, MultipartFile image,
                                 Double price, String description, String category, String brand) throws IOException {

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }

        Product product = optionalProduct.get();

        if (name != null) product.setName(name);
        if (price != null) product.setPrice(price);
        if (description != null) product.setDescription(description);
        if (category != null) product.setCategory(category);
        if (brand != null) product.setBrand(brand);

        // Nếu có ảnh mới thì lưu lại và cập nhật
        if (image != null && !image.isEmpty()) {
            String imageUrl = FileUploadUtil.saveFile("uploads", image);
            product.setImageUrl(imageUrl);
        }

        return productRepository.save(product);
    }

}
