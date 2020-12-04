package com.thoughtworks.buddiee.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Getter
@Setter
public class Base64Decoder implements MultipartFile {

    private final byte[] IMAGE;

    private final String HEADER;

    private final String FILETYPE;

    private Base64Decoder(byte[]image,String header){
        this.IMAGE = image;
        this.HEADER = header;
        this.FILETYPE = HEADER.split("/")[1].split(";")[0];
    }

    public static MultipartFile multipartFile(byte[]image,String header){
        return new Base64Decoder(image, header);
    }

    @Override
    public String getName() {
        return UUID.randomUUID()+"."+this.FILETYPE;
    }

    @Override
    public String getOriginalFilename() {
        return UUID.randomUUID()+"."+this.FILETYPE;
    }

    @Override
    public String getContentType() {
        return HEADER.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return IMAGE == null || IMAGE.length == 0;
    }

    @Override
    public long getSize() {
        return IMAGE.length;
    }

    @Override
    public byte[] getBytes() {
        return IMAGE;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(IMAGE);
    }

    @Override
    public void transferTo(File file) {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(IMAGE);
        } catch (IOException e) {
            log.info(e.getLocalizedMessage());
        }
    }


}
