package com.tools;

public class ChStr {
    public String chStr(String str) {
        if(str == null){
            str = "";
        } else{
            try{
                str = (new String(str.getBytes("iso-8859-1"),"UTF-8")).trim();
            } catch (Exception e){
                e.printStackTrace(System.err);
            }
        }
        return str;
    }
}
