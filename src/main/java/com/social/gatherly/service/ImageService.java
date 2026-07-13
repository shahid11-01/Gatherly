package com.social.gatherly.service;


import com.social.gatherly.configuration.GlobalConfig;
import com.social.gatherly.entity.Event;
import com.social.gatherly.Enum.ImageType;
import com.social.gatherly.repository.EventImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final GlobalConfig globalConfig;

    private final EventImageRepository eventImageRepository;

    public List<String> eventImageUpload(Event event, List<MultipartFile> eventImages, ImageType type) throws IOException {
        for (MultipartFile image : eventImages) {
            if(image.getContentType() == null || !image.getContentType().startsWith("image/")) {
                throw new IOException("존재하지 않은 이미지 타입: " + image.getContentType());
            }

        }

        Path path = Paths.get(globalConfig.getImageDir(), String.valueOf(event.getEventId()), type.getType());
        File fileDir = path.toFile();
        if(!fileDir.exists()) {
            fileDir.mkdirs();
        }
        //파일 저장하고 경로 저장
        List<String> filePathList = new ArrayList<>();

        for(MultipartFile image : eventImages) {
            String originalFileName = image.getOriginalFilename();
            if (originalFileName == null) {
                throw new IOException("이미지 이름이 존재하지 않습니다" + image.getContentType());
            }
            //이름 중복 방지
            String uuid = UUID.randomUUID().toString();
            String savedName = uuid + "_" + originalFileName;

            //파일 저장
            File destination = new File(fileDir, savedName);
            image.transferTo(destination);

            //파일 주소 저장
            filePathList.add("/image/" +event.getEventId() + "/" + type.getType() + "/" + savedName);


        }
        return filePathList;

    }

}
