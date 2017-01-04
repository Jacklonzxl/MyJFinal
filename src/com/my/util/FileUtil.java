package com.my.util;




import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FileUtil
{

    //缓存根目录
    public final static String CACHE = "/sdcard/modifyphone/day/";

    //缓存当前目录
    //public static String dirPath = "";


    /***
     * 删除指定文件
     * @param path
     */
    public static void deleteFile(String path)
    {
        File file = new File(path);
        if (file.exists())
        {
            if (file.isFile())
            {
                file.delete();
            }
        }
    }

    /***
     * 删除部份缓存文件
     */
    public static void delCACHE(String dir)
    {
        try
        {
            File file = new File(dir);
            if (file.exists())
            {
                File delFile[] = file.listFiles();
                // GetFileSize g=new GetFileSize();
                // long l = 0;
                // String path = SaveBitmap.isExistsFilePath();
                // File ff = new File(path);
                // l = g.getFileSize(ff);
                if (delFile.length > 1000)
                {
                    for (int i = 100; i < (delFile.length / 2); i++)
                    {
                        delFile[i].delete();
                        // System.out.println(delFile[i].getName()+":"+TimeUtil.GetSqlDate(delFile[i].lastModified()));
                    }
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /***
     * 删除所有缓存文件
     */
    public static void delAll(String dir)
    {
        try
        {
            File file = new File(dir);
            if (file.exists())
            {
                File delFile[] = file.listFiles();
                for (int i = 0; i < (delFile.length); i++)
                {
                    delFile[i].delete();
                    // System.out.println(delFile[i].getName()+":"+TimeUtil.GetSqlDate(delFile[i].lastModified()));
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /***
     * 判断文件是否存在
     * @param fileName
     * @return
     */
    public static boolean isExistsFiles(String fileName)
    {
        File file = new File(fileName);
        return file.exists();
    }

   


    public static List<String> getFiles()
    {
        List<String> list = new ArrayList<String>();
        File[] files = new File(CACHE).listFiles();
        for (File file : files)
        {
            list.add(file.getAbsolutePath());
            System.out.println(file.getAbsolutePath());
        }
        return list;
    }

    public static void downFile(String _urlStr, String newFilename)
    {
        File file = new File(newFilename);
        //如果目标文件已经存在，则删除。产生覆盖旧文件的效果
        if (file.exists())
        {
            file.delete();
        }
        try
        {
            // 构造URL   
            URL url = new URL(_urlStr);
            // 打开连接   
            URLConnection con = url.openConnection();
            //获得文件的长度
            int contentLength = con.getContentLength();
            System.out.println("长度 :" + contentLength);
            // 输入流   
            InputStream is = con.getInputStream();
            // 1K的数据缓冲   
            byte[] bs = new byte[1024];
            // 读取到的数据长度   
            int len;
            // 输出的文件流   
            OutputStream os = new FileOutputStream(newFilename);
            // 开始读取   
            while ((len = is.read(bs)) != -1)
            {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接   
            os.close();
            is.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void saveFilesss(String toSaveString, String filePath)
    {
        try
        {
            File saveFile = new File(filePath);
            if (!saveFile.exists())
            {
                File dir = new File(saveFile.getParent());
                dir.mkdirs();
                saveFile.createNewFile();
            }

            FileOutputStream outStream = new FileOutputStream(saveFile);
            outStream.write(toSaveString.getBytes());
            outStream.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void makeDir(File dir) {  
        if(! dir.getParentFile().exists()) {  
            makeDir(dir.getParentFile());  
        }  
        dir.mkdir();  
    }
    public static boolean createFile(File file) throws IOException {  
        if(! file.exists()) {  
            makeDir(file.getParentFile());  
        }  
        return file.createNewFile();  
    } 
    public static void main(String[] args) {
    	makeDir(new File("d:/files/admin/2016/06/24/newcp-460078436432321-1466743584-192.168.51.60.xml"));
    	downFile("http://121.196.224.72/files/admin/2016/06/24/newcp-460078436432321-1466743584-192.168.51.60.xml","d:/files/admin/2016/06/24/newcp-460078436432321-1466743584-192.168.51.60.xml");
	}

}
