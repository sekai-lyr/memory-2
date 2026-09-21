package com.youkeda.application.ebusiness.control;

import com.youkeda.application.ebusiness.model.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
public class UploadControl {

    @Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    @PostMapping("/upload/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        Result<String> result = new Result<>();
        if (file == null || file.isEmpty()) {
            result.setCode("400");
            result.setMessage("请选择要上传的图片");
            result.setSuccess(false);
            return result;
        }

        String originalName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String ext = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0) {
            ext = originalName.substring(dot).toLowerCase();
        }
        String[] allowed = {".jpg", ".jpeg", ".png", ".gif", ".webp", ".bmp", ".svg"};
        boolean okExt = false;
        for (String a : allowed) {
            if (a.equals(ext)) {
                okExt = true;
                break;
            }
        }
        if (!okExt) {
            result.setCode("400");
            result.setMessage("仅支持图片格式：jpg / png / gif / webp / svg");
            result.setSuccess(false);
            return result;
        }
        if (file.getSize() > 10 * 1024 * 1024) {
            result.setCode("400");
            result.setMessage("图片大小不能超过 10MB");
            result.setSuccess(false);
            return result;
        }

        try {
            String fileName = "img_" + UUID.randomUUID().toString().replace("-", "") + ext;
            File dir = new File(uploadDir);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IllegalStateException("无法创建上传目录");
            }
            File target = new File(dir, fileName);
            file.transferTo(target.getAbsoluteFile());

            result.setCode("200");
            result.setMessage("上传成功");
            result.setSuccess(true);
            result.setData("/uploads/" + fileName);
            return result;
        } catch (Exception e) {
            result.setCode("500");
            result.setMessage("上传失败：" + e.getMessage());
            result.setSuccess(false);
            return result;
        }
    }
}
