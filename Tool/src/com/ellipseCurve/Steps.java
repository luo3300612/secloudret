package com.ellipseCurve;

import com.utils.*;
import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import sun.nio.cs.ext.GB18030;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static javafx.scene.input.KeyCode.M;

public class Steps {
    public static void createProperties(int rBits, int qBits, String outputPath) throws IOException {
        Properties properties = new Properties();
        PairingParametersGenerator pg = new TypeACurveGenerator(rBits, qBits);
        PairingParameters curveParams = pg.generate();
        String[] pro = curveParams.toString().split("[ \n]");
        for (int i = 0; i < pro.length; i += 2) {
            properties.setProperty(pro[i], pro[i + 1]);
        }
        properties.store(new FileOutputStream(outputPath + "/a.properties"), "文件自述");
    }

    public static Boolean keyGen(String outputPath) {
        Properties pub_properties = new Properties();
        Properties pri_properties = new Properties();
        try {
            createProperties(160, 512, outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pairing pairing = PairingFactory.getPairing(outputPath + "/a.properties");
        Field G1 = pairing.getG1();// G1 × G2 -> GT
        Field GT = pairing.getGT();
        Field Zr = pairing.getZr();
        int m = 5; // m keyword的个数
        /*
        String[] W = new String[m+1];
        W[1] = "hello";
        W[2] = "boy";
        W[3] = "girl";
        W[4] = "fuck";
        W[5] = "cat";*/
        Element[] s = new Element[m + 3]; // 索引从1开始 Apriv
        Element P1 = G1.newRandomElement(); // 素数阶群随机元即生成元
        Element P2 = G1.newRandomElement();
        Element[] Y = new Element[m + 3];
        for (int i = 1; i <= m + 2; i++) {
            s[i] = Zr.newRandomElement();
            Y[i] = (P1.duplicate()).mulZn(s[i]);
            //System.out.println("KeyGen:Y"+i+":" + Y[i]);
            //System.out.println("toString:" + Utils.element2String(Y[i]));
            pri_properties.setProperty("s" + i, Utils.element2String(s[i]));
            pub_properties.setProperty("Y" + i, Utils.element2String(Y[i]));
        }
        Element g = pairing.pairing(P1, P1);
        Element h = pairing.pairing(P1, P2);
        pub_properties.setProperty("P1", Utils.element2String(P1));
        pub_properties.setProperty("P2", Utils.element2String(P2));
        pub_properties.setProperty("g", Utils.element2String(g));
        pub_properties.setProperty("h", Utils.element2String(h));
        Properties filerecord = new Properties();
        filerecord.setProperty("num", "0");


        CopyFile cp = new CopyFile();
        cp.copyFile("c.properties", outputPath + "/b.properties");
        Properties b = null;
        try {
            b = Utils.getProperties(outputPath + "/b.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pairing bpairing = PairingFactory.getPairing(outputPath + "/b.properties");
        Field bZr = bpairing.getZr();
        Field bG1 = bpairing.getG1();
        Element G = Utils.string2Element(b.getProperty("G"), bG1);
        Element n = bZr.newRandomElement();
        Element P = G.duplicate().mulZn(n);
        pub_properties.setProperty("pub", Utils.element2String(P));
        pri_properties.setProperty("pri", Utils.element2String(n));


        //对等检测 x=13128939691011036566842961124270932141331812670940832872529756504245000252505586185071534341
        try {
            pub_properties.store(new FileOutputStream(outputPath + "/pub.properties"), "文件自述");
            pri_properties.store(new FileOutputStream(outputPath + "/pri.properties"), "文件自述");
            filerecord.store(new FileOutputStream(outputPath + "/files.properties"), "文件自述");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public static boolean Encrypt(String keyPath, String[] W, String outputPath, String password, String filename) throws IOException {
        Properties cipherText = new Properties();

        int m = 5;
        Pairing pairing = PairingFactory.getPairing(keyPath + "/a.properties");
        Field G1 = pairing.getG1();
        Field GT = pairing.getGT();
        Field Zr = pairing.getZr();

        Properties pub = Utils.getProperties(keyPath + "/pub.properties");
        Element[] Y = new Element[m + 3];
        for (int i = 1; i <= m + 2; i++) {
            //System.out.println("From String:" + pub.getProperty("Y" + i));
            Y[i] = Utils.string2Element(pub.getProperty("Y" + i), G1);
            //System.out.println("Y" + i+":" + Y[i]);
        }
        Element P1 = Utils.string2Element(pub.getProperty("P1"), G1);
        Element g = Utils.string2Element(pub.getProperty("g"), GT);
        Element h = Utils.string2Element(pub.getProperty("h"), GT);

        Element r[] = new Element[m + 1];
        Element B[] = new Element[m + 1];
        for (int i = 1; i <= m; i++) {
            r[i] = Zr.newRandomElement();
            B[i] = (Y[m + 1].duplicate()).mulZn(r[i]);
            System.out.println("B" + i + ":" + B[i]);
            cipherText.setProperty("B" + i, Utils.element2String(B[i]));
        }
        BigInteger p = Zr.getOrder();
        r[0] = Utils.H3(Utils.string2Bits(filename), B, password, p, Zr);
        //System.out.println("r[0]:" + r[0]);
        //r[0] = Utils.H3()
        Element[] A = new Element[m + 1];
        Element left;
        for (int i = 1; i <= m; i++) {
            left = (P1.duplicate()).mulZn(r[i]);
            A[i] = ((Y[i].duplicate()).add((P1.duplicate()).mulZn(Utils.H1(Utils.string2Bits(W[i]), p, Zr))));
            A[i] = ((A[i].duplicate()).mulZn(r[0])).add(left);
            cipherText.setProperty("A" + i, Utils.element2String(A[i]));
            System.out.println("A" + i + ":" + A[i]);
        }
        //System.out.println("A1+A2:" + (A[1].duplicate()).add(A[2]));
        Element K = (Y[m + 2].duplicate()).mulZn(r[0]);
        System.out.println("K:" + K);
        Element S = Utils.H2(Utils.element2bits((g.duplicate()).mulZn(r[0])), p, Zr);
        String R = Utils.exclusiveOr(Utils.element2bits(Utils.H2(Utils.element2bits((h.duplicate()).mulZn(r[0])), p, Zr)), Utils.string2Bits(password));
        cipherText.setProperty("mode", "S");
        cipherText.setProperty("K", Utils.element2String(K));
        System.out.println("S:" + S);
        cipherText.setProperty("S", Utils.element2String(S));
        cipherText.setProperty("R", R);
        //System.out.println("En:" + outputPath + "/" + filename + ".properties");
        cipherText.store(new FileOutputStream(outputPath + "/" + filename + ".properties"), "文件自述");
        return true;
    }


    public static boolean Encrypt(String keyPath, String[] W, String outputPath, String password, String filename, String pubPath, String originname,Element PASSWORD) throws IOException {
        Properties cipherText = new Properties();

        int m = 5;


        Pairing pairing = PairingFactory.getPairing(keyPath + "/a.properties");
        Field G1 = pairing.getG1();
        Field GT = pairing.getGT();
        Field Zr = pairing.getZr();

        Properties pub = Utils.getProperties(keyPath + "/pub.properties");
        Element[] Y = new Element[m + 3];
        for (int i = 1; i <= m + 2; i++) {
            //System.out.println("From String:" + pub.getProperty("Y" + i));
            Y[i] = Utils.string2Element(pub.getProperty("Y" + i), G1);
            //System.out.println("Y" + i+":" + Y[i]);
        }
        Element P1 = Utils.string2Element(pub.getProperty("P1"), G1);
        Element g = Utils.string2Element(pub.getProperty("g"), GT);
        Element h = Utils.string2Element(pub.getProperty("h"), GT);

        Element r[] = new Element[m + 1];
        Element B[] = new Element[m + 1];
        for (int i = 1; i <= m; i++) {
            r[i] = Zr.newRandomElement();
            B[i] = (Y[m + 1].duplicate()).mulZn(r[i]);
            System.out.println("B" + i + ":" + B[i]);
            cipherText.setProperty("B" + i, Utils.element2String(B[i]));
        }
        BigInteger p = Zr.getOrder();
        r[0] = Utils.H3(Utils.string2Bits(filename), B, password, p, Zr);
        //System.out.println("r[0]:" + r[0]);
        //r[0] = Utils.H3()
        Element[] A = new Element[m + 1];
        Element left;
        for (int i = 1; i <= m; i++) {
            left = (P1.duplicate()).mulZn(r[i]);
            A[i] = ((Y[i].duplicate()).add((P1.duplicate()).mulZn(Utils.H1(Utils.string2Bits(W[i]), p, Zr))));
            A[i] = ((A[i].duplicate()).mulZn(r[0])).add(left);
            cipherText.setProperty("A" + i, Utils.element2String(A[i]));
            System.out.println("A" + i + ":" + A[i]);
        }
        //System.out.println("A1+A2:" + (A[1].duplicate()).add(A[2]));
        Element K = (Y[m + 2].duplicate()).mulZn(r[0]);
        System.out.println("K:" + K);
        Element S = Utils.H2(Utils.element2bits((g.duplicate()).mulZn(r[0])), p, Zr);


        Pairing bpairing = PairingFactory.getPairing(keyPath + "/b.properties");
        Field bG1 = bpairing.getG1();
        Properties pubs = Utils.getProperties(pubPath);
        cipherText.setProperty("originname", originname);
        int user_num = Integer.parseInt(pubs.getProperty("user_num"));
        for (int i = 1; i <= user_num; i++) {
            String username = pubs.getProperty("u" + i);
            Element pubkey = Utils.string2Element(pubs.getProperty("key" + i), bG1);
            System.out.println("PASSWORD:" + PASSWORD);
            Element[] encryptedPassword = EllipseEncryptUtils.stringEncrypt(PASSWORD, pubkey, keyPath);
            System.out.println("ECm1:" + encryptedPassword[1]);
            System.out.println("ECm2:" + encryptedPassword[2]);
            cipherText.setProperty(username + "1", Utils.element2String(encryptedPassword[1]));
            cipherText.setProperty(username + "2", Utils.element2String(encryptedPassword[2]));
        }

        cipherText.setProperty("mode", "D");
        cipherText.setProperty("K", Utils.element2String(K));
        System.out.println("S:" + S);
        cipherText.setProperty("S", Utils.element2String(S));
        //System.out.println("En:" + outputPath + "/" + filename + ".properties");
        cipherText.store(new FileOutputStream(outputPath + "/" + filename + ".properties"), "文件自述");
        return true;
    }

    public static boolean wholeEncrypt(String filePath, String outputPath, String keyPath, String[] keywords, String pubPath) {
        String[] strings = filePath.split("/");
        for (int i = 1; i <= 5; i++) {
            if (keywords[i].length() == 0) {
                //System.out.println("null at " + i);
                keywords[i] = "空词" + i;
            }
        }
        String originname = strings[strings.length - 1];
        Pairing bpairing = PairingFactory.getPairing(keyPath + "/b.properties");
        Element PASSWORD = bpairing.getG1().newRandomElement();
        String password = Utils.element2password(PASSWORD);
        System.out.println("password:" + password);
        Properties filerecord;
        String filename;
        try {
            filerecord = Utils.getProperties(keyPath + "/files.properties");
            int filenum = Integer.parseInt(filerecord.getProperty("num"));
            int num = filenum + 1;
            filename = String.valueOf(num);
            filerecord.setProperty("num", String.valueOf(num));
            filerecord.setProperty("sk" + num, password);
            for (int i = 1; i <= 5; i++) {
                filerecord.setProperty("word" + num + i, keywords[i]);
            }
            filerecord.setProperty("name" + num, originname);
            filerecord.store(new FileOutputStream(keyPath + "/files.properties"), "文件自述");
            FileCipherUtil.encrypt(filePath, outputPath + "/" + filename + ".se", password);
            Encrypt(keyPath, keywords, outputPath, password, filename, pubPath, originname,PASSWORD);
        } catch (GeneralSecurityException | IOException e1) {
            e1.printStackTrace();
            return false;
        }
        CopyFile cp = new CopyFile();
        String PP = pubPath.substring(0, pubPath.lastIndexOf("/")) + "/pubs0.properties";
        cp.copyFile(pubPath, PP);
        List<String> filelist = new ArrayList<>();
        filelist.add(outputPath + "/" + filename + ".se");
        filelist.add(outputPath + "/" + filename + ".properties");
        filelist.add(PP);
        FileCompressUtil.mutileFileToGzip(outputPath + "/" + filename + ".zip", filelist);
        FIleUtils.delete(outputPath + "/" + filename + ".se");
        FIleUtils.delete(outputPath + "/" + filename + ".properties");
        return true;
    }

    public static boolean wholeEncrypt(String filePath, String outputPath, String keyPath, String[] keywords) {
        String[] strings = filePath.split("/");
        for (int i = 1; i <= 5; i++) {
            if (keywords[i].length() == 0) {
                //System.out.println("null at " + i);
                keywords[i] = "空词" + i;
            }
        }
        String originname = strings[strings.length - 1];
        String password = Utils.randomString(20);
        Properties filerecord;
        String filename;
        try {
            filerecord = Utils.getProperties(keyPath + "/files.properties");
            int filenum = Integer.parseInt(filerecord.getProperty("num"));
            int num = filenum + 1;
            filename = String.valueOf(num);
            filerecord.setProperty("num", String.valueOf(num));
            filerecord.setProperty("sk" + num, password);
            for (int i = 1; i <= 5; i++) {
                filerecord.setProperty("word" + num + i, keywords[i]);
            }
            filerecord.setProperty("name" + num, originname);
            filerecord.store(new FileOutputStream(keyPath + "/files.properties"), "文件自述");
            FileCipherUtil.encrypt(filePath, outputPath + "/" + filename + ".se", password);
            Encrypt(keyPath, keywords, outputPath, password, filename);
        } catch (GeneralSecurityException | IOException e1) {
            e1.printStackTrace();
            return false;
        }
        List<String> filelist = new ArrayList<>();
        filelist.add(outputPath + "/" + filename + ".se");
        filelist.add(outputPath + "/" + filename + ".properties");
        FileCompressUtil.mutileFileToGzip(outputPath + "/" + filename + ".zip", filelist);
        FIleUtils.delete(outputPath + "/" + filename + ".se");
        FIleUtils.delete(outputPath + "/" + filename + ".properties");
        return true;
    }

    public static boolean wholeDecrypt(String filepath, String outputPath, String keyPath) throws IOException, GeneralSecurityException {
        FileCompressUtil.unzip(filepath, outputPath + "/temp");
        ArrayList<String> fileNames = new ArrayList<>();
        FIleUtils.getAllFileName(outputPath + "/temp", fileNames);
        int mode = 0;
        for (int i = 0; i < fileNames.size(); i++) {
            String fileName = fileNames.get(i);
            if (fileName.contains(".properties")) {
                mode = 1;
                break;
            }
        }
        System.out.println("mode=" + mode);
        if (mode == 0) {
            Properties fileindex = Utils.getProperties(keyPath + "/files.properties");
            for (int i = 0; i < fileNames.size(); i++) {
                String fileName = fileNames.get(i);
                int filenum = Integer.parseInt(fileName.substring(0, fileName.lastIndexOf(".")));
                //System.out.println("filenum:" + filenum);
                //System.out.println("filename :" + fileName);
                FileCipherUtil.decrypt(outputPath + "/temp/" + fileName, outputPath + "/" + fileindex.getProperty("name" + filenum), fileindex.getProperty("sk" + filenum));
                //System.out.println(outputPath + "/" + fileindex.getProperty("name" + filenum));
            }
            FIleUtils.deleteDirectory(outputPath + "/temp");
            return true;
        } else {
            for (int i = 0; i < fileNames.size(); i++) {
                String fileName = fileNames.get(i);
                if (fileName.contains(".se")) {
                    String notFrom = fileName.substring(0, fileName.lastIndexOf("."));
                    Properties ciphertext = Utils.getProperties(outputPath + "/temp/" + notFrom + ".properties");
                    Pairing bpairing = PairingFactory.getPairing(keyPath + "/b.properties");
                    Element[] Cm = new Element[3];
                    Cm[1] = Utils.string2Element(ciphertext.getProperty("Cm1"), bpairing.getG1());
                    Cm[2] = Utils.string2Element(ciphertext.getProperty("Cm2"), bpairing.getG1());
                    String filename = ciphertext.getProperty("originname");
                    System.out.println("DCm1:" + Cm[1]);
                    System.out.println("DCm2:" + Cm[2]);
                    Element PASSWORD = EllipseEncryptUtils.stringDecrypt(Cm, keyPath);
                    System.out.println("PASSWORD:" + PASSWORD);
                    String password = Utils.element2password(PASSWORD);
                    System.out.println("password:" + password);
                    System.out.println("tode:"+outputPath + "/temp/" + fileName);
                    FileCipherUtil.decrypt(outputPath + "/temp/" + fileName, outputPath + "/" + filename, password);
                }
            }
            FIleUtils.deleteDirectory(outputPath + "/temp");
            return true;
        }

    }

    public static boolean Trapdoor(String keyPath, String[] omega, int[] I, int num, int mode, String outputPath) throws IOException {
        int t = I.length - 1;
        int m = 5;
        Pairing pairing = PairingFactory.getPairing(keyPath + "/a.properties");
        Properties pri = Utils.getProperties(keyPath + "/pri.properties");
        Properties pub = Utils.getProperties(keyPath + "/pub.properties");
        Element[] s = new Element[m + 3];
        Field Zr = pairing.getZr();
        Field G1 = pairing.getG1();
        BigInteger p = Zr.getOrder();
        Element P;
        if (mode == 1) {
            P = Utils.string2Element(pub.getProperty("P1"), G1);
        } else {
            P = Utils.string2Element(pub.getProperty("P2"), G1);
        }
        for (int i = 1; i <= m + 2; i++) {
            s[i] = Utils.string2Element(pri.getProperty("s" + i), Zr);
        }

        Element u = Zr.newRandomElement();
        Element[] T = new Element[4];
        T[1] = Zr.newElement(0);
        for (int i = 1; i <= t; i++) {
            T[1] = (T[1].duplicate()).add(s[I[i]]);
            T[1] = (T[1].duplicate()).add(Utils.H1(Utils.string2Bits(omega[i]), p, Zr));
        }
        T[1].add((s[m + 2].duplicate()).mulZn(u));
        T[1].invert();
        T[1] = (P.duplicate()).mulZn(T[1]);
        Element sm1 = s[m + 1].duplicate();
        T[2] = (T[1].duplicate()).mulZn((sm1.invert()));
        T[3] = u;
        Properties trapdoor = new Properties();
        if (mode == 1) {
            trapdoor.setProperty("mode", "S");
            trapdoor.setProperty("TS1", Utils.element2String(T[1]));
            trapdoor.setProperty("TS2", Utils.element2String(T[2]));
            trapdoor.setProperty("TS3", Utils.element2String(T[3]));
        } else {
            trapdoor.setProperty("mode", "D");
            trapdoor.setProperty("TS1", Utils.element2String(T[1]));
            trapdoor.setProperty("TS2", Utils.element2String(T[2]));
            trapdoor.setProperty("TS3", Utils.element2String(T[3]));
        }
        trapdoor.setProperty("t", String.valueOf(t));
        for (int i = 1; i <= t; i++) {
            trapdoor.setProperty("I" + i, String.valueOf(I[i]));
        }
        trapdoor.store(new FileOutputStream(outputPath + "/" + num + ".properties"), "文件自述");

        return true;
    }

    public static int[] beforeSearch(String[] keywords, String[] notwords, String keyPath) throws IOException {
        Properties files = Utils.getProperties(keyPath + "/files.properties");
        int num = Integer.parseInt(files.getProperty("num"));
        ArrayList<String> filewords = new ArrayList<>();
        int[] idRecord = new int[num + 1];
        idRecord[0] = 0;
        for (int i = 1; i <= num; i++) {
            filewords.clear();
            int flag = 1;
            for (int j = 1; j <= 5; j++) {
                filewords.add(files.getProperty("word" + i + j));
            }
            for (int j = 1; j < keywords.length; j++) {
                if (filewords.indexOf(keywords[j]) == -1) {
                    flag = 0;
                    break;
                }
            }
            for (int j = 1; j < notwords.length; j++) {
                if (filewords.indexOf(notwords[j]) != -1) {
                    flag = 0;
                    break;
                }
            }
            if (flag == 1) {
                idRecord[0] += 1;
                idRecord[idRecord[0]] = i;
            }
        }
        return idRecord;
    }


    public static void main(String[] args) throws IOException {
        /*
        try{
            Steps.createProperties(160,512,System.getProperty("user.dir"));
        }
        catch(IOException e){
            System.out.println(e);
        }
        Pairing pairing = PairingFactory.getPairing("c.properties");
        System.out.println(pairing);
        Field G1 = pairing.getG1();
        Element e = G1.newRandomElement();
        System.out.println("e " + e);*/


        //存取的元素是否一致～OK
        /*
        Pairing pairing = PairingFactory.getPairing("/home/luo3300612/Desktop/a.properties");
        FileInputStream in = new FileInputStream("/home/luo3300612/Desktop/pub.properties");
        Properties properties = new Properties();
        properties.load(in);
        Field GT = pairing.getGT();
        System.out.println(Utils.string2Element(properties.getProperty("g"), GT));*/

        String[] keywords = {"e", "f"};
        String[] notwords = {"a", "g"};
        String keyPath = "/home/luo3300612/Desktop/test";
        //预搜索Test
        int[] re = beforeSearch(keywords, notwords, keyPath);
        System.out.println(Arrays.toString(re));
    }
}