package com.utils;

import java.io.*;

public class CopyFile {
    public CopyFile() {
    }

    /**
     * 新建目录
     *
     * @param folderPath String  如  c:/fqf
     * @return boolean
     */
    public void newFolder(String folderPath) {
        try {
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.mkdir();
            }
        } catch (Exception e) {
            System.out.println("新建目录操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 新建文件
     *
     * @param filePathAndName String  文件路径及名称  如c:/fqf.txt
     * @param fileContent     String  文件内容
     * @return boolean
     */
    public void newFile(String filePathAndName, String fileContent) {

        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();  //取的路径及文件名
            File myFilePath = new File(filePath);
            /**如果文件不存在就建一个新文件*/
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            FileWriter resultFile = new FileWriter(myFilePath);  //用来写入字符文件的便捷类, 在给出 File 对象的情况下构造一个 FileWriter 对象
            PrintWriter myFile = new PrintWriter(resultFile);  //向文本输出流打印对象的格式化表示形式,使用指定文件创建不具有自动行刷新的新 PrintWriter。
            String strContent = fileContent;
            myFile.println(strContent);
            resultFile.close();

        } catch (Exception e) {
            System.out.println("新建文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 删除文件
     *
     * @param filePathAndName String  文件路径及名称  如c:/fqf.txt
     * @param fileContent     String
     * @return boolean
     */
    public void delFile(String filePathAndName) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myDelFile = new File(filePath);
            myDelFile.delete();

        } catch (Exception e) {
            System.out.println("删除文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 删除文件夹
     *
     * @param filePathAndName String  文件夹路径及名称  如c:/fqf
     * @param fileContent     String
     * @return boolean
     */
    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);  //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();  //删除空文件夹

        } catch (Exception e) {
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path String  文件夹路径  如  c:/fqf
     */
    public void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
            }
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String  原文件路径  如：c:/fqf.txt
     * @param newPath String  复制后路径  如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        try {
//           int  bytesum  =  0;  
            File oldfile = new File(oldPath);
            System.out.println("old file name is " + oldfile.getName());
            System.out.println(oldPath);
            if (oldfile.exists()) {  //文件存在时
                InputStream inStream = new FileInputStream(oldPath);  //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                System.out.println("new path is " + newPath);
                byte[] buffer = new byte[1024];
                int  len=0;
                while ((len = inStream.read(buffer)) >0) {
//                   bytesum  +=  byteread;  //字节数  文件大小  
//                   System.out.println(bytesum);  
                    fs.write(buffer, 0, len);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String  原文件路径  如：c:/fqf
     * @param newPath String  复制后路径  如：f:/fqf/ff
     * @return boolean
     */
    public void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs();  //如果文件夹不存在  则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 移动文件到指定目录
     *
     * @param oldPath String  如：c:/fqf.txt
     * @param newPath String  如：d:/fqf.txt
     */
    public void moveFile(String oldPath, String newPath) {
        copyFile(oldPath, newPath);
        delFile(oldPath);

    }

    /**
     * 移动文件到指定目录
     *
     * @param oldPath String  如：c:/fqf.txt
     * @param newPath String  如：d:/fqf.txt
     */
    public void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);

    }

    public static void main(String[] args) {
        CopyFile cp = new CopyFile();
        //cp.delFolder("/home/luo3300612/IdeaProjects/SE/out/artifacts/SE_war_exploded/WEB-INF/upload/download");
        cp.newFolder("/home/luo3300612/IdeaProjects/SE/out/artifacts/SE_war_exploded/WEB-INF/upload/download");
        cp.copyFile("/home/luo3300612/IdeaProjects/SE/out/artifacts/SE_war_exploded/WEB-INF/upload/感知器.cpp",
                "/home/luo3300612/IdeaProjects/SE/out/artifacts/SE_war_exploded/WEB-INF/upload/download/感知器.cpp");
    }

    // 拷贝文件
    private void copyFile2(String source, String dest) {
        try {
            File in = new File(source);
            File out = new File(dest);
            FileInputStream inFile = new FileInputStream(in);
            FileOutputStream outFile = new FileOutputStream(out);
            byte[] buffer = new byte[10240];
            int i = 0;
            while ((i = inFile.read(buffer)) != -1) {
                outFile.write(buffer, 0, i);
            }//end while
            inFile.close();
            outFile.close();
        }//end try
        catch (Exception e) {

        }//end catch
    }//end copyFile



}