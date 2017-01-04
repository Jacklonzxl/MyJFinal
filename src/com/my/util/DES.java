
package com.my.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DES
{
	public static String decryptKey = "A5uC0YG9";
    public static void main(String args[]) throws UnsupportedEncodingException
    {

        try
        {

//            String str = DES
//                    .encryptDES(
//                            "{\"appid\":\"0\",\"apppackage\":\"0\",\"tid\":0,\"username\":\"jzgg\",\"m\":1,\"rqt\":5,\"imei\":\"8316501E-C34A-59BC-B1E2-6C57CA92A700\",\"imsi\":0,\"mobile\":\"0000\",\"sv\":\"1.0.0\",\"st\":1,\"tt\":5,\"ip\":\"192.168.1.109\",\"ca\":\"unknown\",\"ac\":1,\"osv\":\"IOS5.000000\",\"ua\":\"0\",\"lge\":\"0.000000\",\"lae\":\"'0.000000\",\"lac\":\"0\",\"cellid\":\"0\",\"prov\":\"0\",\"city\":\"0\",\"area\":\"0\",\"appsids\":\"0\",\"appsname\":\"0\",\"appsversion\":\"0\",\"ray\":3,\"shid\":\"0\",\"'w\":320,\"h\":240,\"xt\":0.000000,\"rt\":0}\"");
//            System.out.println(str);
//            System.out.println(DES.decryptDES(str));
        	System.out.println(DES.decryptDES("035q84EKgU5KmObTXhIH0g=="));

        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static byte[] hexToBytes(String hexString)
    {
        if (hexString == null)
            return null;
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = (byte) Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }

    private static byte[] iv =
    {
            1, 2, 3, 4, 5, 6, 7, 8
    };

    /**
     * 解密
     * @param decryptString
     * @param decryptKey
     * @return
     */
    public static String decryptDES(String decryptString)
    {
        try
        {
            byte[] byteMi = Base64.decode(decryptString);

            IvParameterSpec zeroIv = new IvParameterSpec(iv);

            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte decryptedData[] = cipher.doFinal(byteMi);
            return new String(decryptedData);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 加密
     * @param encryptString
     * @param encryptKey
     * @return
     */
    public static String encryptDES(String encryptString)
    {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher cipher;
        try
        {
            cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData;
            encryptedData = cipher.doFinal(encryptString.getBytes());
            return Base64.encode(encryptedData);
        }
        catch (IllegalBlockSizeException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encryptString;
    }

}
