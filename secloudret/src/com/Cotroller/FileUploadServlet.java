package com.Cotroller;

import com.tools.CopyFile;
import com.tools.FIleUtils;
import com.tools.FileCompressUtil;
import com.tools.Utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileUploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
//        String name = req.getParameter("name");
//        String age = req.getParameter("age");
//        String file = req.getParameter("file");
//
//        resp.setContentType("text/html;charset=UTF-8");
//        resp.getWriter().write("name:" + name + "<br/>age:" + age + "<br/> file:"+ file);
        process(req,resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html;charset=UTF-8");
        Part file = req.getPart("file");
        HttpSession session = req.getSession();
        String username = (session.getAttribute("account")).toString();
        saveUpload(file,username);
        resp.getWriter().write("<h3>文件上传成功</h3>");
        resp.sendRedirect("/front/file_upload_success.jsp");
    }

    private void saveUpload(Part part,String username) throws IOException {
        String fileName = getFileName(part);
        ServletContext cxt = getServletContext();
        String realPath = cxt.getRealPath("/");
        System.out.println(realPath);
        File file = new File(realPath + "all");
        if (!file.exists()) {
            System.out.println("创建文件夹");
            boolean a = file.mkdir();
            System.out.println("创建结果：" + a);
        }
        File userfile = new File(realPath + "all/" + username);
        if (!userfile.exists()) {
            System.out.println("创建文件夹");
            boolean a = userfile.mkdir();
            System.out.println("创建结果：" + a);
        }
        File filefile = new File(realPath + "all/" + username + "/files");
        if (!filefile.exists()) {
            System.out.println("创建文件夹");
            boolean a = filefile.mkdir();
            System.out.println("创建结果：" + a);
        }
        File upFile = new File(filefile, fileName);
        FileOutputStream fos = null;
        InputStream is = null;
        byte[] buf = new byte[1024];
        int flag = -1;
        try {
            fos = new FileOutputStream(upFile);
            is = part.getInputStream();
            while ((flag = is.read(buf, 0, 1024)) != -1) {
                fos.write(buf, 0, flag);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String path = realPath + "all/" + username + "/files/" + fileName;
        String realName = fileName.substring(0, fileName.lastIndexOf("."));
        String parentPath = realPath + "all/" + username + "/files/temp";
        FileCompressUtil.unzip(path, parentPath);
        Properties p = Utils.getProperties(realPath + "all/" + username + "/files/temp/" + realName + ".properties");
        String mode = p.getProperty("mode");
        if (mode.equals("S")) {
            FileCompressUtil.unzip(path, realPath + "all/" + username + "/files");
            FIleUtils.delete(path);
            FIleUtils.delete(parentPath);
        } else {
            CopyFile cp = new CopyFile();
            cp.delFolder(realPath + "all/" + username + "/shares");
            cp.newFolder(realPath + "all/" + username + "/shares");
            FileCompressUtil.unzip(path, realPath + "all/" + username + "/shares");
            Properties pubs = Utils.getProperties(realPath + "all/" + username + "/shares/pubs0.properties");
            int user_num = Integer.parseInt(pubs.getProperty("user_num"));

            String noname = fileName.substring(0, fileName.lastIndexOf('.'));

            String sharePath = realPath + "all/" + username + "/shares/" + noname + ".properties";
            String filePath = realPath + "all/" + username + "/shares/" + noname + ".se";
            for (int i = 1; i <= user_num; i++) {
                String nowuser = pubs.getProperty("u" + i);
                Properties shareproperties = new Properties();
                Properties fileproperties = Utils.getProperties(realPath + "all/" + username + "/shares/" + noname + ".properties");
                shareproperties.setProperty("Cm1", fileproperties.getProperty(nowuser + "1"));
                shareproperties.setProperty("Cm2", fileproperties.getProperty(nowuser + "2"));
                shareproperties.setProperty("originname", fileproperties.getProperty("originname"));
                String Path = realPath + "all/" + nowuser + "/others";
                File dir = new File(Path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                cp.delFolder(realPath + "all/" + username + "/shares/temp");
                cp.newFolder(realPath + "all/" + username + "/shares/temp");

                shareproperties.store(new FileOutputStream(realPath + "all/" + username + "/shares/temp/" + noname + ".properties"), "文件自述");

                List<String> filelist = new ArrayList<>();
                filelist.add(filePath);
                filelist.add(realPath + "all/" + username + "/shares/temp/" + noname + ".properties");
                System.out.println("SharePath:" + sharePath);
                System.out.println("FilePath:" + filePath);
                System.out.println("filelist：" + filelist);
                FileCompressUtil.mutileFileToGzip(Path + "/" + System.nanoTime() + "_" + noname + ".zip", filelist);
            }
            FIleUtils.delete(path);
            FIleUtils.delete(parentPath);
            FIleUtils.delete(sharePath);
            FIleUtils.delete(filePath);
            FIleUtils.delete(realPath + "all/" + username + "/shares/temp");
        }
    }

    private String getFileName(Part part) {
//        Iterator<String> it = part.getHeaderNames().iterator();
//        while(it.hasNext()){
//            String name = it.next();
//            System.out.println(name + ":" + part.getHeader(name));
//        }
        String header = part.getHeader("content-disposition");
        String[] arr = header.split("filename=");
        //System.out.println(Arrays.toString(arr));
        return arr[arr.length-1].replace("\"","");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
