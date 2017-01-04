package com.my.util;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Created by: Kelson Li
 * Date: 2003-3-27
 */
public class TimeUtil {

  public static final int LAST_WEEK_OF_MONTH = 9;

  public static SimpleDateFormat getTimeFormat() {
    return new SimpleDateFormat("HH:mm");
  }

  public static SimpleDateFormat getTimeFormat2() {
    return new SimpleDateFormat("HH:mm:ss");
  }

  public static SimpleDateFormat getDateFormat() {
    return new SimpleDateFormat("yyyy-M-dd");
  }

  public static SimpleDateFormat getWeekDateFormat() {
    return new SimpleDateFormat("E, d MMM yyyy    Z", new Locale("en"));
  }

  public static SimpleDateFormat getEnDateFormat() {
    return new SimpleDateFormat("d MMM yyyy", new Locale("en"));
  }

  public static SimpleDateFormat getEnMonthYearFormat() {
    return new SimpleDateFormat("MMM yyyy", new Locale("en"));
  }

  public static SimpleDateFormat getEnWeekDateFormat() {
    return new SimpleDateFormat("E, d MMM yyyy", new Locale("en"));
  }

  public static SimpleDateFormat getMonthDayFormat() {
    return new SimpleDateFormat("MM-dd");
  }

  public static SimpleDateFormat getShortDateTimeFormat() {
    return new SimpleDateFormat("M/dd HH:mm:ss");
  }

  public static SimpleDateFormat getDateTimeFormatEn2() {
    return new SimpleDateFormat("yy-MM-dd HH:mm");
  }

  public static SimpleDateFormat getDateTimeFormatEn3() {
    return new SimpleDateFormat("MM-dd HH:mm");
  }

  public static SimpleDateFormat getDateTimeFormatEn4() {
    return new SimpleDateFormat("yyyyMMddHHmmss");
  }

  public static SimpleDateFormat getDateTimeFormatEn() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm");
  }

  public static SimpleDateFormat getDateTimeFormat() {
    return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", java.util.Locale.PRC);
  }

  public static SimpleDateFormat getShortDateFormat() {
    return new SimpleDateFormat("yy/MM/dd");
  }

  public static SimpleDateFormat getSqlDateFormat() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  }

//================================================
// get today time String
//================================================

  public static String GetShowToday() {
    SimpleDateFormat dateFormat = getDateFormat();
    return dateFormat.format(new Date());
  }

  public static String GetShowWeekToday() {
    SimpleDateFormat dateFormat = getWeekDateFormat();
    return dateFormat.format(new Date());
  }
//================================================
// mildec to time String
//================================================

  public static String GetShowDate(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getDateFormat();
    return dateFormat.format(new Date(milsec));
  }

  public static String GetEnShowDate(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getEnDateFormat();
    return dateFormat.format(new Date(milsec));
  }

  public static String GetEnShowMonthYear(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getEnMonthYearFormat();
    return dateFormat.format(new Date(milsec));
  }

  public static String GetEnShowWeekDate(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getEnWeekDateFormat();
    return dateFormat.format(new Date(milsec));
  }

//================================================

  public static String GetShowDateTime(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getDateTimeFormat();
    return dateFormat.format(new Date(milsec));
  }

  public static String GetShowDateTimeEn(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getDateTimeFormatEn();
    return dateFormat.format(new Date(milsec));
  }

  public static String GetShowTime(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getTimeFormat();
    return dateFormat.format(new Date(milsec));
  }

  public static String GetShowTime2(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getTimeFormat2();
    return dateFormat.format(new Date(milsec));
  }

  public static String GetShowDateTimeEn2(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getDateTimeFormatEn2();
    return dateFormat.format(new Date(milsec));
  }

  public static String GetShowDateTimeEn3(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getDateTimeFormatEn3();
    return dateFormat.format(new Date(milsec));
  }

  public static String GetShowDateTimeEn4(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getDateTimeFormatEn4();
    return dateFormat.format(new Date(milsec));
  }
//================================================

  public static String GetShortShowDateTime(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getShortDateTimeFormat();
    return dateFormat.format(new Date(milsec));
  }

