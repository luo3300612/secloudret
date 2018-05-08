package com.utils;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;


public class Utils {
    public static final String sources = "qwertyuioplkjhgfdsazxcvbnmQWERTYUIOPLKJHGFDSAZXCVBNM";
    public static String string2Bits(String string) {
        byte[] bytes = string.getBytes();
        //System.out.println("bytes" + Arrays.toString(bytes));
        return bytes2Bits(bytes);
    }

    public static String bits2String(String bits) {
        //System.out.println("bytes:" + Arrays.toString(bytes));
        return new String(bits2Bytes(bits));
    }


    public static String bytes2Bits(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean flag = false;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] < 0) {
                bytes[i] = (byte) -bytes[i];
                flag = true;
            }
            for (int j = 0; 6 >= j; j++) {
                //System.out.println(bytes[i]);
                if ((bytes[i] & 1) == 1) {
                    stringBuilder.append("1");
                } else {
                    stringBuilder.append("0");
                }
                bytes[i] = (byte) (bytes[i] >> 1);
            }
            if (flag) {
                stringBuilder.append("1");
            } else {
                stringBuilder.append("0");
            }
            flag = false;
            //System.out.println(stringBuilder);
        }
        return stringBuilder.toString();
    }


    public static byte[] bits2Bytes(String bits) {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = new byte[bits.length() / 8];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
            for (int j = 0; j <= 6; j++) {
                bytes[i] = (byte) (bytes[i] + (int) (bits.charAt(8 * i + j) - '0') * Math.pow(2, j));
                //System.out.println(bytes[i]);
            }
            if (bits.charAt(8 * i + 7) == '1') {
                bytes[i] = (byte) (-bytes[i]);
            }
        }
        return bytes;
    }

    public static String element2String(Element e){
        byte[] bytes = e.toBytes();
        StringBuilder sb = new StringBuilder("0");
        for (int i = 0; i < bytes.length; i++) {
            sb.append('@');
            sb.append(bytes[i]);
        }
        return sb.toString();
    }
    public static Element string2Element(String string,Field G1){
        String[] strings = string.split("@");
        byte[] bytes = new byte[strings.length];
        for (int i = 1; i < strings.length; i++) {
            bytes[i-1] = Byte.parseByte(strings[i]);
        }
        //System.out.println(Arrays.toString(bytes));
        return G1.newElementFromBytes(bytes);
    }
    public static String EncoderByMd5(String str) {
        try {
            //确定计算方法
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            //加密后的字符串
            String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
            return newstr;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String EncoderBySHA1(String str) {
        try {
            //确定计算方法
            MessageDigest md5 = MessageDigest.getInstance("SHA1");
            BASE64Encoder base64en = new BASE64Encoder();
            //加密后的字符串
            String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
            return newstr;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int pseudoRandomGenerator(int seed) {
        return (seed * 9301 + 49297) % 233280;
    }

    public static Element H1(String bits, BigInteger p, Field Zr) {//MD5算法加伪随机生成字符拼接得到目标长度比特串
        int log2p = log2(p);
        String md5 = EncoderByMd5(bits);
        StringBuilder sb = new StringBuilder(md5);
        String first = md5.substring(0, 1);
        byte[] md5Bytes = first.getBytes();
        int seed = (int) md5Bytes[0];
        int next;
        while (sb.length() * 8 < log2p) {
            next = pseudoRandomGenerator(seed) % md5.length();
            sb.append(md5.charAt(next));
        }
        String output = string2Bits(sb.toString());

        return Zr.newElement(new BigInteger(output.substring(0, log2p), 2));
    }

    public static Element H2(String bits, BigInteger p, Field Zr) {//MD5算法加伪随机生成字符拼接得到目标长度比特串
        int log2p = log2(p);
        String md5 = EncoderBySHA1(bits);
        StringBuilder sb = new StringBuilder(md5);
        String first = md5.substring(0, 1);
        byte[] md5Bytes = first.getBytes();
        int seed = (int) md5Bytes[0];
        int next;
        while (sb.length() * 8 < log2p) {
            next = pseudoRandomGenerator(seed) % md5.length();
            sb.append(md5.charAt(next));
        }
        String output = string2Bits(sb.toString());
        return Zr.newElement(new BigInteger(output.substring(0, log2p), 2));
        //return output.substring(0, log2p);
    }


    public static Element H3(String bits, Element[] B, String K, BigInteger p, Field Zr) {//MD5算法加伪随机生成字符拼接得到目标长度比特串
        StringBuilder sb0 = new StringBuilder(bits);
        for(int i = 1;i<B.length;i++){
            byte[] bytes = B[i].toBytes();
            sb0.append(new String(bytes));
        }
        int log2p = log2(p);
        String md5 = EncoderByMd5(bits + K);
        StringBuilder sb = new StringBuilder(md5);
        String first = md5.substring(0, 1);
        byte[] md5Bytes = first.getBytes();
        int seed = (int) md5Bytes[0];
        int next;
        while (sb.length() * 8 < log2p) {
            next = pseudoRandomGenerator(seed) % md5.length();
            sb.append(md5.charAt(next));
        }
        String output = string2Bits(sb.toString());
        return Zr.newElement(new BigInteger(output.substring(0, log2p), 2));
    }

    public static Element bits2Element(String bits,Field G){
        String[] strings = bits.split("@");
        byte[] bytes = new byte[strings.length];
        for (int i = 1; i < strings.length; i++) {
            bytes[i-1] = Byte.parseByte(strings[i]);
        }
        //System.out.println(Arrays.toString(bytes));
        return G.newElementFromBytes(bytes);
    }

    public static int log2(BigInteger r) {
        int log2r = 0;
        while (!r.equals(BigInteger.ZERO)) {
            r = r.shiftRight(1);
            log2r++;
        }
        return log2r;
    }

    public static String element2bits(Element e){
        return bytes2Bits(e.toBytes());
    }


    public static String exclusiveOr(String string1,String sk){
        int length = sk.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if(string1.charAt(i) == sk.charAt(i)){
                sb.append("1");
            }
            else{
                sb.append("0");
            }
        }
        String output = sb.toString();
        return output;
        //return Zr.newElement(new BigInteger(output, 2));
    }

    public static Point string2Point(String string, Field G1){
        Point p = (Point)G1.newOneElement();
        p.setFromBytesX(string.getBytes());
        return p;
    }

    public static String point2String(Point e){
        return new String(e.toBytesX());
    }

    public static Properties getProperties(String path) throws IOException {
        FileInputStream in = new FileInputStream(path);
        Properties properties = new Properties();
        properties.load(in);
        return properties;
    }
    public static String randomString(int length){
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = sources.charAt(new Random().nextInt(sources.length()));
        }
        return new String(text);
    }
    public static String parentDir(String path){
        return path.substring(0, path.lastIndexOf("/"));
    }

    public static String element2password(Element element){
        byte[] bytes = element.toBytes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            if(bytes[i]>=0){
                sb.append("1");
            }
            else{
                sb.append("0");
            }
        }
        return sb.toString();
    }
    public static void main(String[] args) {

        //BitsString 测试
        /*
        String a = "我去年买了个表";
        System.out.println("origin: " + a);
        String bits = Utils.string2Bits(a);
        System.out.println("Bits: " + bits);
        String origin = Utils.bits2String(bits);
        System.out.println("back: " + origin);*/

        /*  log2 md5 测试
        String b = "010101111001111111001";
        System.out.println(Utils.log2(new BigInteger("730750818665534535851578973600197997769233793023")));
        System.out.println(Utils.EncoderByMd5(b));
        System.out.println(Utils.EncoderByMd5(b).length());
        System.out.println(Utils.StringToBits(Utils.EncoderByMd5(b)));
        System.out.println(Utils.StringToBits(Utils.EncoderByMd5(b)).length());
        StringBuilder c = new StringBuilder(b);
        System.out.println(c.append(123));*/

        /*
        String c = "asdasdasdasdasda";
        BigInteger d = new BigInteger("7307508186655345358515789736001979977");
        System.out.println(log2(d));
        System.out.println(H1(string2Bits(c),d));
        System.out.println(H1(string2Bits(c),d).length());
        System.out.println(H2(string2Bits(c),d));
        System.out.println(H2(string2Bits(c),d).length());*/


        /*BigInteger bigInteger = new BigInteger("101010", 2);//字符串按二进制读取
        System.out.println(bigInteger);*/


        //point测试
        /*
        Pairing pairing = PairingFactory.getPairing(  "c.properties");
        Field G1 = pairing.getG1();
        Element e = G1.newRandomElement();
        System.out.println("e" + e);
        String c = element2bits(e);
        System.out.println(c);
        System.out.println(Arrays.toString(e.toBytes()));
        System.out.println(bits2Element(c, G1));*/

        /*
        byte c = 12;
        String a = String.valueOf(c);
        byte d = Byte.parseByte(a);
        System.out.println(a);
        System.out.println(d);*/

        String string = "123456";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char ch = (char)(Integer.parseInt(string.substring(i,i+1 )) + 'A');
            sb.append(ch);
        }
        System.out.println(sb);

    }


}
