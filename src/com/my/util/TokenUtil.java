package com.my.util;

import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Acao
 * Date: 2004-12-1
 * Time: 15:16:04
 * To change this template use Options | File Templates.
 */
public class TokenUtil {

   public static byte[] getByteArray(String s, String separator) {
      StringTokenizer tokens = new StringTokenizer(s, separator, false);
      byte[] byteArray = new byte[tokens.countTokens()];

      for (int i = 0; tokens.hasMoreTokens(); i++) {
         byteArray[i] = Byte.parseByte(tokens.nextToken());
      }

      return byteArray;
   }

   public static int[] getIntArray(String s, String separator) {
      StringTokenizer tokens = new StringTokenizer(s, separator, false);
      int[] intArray = new int[tokens.countTokens()];

      for (int i = 0; tokens.hasMoreTokens(); i++) {
         intArray[i] = Integer.parseInt(tokens.nextToken());
      }

      return intArray;
   }

   public static long[] getLongArray(String s, String separator) {
      StringTokenizer tokens = new StringTokenizer(s, separator, false);
      long[] longArray = new long[tokens.countTokens()];

      for (int i = 0; tokens.hasMoreTokens(); i++) {
         longArray[i] = Long.parseLong(tokens.nextToken());
      }

      return longArray;
   }

   public static String[] getStrngArray(String s, String separator) {
      StringTokenizer tokens = new StringTokenizer(s, separator, false);
      String[] stringArray = new String[tokens.countTokens()];

      for (int i = 0; tokens.hasMoreTokens(); i++) {
         stringArray[i] = tokens.nextToken();
      }

      return stringArray;
   }

   public static String getTokenString(int[] array, String separator) {
      String result = "";
      for (int i = 0; i < array.length; i++)
         if (i == array.length - 1)
            result += new Integer(array[i]).toString();
         else
            result += new Integer(array[i]).toString() + separator;
      return result;
   }

   public static String getTokenString(long[] array, String separator) {
      String result = "";
      for (int i = 0; i < array.length; i++)
         if (i == array.length - 1)
            result += new Long(array[i]).toString();
         else
            result += new Long(array[i]).toString() + separator;
      return result;
   }

   public static String getTokenString(String[] array, String separator) {
      String result = "";
      for (int i = 0; i < array.length; i++)
         if (i == array.length - 1)
            result += array[i];
         else
            result += array[i] + separator;
      return result;
   }

   public static String getTokenStringWithStartEnd(String[] array, String separator) {
      String result = "" + separator;
      for (int i = 0; i < array.length; i++) {
         result += array[i] + separator;
      }
      return result;
   }

}
