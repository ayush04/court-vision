/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author ayush
 */
public class PasswordManager {
    private static final String SALT_STRING = "some-salt-to-password";

    private static byte[] key = {
        0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    };//"thisIsASecretKey";

    public static String oneWayEncrypt(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("SHA-256");
        String saltedPassword = SALT_STRING + password;
        byte[] digest = md5.digest(saltedPassword.getBytes("UTF-8"));
        String digestedPassword = new String(Base64.encodeBase64(digest), "US-ASCII");

        return digestedPassword;
    }

    public static String encrypt(String strToDecrypt) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToDecrypt.getBytes()));
        return encryptedString;
    }

    public static String decrypt(String encryptedString) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(encryptedString)));
        return decryptedString;
    }

    public static void main(String args[]) throws Exception {
        System.out.println(PasswordManager.oneWayEncrypt("ayush1"));
    }
}
