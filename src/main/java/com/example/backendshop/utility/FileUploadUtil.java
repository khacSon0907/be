package com.example.backendshop.utility;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

public class FileUploadUtil {
    private static final String UPLOAD_DIR = "uploads/";

    public static String saveFile(String uploadDir, MultipartFile file) throws IOException {
        // Tạo thư mục nếu chưa tồn tại
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Đổi tên file để tránh trùng
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Trả về đường dẫn truy cập ảnh
        return "/api/products/images/" + fileName;
    }
}
