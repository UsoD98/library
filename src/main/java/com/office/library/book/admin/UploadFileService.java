package com.office.library.book.admin;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Log4j2
@Service
public class UploadFileService {

    public String upload(MultipartFile file) {

        boolean result = false;

        // 파일 저장
        String fileOriName = file.getOriginalFilename();
        String fileExtension = fileOriName.substring(fileOriName.lastIndexOf("."), fileOriName.length());
        String uploadDir = "/Users/kjw/MyProject/allinonespring/BookRentalPjt/upload";

        UUID uuid = UUID.randomUUID();
        String uniqueName = uuid.toString().replaceAll("-", "");

        File saveFile = new File(uploadDir + "/" + uniqueName + fileExtension);

        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }

        try {
            file.transferTo(saveFile);
            result = true;
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        if (result) {
            log.info("[UploadFileService] FILE UPLOAD SUCCESS");
            return uniqueName + fileExtension;
        } else {
            log.info("[UploadFileService] FILE UPLOAD FAIL");
            return null;
        }
    }
}
