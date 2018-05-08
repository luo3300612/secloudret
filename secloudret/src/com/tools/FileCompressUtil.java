package com.tools;



import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileCompressUtil {
    public static void mutileFileToGzip(String zipFilePath, List<String> fileList) {
        File file = new File(zipFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            ArrayList<File> files = new ArrayList<File>();
            for (String fileName : fileList) {
                files.add(new File(fileName));
            }
            for (int i = 0; i < files.size(); i++) {
                FileInputStream fis = new FileInputStream(files.get(i));
                zos.putNextEntry(new ZipEntry(files.get(i).getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException ioe) {
        }
    }

    public static void unzip(String filePath,String outputPath) {
        File source = new File(filePath);
        if (source.exists()) {
            ZipInputStream zis = null;
            BufferedOutputStream bos = null;
            try {
                zis = new ZipInputStream(new FileInputStream(source));
                ZipEntry entry = null;
                while ((entry = zis.getNextEntry()) != null
                        && !entry.isDirectory()) {
                    String destFile = outputPath + "/" + entry.getName();
                    File target = new File(destFile);
                    if (!target.getParentFile().exists()) {
                        // 创建文件父目录
                        target.getParentFile().mkdirs();
                    }
                    // 写入文件
                    bos = new BufferedOutputStream(new FileOutputStream(target));
                    int read = 0;
                    byte[] buffer = new byte[1024 * 10];
                    while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.flush();
                }
                zis.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        //unzip("/home/luo3300612/Desktop/test/file/1.zip","/home/luo3300612/Desktop/test/file/re");
        List<String> filelist = new ArrayList<>();
        String sharePath = "/home/luo3300612/IdeaProjects/secloudret/out/artifacts/secloudret_Web_exploded/all/luo3300612/shares/16.properties";
        String filePath = "/home/luo3300612/IdeaProjects/secloudret/out/artifacts/secloudret_Web_exploded/all/luo3300612/shares/16.se";
        filelist.add(sharePath);
        filelist.add(filePath);
        System.out.println("SharePath:" + sharePath);
        System.out.println("FilePath:" + filePath);
        System.out.println("filelist：" +filelist);
        FileCompressUtil.mutileFileToGzip("/home/luo3300612/IdeaProjects/secloudret/out/artifacts/secloudret_Web_exploded/all/luo3300612/others/" + "16.zip", filelist);

    }
}