//================================================
// mildec to time String
//================================================

  public static String GetShortShowDate(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getShortDateFormat();
    return dateFormat.format(new Date(milsec));
  }

  public static String GetSqlDate(long milsec) {
    if (milsec == -1) return "";

    SimpleDateFormat dateFormat = getSqlDateFormat();
    return dateFormat.format(new Date(milsec));
  }

//================================================
// time String to mildec
//================================================

  public static long GetTimeMilsecTime(String date) throws ParseException {
    if (date.trim().length() == 0) return -1;

    SimpleDateFormat dateFormat = getTimeFormat();
    return dateFormat.parse(date).getTime();
  }

  public static long getTime(String timeString) {
    int[] time = TokenUtil.getIntArray(timeString, ":");

    return time[0] * 60 * 60 * 1000l + time[1] * 60 * 1000l;
  }

  public static String getTimeShow(long time) {
    int hour = (int) time / 60 / 60 / 1000;
    int minute = (int) (time - hour * 60 * 60 * 1000l) / 60 / 1000;

    return (hour < 10 ? "0" + hour : "" + hour) + ":" + (minute < 10 ? "0" + minute : "" + minute);
  }

//================================================
// time String to mildec
//================================================

  public static long GetMilsecTime(String date)  {
    if (date.trim().length() == 0) return -1;

    SimpleDateFormat dateFormat = getDateFormat();
    try {
		return dateFormat.parse(date).getTime();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return -1;
  }


  public static long GetMilsecTimeEn(String date) throws ParseException {
    if (date.trim().length() == 0) return -1;

    SimpleDateFormat dateFormat = getDateTimeFormatEn();
    return dateFormat.parse(date).getTime();
  }

//================================================
// time String to mildec
//================================================

  public static long GetSqlMilsecTime(String date) throws ParseException {
    if (date.trim().length() == 0) return -1;

    SimpleDateFormat dateFormat = getSqlDateFormat();
    return dateFormat.parse(date).getTime();
  }

//================================================
// return earliest time of today
//================================================

  public static long getEarlyToday() {
    return (new java.util.Date()).getTime() / (24 * 60 * 60 * 1000) * 24 * 60 * 60 * 1000;
  }

//================================================
// return the zero hour of today
//================================================

  public static long getTodayZeroHour() throws ParseException {

    return GetSqlMilsecTime(getDateFormat().format(new Date())+" 00:00:00");
  }

  public static long getZeroHour(Date date) throws ParseException {
    return GetSqlMilsecTime(getDateFormat().format(date)+" 00:00:00");
  }

  public static long getZeroHour(long time) throws ParseException {
    return GetSqlMilsecTime(getDateFormat().format(new Date(time))+" 00:00:00");
  }

  public static long getDeadLine(long startLine, int days, boolean noRest) {
    GregorianCalendar calendar = new GregorianCalendar();

    if (noRest) return startLine += days * 24 * 60 * 60 * 1000l;

    long deadLine = startLine;
    while (days > 0) {
      calendar.setTimeInMillis(deadLine += 24 * 60 * 60 * 1000l);

      int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
      if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) --days;
    }

    return deadLine;
  }

  public static int getWorkingDay(long startLine, long deadLine, boolean noRest) {
    GregorianCalendar calendar = new GregorianCalendar();

    if (noRest)
      return (startLine == deadLine && startLine != -1) ? 1 : (int) ((deadLine - startLine) / 24 / 60 / 60 / 1000l);

    if (startLine == deadLine && startLine != -1) {
      calendar.setTimeInMillis(startLine);
      int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
      return (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) ? 1 : 0;
    }

    int amount = 0;
    while (startLine <= deadLine) {
      calendar.setTimeInMillis(startLine);
      int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
      if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) ++amount;
      startLine += 24 * 60 * 60 * 1000l;
    }

    return amount;
  }

