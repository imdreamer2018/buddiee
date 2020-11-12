package com.thoughtworks.buddiee.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

public class Base64Converter {

    private Base64Converter() {
        throw new IllegalStateException("Utility class");
    }

    public static MultipartFile converter(String source){
        String [] charArray = source.split(",");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes;
        bytes = decoder.decode(charArray[1]);
        for (int i = 0; i < bytes.length; i++){
            if(bytes[i] < 0){
                bytes[i]+=256;
            }
        }
        return Base64Decoder.multipartFile(bytes,charArray[0]);
    }
}
