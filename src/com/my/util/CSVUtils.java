package com.my.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.output.FileWriterWithEncoding;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.common.primitives.Bytes;

/**   
 * CSV操作(导出和导入)
 *
 * @author 林计钦
 * @version 1.0 Jan 27, 2014 4:30:58 PM   
 */
public class CSVUtils {
    
    /**
     * 导出
     * 
     * @param file csv文件(路径+文件名)，csv文件不存在会自动创建
     * @param dataList 数据
     * @return
     */
    public static boolean exportCsv(File file, List<String> dataList){
        boolean isSucess=false;
        
        FileOutputStream out=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw =new BufferedWriter(osw);
            if(dataList!=null && !dataList.isEmpty()){
                for(String data : dataList){
                    bw.append(data).append("\r");
                }
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }
        
        return isSucess;
    }
    
    /**
     * 导入
     * 
     * @param file csv文件(路径+文件)
     * @return
     */
    public static List<String> importCsv(File file){
        List<String> dataList=new ArrayList<String>();
        
        BufferedReader br=null;
        try { 
            br = new BufferedReader(new FileReader(file));
            String line = ""; 
            while ((line = br.readLine()) != null) { 
                dataList.add(line);
            }
        }catch (Exception e) {
        }finally{
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
 
        return dataList;
    }
//    public static void main(String aa[])
//    {
//    	 List<String> dataList=new ArrayList<String>();
//         dataList.add("1,张三,男");
//         dataList.add("2,李四,男");
//         dataList.add("3,小红,女");
//         boolean isSuccess=CSVUtils.exportCsv(new File("C:\\.csv"), dataList);
//         System.out.println(isSuccess);
//    }
    public static void csv(String path,String[] h,List<String[]> list) {
    	  try {
    	   FileWriterWithEncoding fw = new FileWriterWithEncoding(path,"UTF-8");
    	   String header = "";
    	   for(int i=0;i<h.length;i++)
    	   {
    		   if(i==h.length-1)
    		   {
    			   header+=h[i]+"\r\n";
    		   }else
    		   {
    			   header+=h[i]+",";
    		   }
    		  
    	   }
    	   byte b[] = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
    	   //fw.setHeader("Content-Disposition", "attachment;filename=test.csv");  
    	   //fw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));  
    	   fw.write(b.toString());
    	   fw.write(header);
    	   for (int i = 0; i < list.size(); i++) {
    		String txt[]=list.get(i); 
    		String content="";
    	    for (int j = 0; j < txt.length; j++) {
     		   if(j==txt.length-1)
     		   {
     			  content+=txt[j]+"\r\n";
     		   }else
     		   {
     			  content+=txt[j]+",";
     		   }  
    	    }
    	    fw.write(content);
    	    fw.flush();
    	   }
    	   fw.close();
    	  } catch (IOException e) {
    	   e.printStackTrace();
    	  }
    	 }
    public static void main(String aa[])
    {
    	String h[]={"1","2"};
    	String t1[]={"1","1"};
    	String t2[]={"1","2"};
    	String t3[]={"1","3"};
    	List<String[]> list=new ArrayList<String[]>();
    	list.add(t1);list.add(t2);list.add(t3);
    	//csv(h, list);
    }
    // csv's default delemiter is ','
    private final static String DEFAULT_DELIMITER = ",";
    // Mark a new line
    private final static String DEFAULT_END = "\r\n";
    // If you do not want a UTF-8 ,just replace the byte array.
    private final static byte commonCsvHead[] = { (byte) 0xEF, (byte) 0xBB,
      (byte) 0xBF };
    /**
     * Write source to a csv file
     * 
     * @param source
     * @throws IOException
     */
    public static void writeCsv(List<List<String>> source,String path) throws IOException {
     // Aoid java.lang.NullPointerException
     Preconditions.checkNotNull(source);
     StringBuilder sbBuilder = new StringBuilder();
     for (List<String> list : source) {
      sbBuilder.append(Joiner.on(DEFAULT_DELIMITER).join(list)).append(
        DEFAULT_END);
     }
     Files.write(Bytes.concat(commonCsvHead,
       sbBuilder.toString().getBytes(Charsets.UTF_8.toString())),
       new File(path));
    }
    /**
     * Simple read a csv file
     * 
     * @param file
     * @throws IOException
     */
    public static void readCsv(File file) throws IOException {
     System.out.println(Files.readFirstLine(file, Charsets.UTF_8));
    }
}