//================================================
//  ��ָ������ת����ʾ��ʽ
//================================================

  public static String showCalendar(long milsec) throws ParseException {
    if (milsec == -1)
      return "-";

    milsec = getZeroHour(milsec);
//      long todayMilsec = getTodayZeroHour();
//      Calendar today = new GregorianCalendar();
//      Calendar testDay = new GregorianCalendar();
//      today.setTimeInMillis(todayMilsec);
//      testDay.setTimeInMillis(milsec);

//      long firstDayOfWeek = todayMilsec - today.get(Calendar.DAY_OF_WEEK) * 24 * 60 * 60 * 1000;
//      long lastDayOfWeek = firstDayOfWeek + 7 * 24 * 60 * 60 * 1000;
//
//      if (milsec == todayMilsec)
//         return "����";
//
//      else if (milsec == todayMilsec - 24 * 60 * 60 * 1000)
//         return "����";
//
//      else if (milsec == todayMilsec - 2 * 24 * 60 * 60 * 1000)
//         return "ǰ��";
//
//      else if (milsec == todayMilsec + 24 * 60 * 60 * 1000)
//         return "����";
//
//      else if (milsec == todayMilsec + 2 * 24 * 60 * 60 * 1000)
//         return "����";
//
//      else if (milsec >= firstDayOfWeek && milsec <= lastDayOfWeek) {
//         switch (testDay.get(Calendar.DAY_OF_WEEK)) {
//            case Calendar.SUNDAY:
//               return "����";
//            case Calendar.MONDAY:
//               return "��һ";
//            case Calendar.TUESDAY:
//               return "�ܶ�";
//            case Calendar.WEDNESDAY:
//               return "����";
//            case Calendar.THURSDAY:
//               return "����";
//            case Calendar.FRIDAY:
//               return "����";
//            case Calendar.SATURDAY:
//               return "����";
//         }
//      }

    SimpleDateFormat dateFormat = getMonthDayFormat();
    return dateFormat.format(new Date(milsec));
  }

  public static boolean overdue(long milsec) throws ParseException {
    long todayMilsec = getTodayZeroHour();
    if (milsec == -1) return false;
    if (todayMilsec > milsec) return true;
    return false;
  }

  public static long GetNow() {
    return new Date().getTime();
  }

  public static int getYear(long time) { 
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(time);

    return calendar.get(Calendar.YEAR);
  }

  public static int getMonth(long time) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(time);

    return (calendar.get(Calendar.MONTH) + 1);
  }

  public static int getDay(long time) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(time);

    return calendar.get(Calendar.DATE);
  }

  public static int getDayOfMonth(long time) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(time);
    return calendar.getActualMaximum(Calendar.DATE);
  }

  public static int getToday(long time) {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(time);

    return calendar.get(Calendar.DAY_OF_YEAR);
  }

  public static int getWeek(long milsec) {
    if (milsec == -1) return -1;

    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(new Date(milsec));

    int w = c.get(java.util.Calendar.DAY_OF_WEEK);

//    String strDate = "����" + GBweekDay(w - 1);
    return (w - 1);
  }
  public static String getWeekString(int intweek)
  {
	  if(intweek==1)
	  {
		  return "һ";
	  }else if(intweek==2) 
	  {
		  return "��";
	  }else if(intweek==3) 
	  {
		  return "��";
	  }else if(intweek==4) 
	  {
		  return "��";
	  }else if(intweek==5) 
	  {
		  return "��";
	  }else if(intweek==6) 
	  {
		  return "��";
	  }else
	  {
		  return "��";
	  }
  }

//================================================
// return local time String of Today
//================================================

  public static String showToday() {
    Date d = new Date();
    return TimeUtil.showDate(d.getTime());
  }

