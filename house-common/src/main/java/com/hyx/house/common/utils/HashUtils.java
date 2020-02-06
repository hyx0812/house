package com.hyx.house.common.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class HashUtils {
    private static final HashFunction FUNCTION = Hashing.md5();

    private static final String ASLT = "hyx.com";

    public static String encryPassword(String password){
        HashCode hashCode = FUNCTION.hashString(password+ASLT, Charset.forName("utf-8"));
        return hashCode.toString();
    }
}
