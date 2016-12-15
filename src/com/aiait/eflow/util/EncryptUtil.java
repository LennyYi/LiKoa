package com.aiait.eflow.util;

import java.io.File;
import java.io.FileInputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.aiait.framework.mvc.util.Global;

/**
 * 
 * @author asnpgj3 Robin Hou
 * @date 06/26/2007
 */

public class EncryptUtil {
    private SecretKey key;
    private Cipher cipher;

    public static final String KEY_FILE_NAME = "eflow.key";
    public static String defaultKeyFile;
    public String keyFile;

    // static{
    // System.out.println("----------------------:"+Global.CONFIG_FILE_FULL_PATH);
    // keyFileName = Global.CONFIG_FILE_FULL_PATH.substring(0,Global.CONFIG_FILE_FULL_PATH.lastIndexOf("\\")) +
    // keyFileName;
    // strFileName = "\\eflow.key";
    // }

    protected String getKeyFileName() {
        if (this.keyFile != null) {
            return this.keyFile;
        }
        if (defaultKeyFile != null) {
            return defaultKeyFile;
        }
        int index = -1;
        if ((index = Global.CONFIG_FILE_FULL_PATH.lastIndexOf("\\")) != -1) {
            defaultKeyFile = Global.CONFIG_FILE_FULL_PATH.substring(0, index + 1) + KEY_FILE_NAME;
            return defaultKeyFile;
        }
        if ((index = Global.CONFIG_FILE_FULL_PATH.lastIndexOf("/")) != -1) {
            defaultKeyFile = Global.CONFIG_FILE_FULL_PATH.substring(0, index + 1) + KEY_FILE_NAME;
            return defaultKeyFile;
        }
        return KEY_FILE_NAME;
    }

    /**
     * @param strPwd
     *            需要加密的字符串 DES加密<br>
     * @return 加密后的字符串<br>
     * @throws Exception
     * <br>
     */
    public String EncryptDES(String strPwd) throws Exception {
        try {
            File file = new File(this.getKeyFileName());
            long len = file.length();
            byte rawKey[] = new byte[(int) len];
            FileInputStream fin = new FileInputStream(file);
            int r = fin.read(rawKey);
            if (r != len) {
                throw new Exception("读取密钥文件有误");
            }
            DESKeySpec dks = new DESKeySpec(rawKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("des");
            key = keyFactory.generateSecret(dks);
            cipher = Cipher.getInstance("DES");

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

        byte[] data = strPwd.getBytes();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(data);
            String strTeturn = byte2hex(result);
            return strTeturn;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

    /**
     * 解密
     * 
     * @param strPwd
     *            需要解密的字符串 <br>
     * @return 解密后的字符串 <br>
     * @throws Exception
     * <br>
     */
    public String UnryptDES(String strPwd) throws Exception {
        try {
            File file = new File(this.getKeyFileName());
            long len = file.length();
            byte rawKey[] = new byte[(int) len];
            FileInputStream fin = new FileInputStream(file);
            int r = fin.read(rawKey);
            if (r != len) {
                throw new Exception("读取密钥文件有误");
            }
            DESKeySpec dks = new DESKeySpec(rawKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("des");
            key = keyFactory.generateSecret(dks);
            cipher = Cipher.getInstance("DES");

        } catch (Exception ex) {
            throw ex;
        }

        String str_Retrun = "";

        try {
            byte byteTemp[] = new byte[strPwd.length() / 2];

            for (int i = 0; i < strPwd.length() / 2; i++) {
                byteTemp[i] = (byte) ((Char2Byet(strPwd.charAt(2 * i)) * 0x10 + Char2Byet(strPwd.charAt(2 * i + 1))) & 0XFF);
            }

            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] original = cipher.doFinal(byteTemp);
            str_Retrun = new String(original);
            return str_Retrun;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 二进制转换为Stirng
     * 
     * @param b
     * @return
     */
    private String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = hs;
        }
        return hs.toUpperCase();
    }

    /**
     * 字符 0---9 ,abbcdef 转换为 0----f
     * 
     * @param c
     * @return
     */
    private byte Char2Byet(char c) {
        byte iReturn = 0;
        switch (c) {
        case '0':
            iReturn = 0x00;
            break;
        case '1':
            iReturn = 0x01;
            break;
        case '2':
            iReturn = 0x02;
            break;
        case '3':
            iReturn = 0x03;
            break;
        case '4':
            iReturn = 0x04;
            break;
        case '5':
            iReturn = 0x05;
            break;
        case '6':
            iReturn = 0x06;
            break;
        case '7':
            iReturn = 0x07;
            break;
        case '8':
            iReturn = 0x08;
            break;
        case '9':
            iReturn = 0x09;
            break;

        case 'A':
        case 'a':
            iReturn = 0x0a;
            break;
        case 'B':
        case 'b':
            iReturn = 0x0b;
            break;
        case 'C':
        case 'c':
            iReturn = 0x0c;
            break;
        case 'D':
        case 'd':
            iReturn = 0x0d;
            break;
        case 'E':
        case 'e':
            iReturn = 0x0e;
            break;
        case 'F':
        case 'f':
            iReturn = 0x0f;
            break;
        }
        return iReturn;
    }

    public static void main(String[] args) throws Exception {
        EncryptUtil encrypt = new EncryptUtil();
        String newPWD = encrypt.EncryptDES("Aiait@2007");
        System.out.println(newPWD);
        String oldPWD = encrypt.UnryptDES(newPWD);
        System.out.println(oldPWD);
        // String s = "/WEB-INF/sdkfa.xml";
        // System.out.println(s.substring(0,s.lastIndexOf("/"))+"/eflow.key");
    }
}
