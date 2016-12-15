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
     * 将指定的内容fileContent保存到指定的文件filePath
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
            bw.flush();// 将数据更新至文件
        } finally {
            if (bw != null)
                bw.close();
            if (fw != null)
                fw.close();// 关闭文件流
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
     * 读取指定的文件，是否按原文件内容格式返回
     * 
     * @param path
     * @param keepOriginalFormat
     *            true:按原文件内容格式返回；false:不需要
     * @return
     */
    public static String readfile(String path, boolean keepOriginalFormat) {
        if (keepOriginalFormat == false)
            return readfile(path);
        StringBuffer str = new StringBuffer("");
        try {
            FileReader fr = new FileReader(path);// 建立FileReader对象，并实例化为fr
            // 关键在于读取过程中，要判断所读取的字符是否已经到了文件的末尾，
            // 并且这个字符是不是文件中的断行符，即判断该字符值是否为13。
            int c = fr.read();// 从文件中读取一个字符
            // 判断是否已读到文件结尾
            while (c != -1) {
                str.append((char) c);
                c = fr.read();// 从文件中继续读取数据
                if (c == 13) {// 判断是否为断行字符
                    fr.skip(1);// 略过一个字符
                }
            }
            fr.close();
        } catch (Exception d) {
            System.out.println(d.getMessage());
            return "";
        }
        return str.toString();
    }

    public static String readfile(String path) // 从文本文件中读取内容
    {
        BufferedReader bufread;
        String filepath, read;
        String readStr = "";
        try {
            filepath = path; // 得到文本文件的路径
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
        return readStr; // 返回从文本文件中读取内容
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
            filepath = path; // 得到文本文件的路径
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
