package com.hyx.house.biz.service;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
public class FileService {

    @Value("${file.path}")
    private String filePath;

    public List<String> getImgPath(List<MultipartFile> files){
        List<String> paths = Lists.newArrayList();
        files.forEach(file ->{
            File localFile = null;
            if (!file.isEmpty()){
                try {
                    localFile =  saveToLocal(file, filePath);
                    String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(), filePath);
                    paths.add(path);
                } catch (Exception e){
                    throw new IllegalArgumentException(e);
                }
            }
        });
        return paths;
    }

    private File saveToLocal(MultipartFile file, String filePath2) throws IOException {
        File newfile = new File(filePath + "/" + Instant.now().getEpochSecond() + "/" + file.getOriginalFilename());
        newfile.getParentFile().mkdirs();
        newfile.createNewFile();
        Files.write(file.getBytes(), newfile);
        return newfile;
    }

}
