package com.yfkj.code.xfcc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 读取模版文件内容替换写入生成文件
 * @author 胡汉三
 * 2017年1月19日 下午4:41:37
 */
public class ReadWriteFile {
	public static BufferedReader bufread;
    //指定文件路径和名称
    private static String path = "//yfkj//gz//task//code//template//dao.temp";
    private static  File filename = new File(path);
    private static StringBuilder readStr = new StringBuilder(); 
    public String getReadStr(){
    	return readStr.toString();
    }
    
    @SuppressWarnings("static-access")
	public ReadWriteFile(String path){  
    	this.path = path;
    	this.filename = new File(path);
    }
    public ReadWriteFile(){}
     
    /**
     * 检测是否创建
     * @throws IOException
     */
    public void creatTxtFile() throws IOException{
        if (!filename.exists()) {
            filename.createNewFile();
            System.err.println(filename + "已创建！");
        }
    }
    
    /**
     * 读取template文件内容
     * @return
     */
    public String readTxtFile(){
    	readStr = new StringBuilder(); 
        String read;
        FileReader fileread;
        try {
            fileread = new FileReader(filename);
            bufread = new BufferedReader(fileread);
            try {
                while ((read = bufread.readLine()) != null) {
                	readStr.append(read+"\n"); 
                }  
            } catch (IOException e) {
                e.printStackTrace(); 
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return readStr.toString();
    } 
    /**
     * 写入新文件
     * @param path  新文件path
     * @throws IOException
     */
    public void writeTxtFile(String path) throws IOException{
        RandomAccessFile mm = null;
        try {
        	File file = new File(path);
            mm = new RandomAccessFile(file, "rw");
            mm.write(readStr.toString().getBytes("UTF-8"));
        } catch (IOException e1) {      
            e1.printStackTrace();
        } finally { 
            if (mm != null) {  
                try {  
                    mm.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 替换源
     * @param target 替换的目标
     * @param str    替换的内容
     */
    public void getReplace(String target,String str){
    	String o = readStr.toString().replaceAll(target, str);
    	readStr = new StringBuilder();
    	readStr.append(o);
    } 
    
    
}
