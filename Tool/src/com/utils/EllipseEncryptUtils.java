package com.utils;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import sun.nio.cs.ext.GB18030;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class EllipseEncryptUtils {
    public static Element[] stringEncrypt(Element P, Element PB, String keyPath) throws IOException {
        Pairing pairing = PairingFactory.getPairing(keyPath + "/b.properties");
        Field G1 = pairing.getG1();
        Field Zr= pairing.getZr();
        Properties b = Utils.getProperties(keyPath + "/b.properties");
        Element G = Utils.string2Element(b.getProperty("G"), G1);
        Element[] ciphertext = new Element[3];
        Element k = Zr.newRandomElement();
        ciphertext[1] = G.duplicate().mulZn(k);
        ciphertext[2] = P.duplicate().add(PB.duplicate().mulZn(k));
        return ciphertext;
    }

    public static Element stringDecrypt(Element[] ciphertext,String keyPath){
        Properties pri = null;
        try {
            pri = Utils.getProperties(keyPath + "/pri.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pairing pairing = PairingFactory.getPairing(keyPath +"/b.properties");
        Field Zr = pairing.getZr();

        Element n = Utils.string2Element(pri.getProperty("pri"),Zr);
        Element P = ciphertext[2].duplicate().sub(ciphertext[1].duplicate().mulZn(n));
        return P;
    }

    public static void main(String[] args) throws IOException {
        Pairing pairing = PairingFactory.getPairing("c.properties");
        Properties p = Utils.getProperties("c.properties");
        Field G1 = pairing.getG1();
        p.setProperty("G", Utils.element2String(G1.newRandomElement()));
        p.store(new FileOutputStream( "c.properties"), "文件自述");
    }
}
