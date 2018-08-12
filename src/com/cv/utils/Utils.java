/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.utils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author ayush
 */
public class Utils {
    private Utils() {
    }

    private static final SessionFactory SESSION_FACTORY;
    final static String STR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            SESSION_FACTORY = configuration.buildSessionFactory(serviceRegistry);
        } catch (HibernateException ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

    public static long getCurrentTimestamp() {
        return (new Date()).getTime();
    }

    public static int randomNumber(int min, int max) {
        Random random = new Random();
        int randomNo = random.nextInt((max - min) + 1) + min;
        return randomNo;
    }

    public static boolean isNumber(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        return Pattern.matches("-?[0-9]+", str);
    }
    
    public static boolean isFloat(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        return Pattern.matches("[-+]?[0-9]*\\.?[0-9]+", str);
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str) || "undefined".equals(str);
    }

    public static String randomString() {
        StringBuilder sb = new StringBuilder(25);
        for (int i = 0; i < 25; i++) {
            sb.append(STR.charAt(rnd.nextInt(STR.length())));
        }
        return sb.toString();
    }

    public static String getFileExtension(String fileName) {
        if (fileName != null) {
            String[] nameArr = fileName.split("\\.");
            return nameArr.length > 1 ? nameArr[nameArr.length - 1] : "";
        }
        return "";
    }

    public static String convertArrToStringObject(long[] arr) {
        String strObj = "(";
        for (int i = 0; i < arr.length; i++) {
            strObj += arr[i] + ",";
        }
        String returnStr = strObj.substring(0, strObj.length() - 1);
        return "".equals(returnStr)? returnStr: returnStr + ")";
    }

    public static String convertArrToStringObject(int[] arr) {
        String strObj = "(";
        for (int i = 0; i < arr.length; i++) {
            strObj += arr[i] + ",";
        }
        return strObj.substring(0, strObj.length() - 1) + ")";
    }
    
    public static long[] convertListToLongArray(List<Long> list) {
        long[] arr = new long[list.size()];
        for(int i=0; i<list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static void main(String[] args) {
        System.out.println(Utils.isNumber("1.2"));
    }
}