//================================================
// return local time String from milseconds
//================================================

  public static String showDate(long milsec) {
    if (milsec == -1) return "";

    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(new Date(milsec));

    int y = c.get(java.util.Calendar.YEAR);
    int m = c.get(java.util.Calendar.MONTH) + 1;
    int d = c.get(java.util.Calendar.DATE);
    int w = c.get(java.util.Calendar.DAY_OF_WEEK);

    String strDate = y + "��" + m + "��" + d + "�� ������" + GBweekDay(w - 1) + "��";
    return strDate;
  }

  public static String showThisYear(long milsec) {
    if (milsec == -1) return "";

    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(new Date(milsec));

    int y = c.get(java.util.Calendar.YEAR);
    int m = c.get(java.util.Calendar.MONTH) + 1;
    int d = c.get(java.util.Calendar.DATE);
    int w = c.get(java.util.Calendar.DAY_OF_WEEK);

    String strDate = String.valueOf(y);
    return strDate;
  }

  public static String showThisMonth(long milsec) {
    if (milsec == -1) return "";

    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(new Date(milsec));

    int y = c.get(java.util.Calendar.YEAR);
    int m = c.get(java.util.Calendar.MONTH) + 1;
    int d = c.get(java.util.Calendar.DATE);
    int w = c.get(java.util.Calendar.DAY_OF_WEEK);

    String strDate = String.valueOf(m);
    return strDate;
  }

  public static String showYearMonth(long milsec) {
    if (milsec == -1) return "";
    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(new Date(milsec));
    int y = c.get(java.util.Calendar.YEAR);
    int m = c.get(java.util.Calendar.MONTH) + 1;
    String strDate = String.valueOf(y) + "-" + String.valueOf(m);
    return strDate;
  }

  public static String showDateWithoutWeek(long milsec) {
    if (milsec == -1) return "";

    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(new Date(milsec));

    int y = c.get(java.util.Calendar.YEAR);
    int m = c.get(java.util.Calendar.MONTH) + 1;
    int d = c.get(java.util.Calendar.DATE);

    String strDate = y + "��" + m + "��" + d + "��";
    return strDate;
  }

  public static String showMonthYear(long milsec) {
    if (milsec == -1) return "";

    java.util.Calendar c = java.util.Calendar.getInstance();
    c.setTime(new Date(milsec));

    int y = c.get(java.util.Calendar.YEAR);
    int m = c.get(java.util.Calendar.MONTH) + 1;

    String strDate = y + "��" + m + "��";
    return strDate;
  }

