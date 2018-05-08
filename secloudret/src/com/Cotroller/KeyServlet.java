package com.Cotroller;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;

public class KeyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        process(req,resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html;charset=UTF-8");
        Part pro = req.getPart("pro");
        Part pub = req.getPart("pub");
        HttpSession session = req.getSession();
        String username = (session.getAttribute("account")).toString();
        saveUpload(pro,username);
        saveUpload(pub,username);
        resp.sendRedirect("/front/key_upload_success.jsp");
    }

    private void saveUpload(Part part,String username) {
        String fileName = getFileName(part);


        ServletContext cxt = getServletContext();
        String realPath = cxt.getRealPath("/");
        System.out.println(realPath);
        File file = new File(realPath+"all");
        if(!file.exists()){
            System.out.println("创建文件夹");
            boolean a = file.mkdir();
            System.out.println("创建结果：" + a);
        }
        File userfile = new File(realPath + "all/" + username);
        if(!userfile.exists()){
            System.out.println("创建文件夹");
            boolean a = userfile.mkdir();
            System.out.println("创建结果：" + a);
        }
        File keyfile = new File(realPath + "all/" + username+"/key");
        if(!keyfile.exists()){
            System.out.println("创建文件夹");
            boolean a = keyfile.mkdir();
            System.out.println("创建结果：" + a);
        }
        File upFile = new File(keyfile,fileName);
        FileOutputStream fos = null;
        InputStream is = null;
        byte[] buf = new byte[1024];
        int flag = -1;
        try{
            fos = new FileOutputStream(upFile);
            is = part.getInputStream();
            while((flag = is.read(buf,0,1024))!=-1){
                fos.write(buf,0,flag);
            }
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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