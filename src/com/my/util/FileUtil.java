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

    //�����Ŀ¼
    public final static String CACHE = "/sdcard/modifyphone/day/";

    //���浱ǰĿ¼
    //public static String dirPath = "";


    /***
     * ɾ��ָ���ļ�
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
     * ɾ�����ݻ����ļ�
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
     * ɾ�����л����ļ�
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
     * �ж��ļ��Ƿ����
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
        //���Ŀ���ļ��Ѿ����ڣ���ɾ�����������Ǿ��ļ���Ч��
        if (file.exists())
        {
            file.delete();
        }
        try
        {
            // ����URL   
            URL url = new URL(_urlStr);
            // ������   
            URLConnection con = url.openConnection();
            //����ļ��ĳ���
            int contentLength = con.getContentLength();
            System.out.println("���� :" + contentLength);
            // ������   
            InputStream is = con.getInputStream();
            // 1K�����ݻ���   
            byte[] bs = new byte[1024];
            // ��ȡ�������ݳ���   
            int len;
            // ������ļ���   
            OutputStream os = new FileOutputStream(newFilename);
            // ��ʼ��ȡ   
            while ((len = is.read(bs)) != -1)
            {
                os.write(bs, 0, len);
            }
            // ��ϣ��ر���������   
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
