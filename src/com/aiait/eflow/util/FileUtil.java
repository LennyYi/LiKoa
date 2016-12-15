package com.aiait.eflow.util;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import com.aiait.eflow.common.CommonName;
import com.aiait.eflow.common.helper.ParamConfigHelper;

public class FileUtil {

    public static String generateDestfile(String filename) {
        String tmpfilename = filename;
        String timestamp = "" + (new Date()).getTime();
        String filetrail = tmpfilename.substring(tmpfilename.indexOf("-"), tmpfilename.length());
        String Stringupfile = timestamp.concat(filetrail);
        return Stringupfile;
    }

    /**
     * ��ָ��������fileContent���浽ָ�����ļ�filePath
     * 
     * @param filePath
     * @param fileContent
     */
    public static void saveAs(String filePath, String fileContent) throws IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(filePath);
            bw = new BufferedWriter(fw);
            bw.write(fileContent);
            bw.flush();// �����ݸ������ļ�
        } finally {
            if (bw != null)
                bw.close();
            if (fw != null)
                fw.close();// �ر��ļ���
        }
    }

    public static void saveAs(File upFile, String filePath) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        createFilePath(filePath);
        try {
            fis = new FileInputStream(upFile);
            fos = new FileOutputStream(filePath);
            byte[] buffer = new byte[16384]; // 16K
            int bytes;
            while ((bytes = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytes);
            }
        } finally {
            if (fos != null)
                fos.close();
            if (fis != null)
                fis.close();
        }
    }

    public static void createFilePath(String filePath) {
        filePath = filePath.substring(0, filePath.lastIndexOf("/"));
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
    }

    /**
     * 
     * @param filePathAndName
     * @return true --- successful false --- fail
     */
    public static boolean deleteFile(String filePathAndName) {
        return new File(filePathAndName).delete();
    }

    /**
     * ��ȡָ�����ļ����Ƿ�ԭ�ļ����ݸ�ʽ����
     * 
     * @param path
     * @param keepOriginalFormat
     *            true:��ԭ�ļ����ݸ�ʽ���أ�false:����Ҫ
     * @return
     */
    public static String readfile(String path, boolean keepOriginalFormat) {
        if (keepOriginalFormat == false)
            return readfile(path);
        StringBuffer str = new StringBuffer("");
        try {
            FileReader fr = new FileReader(path);// ����FileReader���󣬲�ʵ����Ϊfr
            // �ؼ����ڶ�ȡ�����У�Ҫ�ж�����ȡ���ַ��Ƿ��Ѿ������ļ���ĩβ��
            // ��������ַ��ǲ����ļ��еĶ��з������жϸ��ַ�ֵ�Ƿ�Ϊ13��
            int c = fr.read();// ���ļ��ж�ȡһ���ַ�
            // �ж��Ƿ��Ѷ����ļ���β
            while (c != -1) {
                str.append((char) c);
                c = fr.read();// ���ļ��м�����ȡ����
                if (c == 13) {// �ж��Ƿ�Ϊ�����ַ�
                    fr.skip(1);// �Թ�һ���ַ�
                }
            }
            fr.close();
        } catch (Exception d) {
            System.out.println(d.getMessage());
            return "";
        }
        return str.toString();
    }

    public static String readfile(String path) // ���ı��ļ��ж�ȡ����
    {
        BufferedReader bufread;
        String filepath, read;
        String readStr = "";
        try {
            filepath = path; // �õ��ı��ļ���·��
            File file = new File(filepath);
            FileReader fileread = new FileReader(file);
            bufread = new BufferedReader(fileread);
            while ((read = bufread.readLine()) != null) {
                readStr = readStr + read;
            }
        } catch (Exception d) {
            System.out.println(d.getMessage());
            return "";
        }
        return readStr; // ���ش��ı��ļ��ж�ȡ����
    }

    public static String getCurrentTime() {

        return null;
    }

    public static Timestamp getNowTimestamp() {
        long curTime = System.currentTimeMillis();
        return new Timestamp(curTime);
    }

    public static String getdate_s() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar MyDate = Calendar.getInstance();
        MyDate.setTime(new java.util.Date());
        String adddate = df.format(MyDate.getTime()).substring(0, 14);
        return adddate;
    }

    public static File getUploadFile(String fileName) {
        String uploadDir = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_UPLOAD_DIR);
        if (uploadDir == null || "".equals(uploadDir.trim())) {
            throw new IllegalStateException("Parameter - upload_file_dir has not been set");
        }
        uploadDir = uploadDir.trim();
        String filePath = uploadDir + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        }

        // Get from upload file backup
        String uploadDirBak = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_UPLOAD_DIR_BAK);
        if (uploadDirBak == null || "".equals(uploadDirBak.trim())) {
            return file;
        }
        uploadDirBak = uploadDirBak.trim();
        filePath = uploadDirBak + fileName;
        File file2 = new File(filePath);
        if (file2.exists()) {
            return file2;
        }
        return file;
    }

    public static boolean deleteUploadFile(String fileName) {
        String uploadDir = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_UPLOAD_DIR);
        if (uploadDir == null || "".equals(uploadDir.trim())) {
            throw new IllegalStateException("Parameter - upload_file_dir has not been set");
        }
        uploadDir = uploadDir.trim();
        String filePath = uploadDir + fileName;
        if (new File(filePath).delete()) {
            return true;
        }

        // Delete from upload file backup
        String uploadDirBak = ParamConfigHelper.getInstance().getParamValue(CommonName.PARAM_UPLOAD_DIR_BAK);
        if (uploadDirBak == null || "".equals(uploadDirBak.trim())) {
            return false;
        }
        uploadDirBak = uploadDirBak.trim();
        filePath = uploadDirBak + fileName;
        return new File(filePath).delete();
    }

    public static void main(String[] args) throws Exception {
        BufferedReader bufread;
        String filepath, read;
        String readStr = "";
        String path = "c://2.txt";
        FileReader fileread = null;
        try {
            filepath = path; // �õ��ı��ļ���·��
            File file = new File(filepath);
            fileread = new FileReader(file);
            bufread = new BufferedReader(fileread);
            int row = 1;
            while ((read = bufread.readLine()) != null) {
                // readStr = readStr + read;
                System.out.println(read);
                String[] s = StringUtil.split(read, "||");
                for (int i = 0; i < s.length; i++) {
                    System.out.println("row=" + row + ",i:" + s[i] + ".");
                }
                row++;
            }
        } catch (Exception d) {
            System.out.println(d.getMessage());
        } finally {
            if (fileread != null)
                fileread.close();
        }
    }
}
