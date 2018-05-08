package com.Cotroller;

import com.tools.CopyFile;
import com.tools.FIleUtils;
import com.tools.FileCompressUtil;
import com.tools.Utils;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;

public class FileDownloadServlet extends HttpServlet {
    private ArrayList<String> downloadfiles;
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





        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        CopyFile cp = new CopyFile();
        ServletContext cxt = getServletContext();
        String realPath = cxt.getRealPath("/") + "all/" + session.getAttribute("account");
        String downloadPath = realPath + "/download";

        cp.delFolder(downloadPath);
        cp.newFolder(downloadPath);
        FileCompressUtil.mutileFileToGzip(downloadPath+"/result.zip", downloadfiles);

        //获取上传文件的目录
        System.out.println("上传目录 " + downloadPath);
        //存储要下载的文件名
        Map<String, String> fileNameMap = new HashMap<String, String>();
        //递归遍历filepath目录下的所有文件和目录，将文件的文件名存储到map集合中
        listfile(new File(downloadPath), fileNameMap);//File既可以代表一个文件也可以代表一个目录
        //将Map集合发送到listfile.jsp页面进行显示
        req.setAttribute("fileNameMap", fileNameMap);
        req.getRequestDispatcher("/front/listfile.jsp").forward(req, resp);
        //resp.sendRedirect("/front/file_upload_success.jsp");
    }

    private void saveUpload(Part part,String username) throws IOException {
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
        File filefile = new File(realPath + "all/" + username+"/temp");
        if(!filefile.exists()){
            System.out.println("创建文件夹");
            boolean a = filefile.mkdir();
            System.out.println("创建结果：" + a);
        }
        File upFile = new File(filefile,fileName);
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
        String filePath = realPath + "all/" + username + "/files";
        String path = realPath + "all/" + username + "/temp/" + fileName;
        String parentPath = realPath + "all/" + username + "/temp";
        String keyPath = realPath + "all/" + username + "/key";
        FileCompressUtil.unzip(path, parentPath);
        FIleUtils.delete(path);
        ArrayList<String> allFileName = new ArrayList<>();
        FIleUtils.getAllFileName(parentPath, allFileName);
        downloadfiles = new ArrayList();
        for (int i = 0; i < allFileName.size(); i++) {
            String turnfilename = allFileName.get(i);
            String doorPath = parentPath + "/" + turnfilename;
            wholeMatch(doorPath, keyPath, filePath);
        }
        FIleUtils.delete(parentPath);
    }
    private void wholeMatch(String doorPath,String keyPath,String filePath) throws IOException {
        Properties door = Utils.getProperties(doorPath);
        Pairing pairing = PairingFactory.getPairing(keyPath + "/a.properties");
        Field G1 = pairing.getG1();
        Field Zr = pairing.getZr();
        Field GT = pairing.getGT();
        BigInteger p = Zr.getOrder();
        int t = Integer.parseInt(door.getProperty("t"));
        Element[] TS = new Element[4];
        TS[1] = Utils.string2Element(door.getProperty("TS1"),G1);
        TS[2] = Utils.string2Element(door.getProperty("TS2"),G1);
        TS[3] = Utils.string2Element(door.getProperty("TS3"),Zr);
        int[] I = new int[t+1];
        for (int i = 1; i <= t; i++) {
            I[i] = Integer.parseInt(door.getProperty("I"+i));
        }
        ArrayList<String> allFileName = new ArrayList<>();
        System.out.println("wholeMath,filePath:" + filePath);
        FIleUtils.getAllFileName(filePath, allFileName);
        for (int i = 0; i < allFileName.size(); i++) {
            String filename = allFileName.get(i);
            if(filename.contains(".properties")){
                boolean result = match(filePath+"/"+filename, G1, t, I, TS, pairing, p, Zr,GT);
            }
        }
    }

    private boolean match(String filepos,Field G1,int t,int[] I,Element[] TS,Pairing pairing,BigInteger p,Field Zr,Field GT) throws IOException {
        System.out.println("I:" + Arrays.toString(I));
        System.out.println("t:" +t);
        Properties file = Utils.getProperties(filepos);
        Element[] A = new Element[6];
        Element[] B = new Element[6];
        Element K;
        for (int i = 1; i <= 5; i++) {
            A[i] = Utils.string2Element(file.getProperty("A"+i), G1);
            System.out.println("A" + i + ":" + A[i]);
            B[i] = Utils.string2Element(file.getProperty("B"+i), G1);
            System.out.println("B" + i + ":" + B[i]);
        }
        K = Utils.string2Element(file.getProperty("K"), G1);
        System.out.println("K:" + K);
        Element up = G1.newElement(0);
        for (int i = 1; i <=t ; i++) {
            up.add(A[I[i]]);
            //System.out.println("up step" + i + " :" + up);
            //System.out.println("A[I[" + i + "]]:"  + A[I[i]]);
        }
        up.add((K.duplicate()).mulZn(TS[3]));
        ///System.out.println("up:" + up);
        Element down = G1.newElement(0);
        for (int i = 1; i <=t ; i++) {
            down.add(B[I[i]]);
            //System.out.println("B[I[" + i + "]]:"  + B[I[i]]);
            //System.out.println("down step" + i + " :" + down);
        }
        System.out.println("down:" + down);
        Element Test = pairing.pairing(up,TS[1]).mul((pairing.pairing(down,TS[2])).invert());
        Element check = Utils.H2(Utils.element2bits(Test),p,Zr);

        Element S = Utils.string2Element(file.getProperty("S"),Zr);
        System.out.println("S:" + S);
        System.out.println("check:" + check);
        if(S.equals(check)){
            System.out.println(filepos.substring(
                    0, filepos.lastIndexOf("."))+".se");
            System.out.println("Yes");
            downloadfiles.add(filepos.substring(
                    0, filepos.lastIndexOf("."))+".se");
            return true;
        }
        else {
            System.out.println(filepos);
            System.out.println("No");
            return false;
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

    public void listfile(File file, Map<String, String> map) {
        //如果file代表的不是一个文件，而是一个目录
        if (!file.isFile()) {
            //列出该目录下的所有文件和目录
            File files[] = file.listFiles();
            //遍历files[]数组
            for (File f : files) {
                //递归
                listfile(f, map);
            }
        } else {
            /**
             * 处理文件名，上传后的文件是以uuid_文件名的形式去重新命名的，去除文件名的uuid_部分
             file.getName().indexOf("_")检索字符串中第一次出现"_"字符的位置，如果文件名类似于：9349249849-88343-8344_阿_凡_达.avi
             那么file.getName().substring(file.getName().indexOf("_")+1)处理之后就可以得到阿_凡_达.avi部分
             */
            String realName = file.getName().substring(file.getName().indexOf("_") + 1);
            //file.getName()得到的是文件的原始名称，这个名称是唯一的，因此可以作为key，realName是处理过后的名称，有可能会重复
            map.put(file.getName(), realName);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