//==============================================================================
  // Convert weekday to Chinese
  //==============================================================================
  public static String[] GBweek = new String[]{"��", "һ", "��", "��", "��", "��", "��"};

  public static String GBweekDay(int weekday) {
    return GBweek[weekday];
  }

  public static String calcHMS(long timeInSeconds) {
    long hours, minutes, seconds;
    hours = timeInSeconds / 3600000;
    timeInSeconds = timeInSeconds - (hours * 3600000);
    minutes = timeInSeconds / 60000;
    timeInSeconds = timeInSeconds - (minutes * 60000);
    seconds = timeInSeconds / 1000;
    return hours + ":" + minutes + ":" + seconds;
  }

  //ʱ���?-�죺Сʱ����
  public static String calcDHM(long time) {
    long day = (time / 24 / 60 / 60 / 1000);
    long hour = ((time - day * 24 * 60 * 60 * 1000l) / 60 / 60 / 1000);
    long minute = ((time - day * 24 * 60 * 60 * 1000l - hour * 60 * 60 * 1000l) / 60 / 1000);
    return (day > 0 ? (day + "��") : "") + (hour > 0 ? ((hour < 10 ? "0" + hour : "" + hour) + "Сʱ") : "") + (minute > 0 ? ((minute < 10 ? "0" + minute : "" + minute) + "����") : "");
  }

  //ȡָ�����������ܵĵ�һ��
  public static long getFirstDayOfWeekEn(Date date) throws ParseException {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);

    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    long zeroTime = getZeroHour(date);

    return zeroTime - (dayOfWeek - 1) * 24 * 60 * 60 * 1000;
  }

  public static long getFirstDayOfWeek(Date date) throws ParseException {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);

    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) == 1 ? 7 : (calendar.get(Calendar.DAY_OF_WEEK) - 1);
    long zeroTime = getZeroHour(date);

    return zeroTime - (dayOfWeek - 1) * 24 * 60 * 60 * 1000;
  }

  //ȡָ�����������ܵ����һ��?
  public static long getLastDayOfWeekEn(Date date) throws ParseException {
    long firstDayTime = getFirstDayOfWeekEn(date);

    return firstDayTime + 6 * 24 * 60 * 60 * 1000;
  }

  public static long getLastDayOfWeek(Date date) throws ParseException {
    long firstDayTime = getFirstDayOfWeek(date);

    return firstDayTime + 6 * 24 * 60 * 60 * 1000;
  }

  //ȡָ�����������µĵ�һ��
  public static long getFirstDayOfMonth(Date date) throws ParseException {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);

    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    long zeroTime = getZeroHour(date);

    return zeroTime - (long) (dayOfMonth - 1) * 24 * 60 * 60 * 1000;
  }

  //ȡָ�����������µ����һ��?
  public static long getLastDayOfMonth(Date date)  {
    long firstDayTime =0;
    try{
    firstDayTime=getFirstDayOfMonth(date);
    }catch(Exception e){}
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

    return firstDayTime + (long) (daysInMonth - 1) * 24 * 60 * 60 * 1000;
  }

  //ָ�����ڵ����µĵ�һ��
  public static long getFirstDayOfLastMonth(Date date) throws ParseException {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    int y = calendar.get(Calendar.YEAR);
    int m = calendar.get(Calendar.MONTH) + 1;
    int lastY = y;
    int lastM = m - 1;
    if (lastM == 0) {
      lastM = 12;
      lastY = y - 1;
    }
    return GetMilsecTime(lastY + "-" + lastM + "-01");
  }

  //ȡָ������������ĵ�һ��?
  public static long getFirstDayOfYear(Date date) throws ParseException {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);

    return new Date(TimeUtil.GetMilsecTime(String.valueOf(calendar.get(Calendar.YEAR) + "-1-1"))).getTime();
  }

  //ȡָ�����������µĵ�һ��
  public static long getLastDayOfYear(Date date) throws ParseException {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);

    return new Date(TimeUtil.GetMilsecTime(String.valueOf(calendar.get(Calendar.YEAR) + "-12-31"))).getTime();
  }

  //ȡ�涨ʱ�䷶Χ�ڣ�����������������ڵ���ʱ��weekSpace:ÿ����   date:���ڼ�
  //dateʹ��Calendar����ĳ���?
  public static long[] getCycleTimeByWeek(long startLine, long startTime, long endTime,
                                          int weekSpace, int date) throws ParseException {
    long A_DAY_TIME = 7 * 24 * 60 * 60 * 1000l;

    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(getZeroHour(startLine));
    calendar.set(Calendar.DAY_OF_WEEK, date);

    long flagTime = calendar.getTimeInMillis();
    long cycleTime = weekSpace * A_DAY_TIME;

    if (flagTime < startTime) {
      int beforeTimes = (int) ((startTime - flagTime) / A_DAY_TIME) / (7 * weekSpace);
      int beforeDays = (int) ((startTime - flagTime) / A_DAY_TIME) % (7 * weekSpace);
      if (beforeTimes == 0)
        flagTime += cycleTime;
      else
        flagTime += (7 * weekSpace - beforeDays) * A_DAY_TIME;
    }

    StringBuffer result = new StringBuffer();
    for (long fitTime = flagTime; fitTime <= endTime; fitTime += cycleTime)
      result.append(fitTime + "|");

    return TokenUtil.getLongArray(result.toString(), "|");
  }

  public static long[] getCycleTime(long startTime, long endTime, int weekSpace, int date) throws ParseException {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(getZeroHour(startTime));
    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
      calendar.setTimeInMillis(getZeroHour(startTime) - 24 * 60 * 60 * 1000l);
    calendar.set(Calendar.DAY_OF_WEEK, date);

    String result = "";
    long cycleTime = weekSpace * 7 * 24 * 60 * 60 * 1000l;
    for (long fitTime = calendar.getTimeInMillis(); fitTime <= endTime; fitTime += cycleTime)
      if (fitTime >= startTime)
        result += fitTime + "|";

    return TokenUtil.getLongArray(result, "|");
  }

  //ȡ�涨�ظ������ڣ�����������������ڵ���ʱ��weekSpace:ÿ����   date:���ڼ�
  public static long[] getCycleTimeByWeek(long startLine, long startTime, long endTime,
                                          int deadTimes, int weekSpace, int date) throws ParseException {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(getZeroHour(startLine));
    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
      calendar.setTimeInMillis(getZeroHour(startTime) - 24 * 60 * 60 * 1000l);
    calendar.set(Calendar.DAY_OF_WEEK, date);

    long flagTime = calendar.getTimeInMillis();
    long cycleTime = weekSpace * 7 * 24 * 60 * 60 * 1000l;
    if (flagTime < startTime) {
      int beforeTimes = (int) ((startTime - flagTime) / 1000 / 60 / 60 / 24) / (7 * weekSpace);
      int beforeDays = (int) ((startTime - flagTime) / 1000 / 60 / 60 / 24) % (7 * weekSpace);

      if (beforeTimes >= deadTimes)
        return new long[0];
      else {

      }
    }

    return new long[0];
  }

  public static long[] getCycleTime(long startTime, int deadTimes, int weekSpace, int date) throws ParseException {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(getZeroHour(startTime));
    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
      calendar.setTimeInMillis(getZeroHour(startTime) - 24 * 60 * 60 * 1000l);
    calendar.set(Calendar.DAY_OF_WEEK, date);

    String result = "";
    long cycleTime = weekSpace * 7 * 24 * 60 * 60 * 1000l;
    for (long fitTime = calendar.getTimeInMillis(), times = 0; times < deadTimes; fitTime += cycleTime)
      if (fitTime >= startTime) {
        result += fitTime + "|";
        times++;
      }

    return TokenUtil.getLongArray(result, "|");
  }

  //ȡ�涨ʱ�䷶Χ�ڣ�����������������ڵ���ʱ��?
  public static long[] getCycleTimeByMonth(long startTime, long endTime) throws ParseException {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(getZeroHour(startTime));
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    String result = "";
    while (calendar.getTimeInMillis() <= endTime) {
      result += calendar.getTimeInMillis() + "|";

      if (calendar.get(Calendar.MONTH) + 1 > Calendar.DECEMBER)
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
      calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) + 1) % (Calendar.DECEMBER + 1));
      while (calendar.get(Calendar.DAY_OF_MONTH) != day)
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    return TokenUtil.getLongArray(result, "|");
  }

  //ȡ�涨�ظ������ڣ�����������������ڵ���ʱ��?
  public static long[] getCycleTimeByMonth(long startTime, int deadTimes) throws ParseException {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(getZeroHour(startTime));
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    String result = "";
    for (int i = 0; i < deadTimes; i++) {
      result += calendar.getTimeInMillis() + "|";

      if (calendar.get(Calendar.MONTH) + 1 > Calendar.DECEMBER)
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
      calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) + 1) % (Calendar.DECEMBER + 1));
      while (calendar.get(Calendar.DAY_OF_MONTH) != day)
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    return TokenUtil.getLongArray(result, "|");
  }

  public static long[] getCycleTimeByYear(long startTime, long endTime) throws ParseException {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(getZeroHour(startTime));

    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    String result = "";
    while (calendar.getTimeInMillis() <= endTime) {
      if (calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.DAY_OF_MONTH) == day)
        result += calendar.getTimeInMillis() + "|";

      calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
      calendar.set(Calendar.MONTH, month);
      calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    return TokenUtil.getLongArray(result, "|");
  }

  public static long[] getCycleTimeByYear(long startTime, int deadTimes) throws ParseException {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(getZeroHour(startTime));

    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    String result = "";
    for (int i = 0; i < deadTimes; i++) {
      if (calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.DAY_OF_MONTH) == day)
        result += calendar.getTimeInMillis() + "|";

      calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
      calendar.set(Calendar.MONTH, month);
      calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    return TokenUtil.getLongArray(result, "|");
  }

  //ȡ�涨ʱ�䷶Χ�ڣ�����������������ڵ���ʱ��monthSpace:ÿ����  week:�ڼ���  date:���ڼ�
  //dateʹ��Calendar����ĳ���?
  public static long[] getCycleTime(long startTime, long endTime, int monthSpace, int week, int date) throws ParseException {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(getZeroHour(startTime));
    calendar.set(Calendar.DAY_OF_MONTH, 1);

    String result = "";
    for (long fitMonthFirstDay = calendar.getTimeInMillis();
         fitMonthFirstDay <= endTime; fitMonthFirstDay = calendar.getTimeInMillis()) {

      int currentMonth = calendar.get(Calendar.MONTH);

      week = week == LAST_WEEK_OF_MONTH ? calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) : week;
      calendar.set(Calendar.WEEK_OF_MONTH, week);
      calendar.set(Calendar.DAY_OF_WEEK, date);

      long fitTime = calendar.getTimeInMillis();
      if (calendar.get(Calendar.MONTH) == currentMonth && fitTime >= startTime && fitTime <= endTime)
        result += fitTime + "|";

      calendar.setTimeInMillis(fitMonthFirstDay);
      if (calendar.get(Calendar.MONTH) + monthSpace > Calendar.DECEMBER)
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
      calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) + monthSpace) % (Calendar.DECEMBER + 1));
    }

    return TokenUtil.getLongArray(result, "|");
  }

  //ȡ�涨�ظ������ڣ�����������������ڵ���ʱ��monthSpace:ÿ����  week:�ڼ���  date:���ڼ�
  //dateʹ��Calendar����ĳ���?
  public static long[] getCycleTime(long startTime, int deadTimes, int monthSpace, int week, int date) throws ParseException {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(getZeroHour(startTime));
    calendar.set(Calendar.DAY_OF_MONTH, 1);

    String result = "";
    for (long fitMonthFirstDay = calendar.getTimeInMillis(), times = 0;
         times < deadTimes; fitMonthFirstDay = calendar.getTimeInMillis()) {

      int currentMonth = calendar.get(Calendar.MONTH);

      week = week == LAST_WEEK_OF_MONTH ? calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) : week;
      calendar.set(Calendar.WEEK_OF_MONTH, week);
      calendar.set(Calendar.DAY_OF_WEEK, date);

      long fitTime = calendar.getTimeInMillis();
      if (calendar.get(Calendar.MONTH) == currentMonth && fitTime >= startTime) {
        result += fitTime + "|";
        times++;
      }

      calendar.setTimeInMillis(fitMonthFirstDay);
      if (calendar.get(Calendar.MONTH) + monthSpace > Calendar.DECEMBER)
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
      calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) + monthSpace) % (Calendar.DECEMBER + 1));
    }

    return TokenUtil.getLongArray(result, "|");
  }
  
  /** 
   * 获得指定日期的前一天 
   *  
   * @param specifiedDay 
   * @return 
   * @throws Exception 
   */  
  public static String getSpecifiedDayBefore(String specifiedDay,SimpleDateFormat simp) {//可以用new Date().toLocalString()传递参数  
      Calendar c = Calendar.getInstance();  
      Date date = null;  
      try {  
          date =simp.parse(specifiedDay);  
      } catch (ParseException e) {  
          e.printStackTrace();  
      }  
      c.setTime(date);  
      int day = c.get(Calendar.DATE);  
      c.set(Calendar.DATE, day - 1);  
      String dayBefore =simp.format(c.getTime());  
      return dayBefore;  
  }  

  /** 
   * 获得指定日期的后一天 
   *  
   * @param specifiedDay 
   * @return 
   */  
  public static String getSpecifiedDayAfter(String specifiedDay,SimpleDateFormat simp) {  
      Calendar c = Calendar.getInstance();  
      Date date = null;  
      try {  
          date =simp.parse(specifiedDay);  
      } catch (ParseException e) {  
          e.printStackTrace();  
      }  
      c.setTime(date);  
      int day = c.get(Calendar.DATE);  
      c.set(Calendar.DATE, day + 1);  

      String dayAfter = simp.format(c.getTime());  
      return dayAfter;  
  }    
}