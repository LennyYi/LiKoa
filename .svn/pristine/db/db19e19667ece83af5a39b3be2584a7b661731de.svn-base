package com.aiait.eflow.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.aiait.eflow.common.helper.HolidayHelper;

public class OverDueUtil {

    public static int getDaysBetween(Calendar d1, Calendar d2) {
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            java.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    public static int getDaysBetween(Date beginDate, Date endDate) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(beginDate);
        cal2.setTime(endDate);
        return getDaysBetween(cal1, cal2);
    }

    private static double getHolidays(String beginDate, String endDate, HashMap holidayMap) {
        double holidays = 0;
        int count = 0;
        endDate = endDate.substring(0, 10);
        beginDate = beginDate.substring(0, 10);
        while (!endDate.equals(beginDate)) {
            // System.out.println("current="+currentDate+"     subdate="+submitDateStr);
            try {
                endDate = StringUtil.afterNDay(-1, "yyyy-MM-dd", endDate);
                if (holidayMap.containsKey(endDate.substring(5, 7) + "/" + endDate.substring(8, 10) + "/"
                        + endDate.substring(0, 4))) {
                    String type = (String) holidayMap.get(endDate.substring(5, 7) + "/" + endDate.substring(8, 10)
                            + "/" + endDate.substring(0, 4));
                    if ("5".equals(type)) {
                        holidays++;
                    } else {
                        holidays = holidays + 0.5;
                    }
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        return holidays;
    }
    
    public static String shiftWorkingDay(String date, HashMap holidayMap, boolean forward) throws Exception {
        String time = date.substring(11);
        String hDate = date.substring(5, 7) + "/" + date.substring(8, 10) + "/" + date.substring(0, 4);
        while (holidayMap.containsKey(hDate)) {
            if (forward) {
                hDate = StringUtil.afterNDay(1, "MM/dd/yyyy", hDate);
                time = "09:00:00";
            } else {
                hDate = StringUtil.afterNDay(-1, "MM/dd/yyyy", hDate);
                time = "17:30:00";
            }
        }
        date = hDate.substring(6, 10) + "-" + hDate.substring(0, 2) + "-" + hDate.substring(3, 5) + " " + time;
        return date;
    }

    /**
     * Compute Overdue Hours
     * 
     * @param receiveDate
     *            yyyy-MM-dd HH:mm:ss
     * @param currentDate
     *            yyyy-MM-dd HH:mm:ss
     * @param limitHours
     */
    public static double computeOverdueHours(String receiveDate, String currentDate, double limitHours) {
        double invertalHours = computeInvertalHours(receiveDate, currentDate);
        double overdueHours = (double) (((int) Math.round((limitHours - invertalHours) * 100)) / 100.00);
        return overdueHours;
    }

    /**
     * 
     * @param receiveDate
     *            yyyy-MM-dd HH:mm:ss
     * @param currentDate
     *            yyyy-MM-dd HH:mm:ss
     * @param limitHours
     * @return
     */
    public static double computeOverdueHours_backup(String receiveDate, String currentDate, double limitHours) {
        // System.out.println("receiveDate="+receiveDate);
        // System.out.println("currentDate="+currentDate);
        // System.out.println("limitHours="+limitHours);

        String receiveTime = receiveDate.substring(11, 19);
        String currentTime = currentDate.substring(11, 19);

        String receiveHourMinute = receiveTime.substring(0, 2) + "" + receiveTime.substring(3, 5);
        String currentHourMinute = currentTime.substring(0, 2) + "" + currentTime.substring(3, 5);
        if (Long.parseLong(receiveHourMinute) > 1230 && Long.parseLong(receiveHourMinute) < 1330) {
            receiveDate = receiveDate.substring(0, 10) + " 13:30:00";
        } else if (Long.parseLong(receiveHourMinute) < 900) {
            receiveDate = receiveDate.substring(0, 10) + " 09:00:00";
        } else if (Long.parseLong(receiveHourMinute) > 1730) {
            receiveDate = receiveDate.substring(0, 10) + " 17:30:00";
        }

        if (Long.parseLong(currentHourMinute) > 1230 && Long.parseLong(currentHourMinute) < 1330) {
            currentDate = currentDate.substring(0, 10) + " 12:30:00";
        } else if (Long.parseLong(currentHourMinute) < 900) {
            try {
                currentDate = StringUtil.afterNDay(-1, "yyyy-MM-dd", currentDate.substring(0, 10));
                currentDate = currentDate + " 17:30:00";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Long.parseLong(currentHourMinute) > 1730) {
            currentDate = currentDate.substring(0, 10) + " 17:30:00";
        }

        receiveHourMinute = receiveDate.substring(11, 13) + "" + receiveDate.substring(14, 16);
        currentHourMinute = currentDate.substring(11, 13) + "" + currentDate.substring(14, 16);

        // System.out.println("currentDate="+currentDate);
        // System.out.println("receiveDate="+receiveDate);

        double overdueHours = 0;
        double invertalHours = 0;
        // 计算两个日期之间的间隔天数
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date beginDate = df.parse(receiveDate);
            Date endDate = df.parse(currentDate);
            // long time = endDate.getTime() - beginDate.getTime();
            // double days = time/(1000.0*60*60*60);
            int days = getDaysBetween(beginDate, endDate);
            // System.out.println("days: " + days);
            if (days > 1) {
                invertalHours = (days - 1) * 7.5; // 除掉开始天与结束天，其他天数*7.5
                String receiveDateStr1 = receiveDate.substring(0, 10) + " 17:30:00";
                String currentDateStr1 = currentDate.substring(0, 10) + " 09:00:00";
                // System.out.println(currentDateStr1);
                Date receiveDate1 = df.parse(receiveDateStr1);
                Date currentDate1 = df.parse(currentDateStr1);

                invertalHours = invertalHours
                        + ((receiveDate1.getTime() - beginDate.getTime()) + (endDate.getTime() - currentDate1.getTime()))
                        / (1000.0 * 60 * 60);
                // 计算两个日期之间的假期的天数
                // **
                HashMap holidayMap = HolidayHelper.getInstance().getHolidayMap();
                double holidays = getHolidays(receiveDate, currentDate, holidayMap);
                // System.out.println("holidays="+holidays);
                invertalHours = invertalHours - 7.5 * holidays;
                // */
            }
            if (days == 0) {
                long time = endDate.getTime() - beginDate.getTime();
                invertalHours = time / (1000.0 * 60 * 60);
            }
            if (days == 1) {
                String receiveDateStr1 = receiveDate.substring(0, 10) + " 17:30:00";
                String currentDateStr1 = StringUtil.afterNDay(1, "yyyy-MM-dd", receiveDateStr1.substring(0, 10))
                        + " 09:00:00";// currentDate.substring(0,10) + " 09:00:00";
                Date receiveDate1 = df.parse(receiveDateStr1);
                Date currentDate1 = df.parse(currentDateStr1);
                invertalHours = invertalHours
                        + ((receiveDate1.getTime() - beginDate.getTime()) + (endDate.getTime() - currentDate1.getTime()))
                        / (1000.0 * 60 * 60);
                // System.out.println("invertalHours 1: " + invertalHours);
            }

            // 如果receiveDate是在上午，则多计算了中午休息的一个小时，需要减掉
            // System.out.println("receiveHourMinute="+receiveHourMinute);
            if (days == 0 && Long.parseLong(receiveHourMinute) >= 900 && Long.parseLong(receiveHourMinute) <= 1230
                    && Long.parseLong(currentHourMinute) >= 1330 && Long.parseLong(currentHourMinute) <= 1730) {
                invertalHours = invertalHours - 1;
            } else if (days >= 1 && Long.parseLong(receiveHourMinute) >= 900
                    && Long.parseLong(receiveHourMinute) <= 1230 && Long.parseLong(currentHourMinute) >= 900
                    && Long.parseLong(currentHourMinute) <= 1230) {
                invertalHours = invertalHours - 1;
            } else if (days >= 1 && Long.parseLong(receiveHourMinute) >= 900
                    && Long.parseLong(receiveHourMinute) <= 1230 && Long.parseLong(currentHourMinute) >= 1330
                    && Long.parseLong(currentHourMinute) <= 1730) {
                invertalHours = invertalHours - 2;
            }

            // if ((days >= 1 && Long.parseLong(receiveHourMinute) >= 900 && Long.parseLong(receiveHourMinute) <= 1230)
            // || (days == 0 && Long.parseLong(receiveHourMinute) >= 900
            // && Long.parseLong(receiveHourMinute) <= 1230 && Long.parseLong(currentHourMinute) >= 1330 && Long
            // .parseLong(currentHourMinute) <= 1730)) {
            // invertalHours = invertalHours - 1;
            // }
            // 如果currentDate是在下午，则多计算了中午休息的一个小时，需要减掉
            // if(Long.parseLong(currentHourMinute)<=1730 && Long.parseLong(currentHourMinute)>=1330){
            // invertalHours = invertalHours - 1;
            // }

            // System.out.println("invertalHours: " + invertalHours);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println("invertalHours="+invertalHours);
        // System.out.println("limitHours-invertalHours="+((limitHours - invertalHours)));
        // overdueHours = (double)((int)Math.round((limitHours - overdueHours)*100)/100.00);
        // System.out.println("overdueHours="+(limitHours - invertalHours));
        overdueHours = (double) (((int) Math.round((limitHours - invertalHours) * 100)) / 100.00);
        // System.out.println("overdueHours="+overdueHours);
        return overdueHours;
    }

    /**
     * Compute Invertal Hours
     * 
     * @param receiveDate
     *            yyyy-MM-dd HH:mm:ss
     * @param currentDate
     *            yyyy-MM-dd HH:mm:ss
     */
    public static double computeInvertalHours(String receiveDate, String currentDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashMap holidayMap = HolidayHelper.getInstance().getHolidayMap();
        try {
            receiveDate = shiftWorkingDay(receiveDate, holidayMap, true);
            // System.out.println("receiveDate: " + receiveDate);
            currentDate = shiftWorkingDay(currentDate, holidayMap, false);
            // System.out.println("currentDate: " + currentDate);
            if (df.parse(receiveDate).after(df.parse(currentDate))) {
                return 0.0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        String receiveTime = receiveDate.substring(11, 19);
        String currentTime = currentDate.substring(11, 19);

        String receiveHourMinute = receiveTime.substring(0, 2) + "" + receiveTime.substring(3, 5);
        String currentHourMinute = currentTime.substring(0, 2) + "" + currentTime.substring(3, 5);
        if (Long.parseLong(receiveHourMinute) > 1230 && Long.parseLong(receiveHourMinute) < 1330) {
            receiveDate = receiveDate.substring(0, 10) + " 13:30:00";
        } else if (Long.parseLong(receiveHourMinute) < 900) {
            receiveDate = receiveDate.substring(0, 10) + " 09:00:00";
        } else if (Long.parseLong(receiveHourMinute) > 1730) {
            receiveDate = receiveDate.substring(0, 10) + " 17:30:00";
        }

        if (Long.parseLong(currentHourMinute) > 1230 && Long.parseLong(currentHourMinute) < 1330) {
            currentDate = currentDate.substring(0, 10) + " 12:30:00";
        } else if (Long.parseLong(currentHourMinute) < 900) {
            try {
                currentDate = StringUtil.afterNDay(-1, "yyyy-MM-dd", currentDate.substring(0, 10));
                currentDate = currentDate + " 17:30:00";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Long.parseLong(currentHourMinute) > 1730) {
            currentDate = currentDate.substring(0, 10) + " 17:30:00";
        }

        receiveHourMinute = receiveDate.substring(11, 13) + "" + receiveDate.substring(14, 16);
        currentHourMinute = currentDate.substring(11, 13) + "" + currentDate.substring(14, 16);

        // System.out.println("currentDate: " + currentDate);
        // System.out.println("receiveDate: " + receiveDate);

        double invertalHours = 0;
        // 计算两个日期之间的间隔天数

        try {
            Date beginDate = df.parse(receiveDate);
            Date endDate = df.parse(currentDate);
            int days = getDaysBetween(beginDate, endDate);
            // System.out.println("days: " + days);
            if (days > 1) {
                invertalHours = (days - 1) * 7.5; // 除掉开始天与结束天，其他天数*7.5
                String receiveDateStr1 = receiveDate.substring(0, 10) + " 17:30:00";
                String currentDateStr1 = currentDate.substring(0, 10) + " 09:00:00";
                // System.out.println(currentDateStr1);
                Date receiveDate1 = df.parse(receiveDateStr1);
                Date currentDate1 = df.parse(currentDateStr1);

                invertalHours = invertalHours
                        + ((receiveDate1.getTime() - beginDate.getTime()) + (endDate.getTime() - currentDate1.getTime()))
                        / (1000.0 * 60 * 60);
                // 计算两个日期之间的假期的天数
                double holidays = getHolidays(receiveDate, currentDate, holidayMap);
                // System.out.println("holidays: " + holidays);
                invertalHours = invertalHours - 7.5 * holidays;
            } else if (days == 0) {
                long time = endDate.getTime() - beginDate.getTime();
                invertalHours = time / (1000.0 * 60 * 60);
            } else if (days == 1) {
                String receiveDateStr1 = receiveDate.substring(0, 10) + " 17:30:00";
                String currentDateStr1 = StringUtil.afterNDay(1, "yyyy-MM-dd", receiveDateStr1.substring(0, 10))
                        + " 09:00:00";
                Date receiveDate1 = df.parse(receiveDateStr1);
                Date currentDate1 = df.parse(currentDateStr1);
                invertalHours = invertalHours
                        + ((receiveDate1.getTime() - beginDate.getTime()) + (endDate.getTime() - currentDate1.getTime()))
                        / (1000.0 * 60 * 60);
                // System.out.println("invertalHours 1: " + invertalHours);
            }

            // 如果receiveDate是在上午，则多计算了中午休息的一个小时，需要减掉
            // System.out.println("receiveHourMinute: " + receiveHourMinute);
            if (days == 0 && Long.parseLong(receiveHourMinute) >= 900 && Long.parseLong(receiveHourMinute) <= 1230
                    && Long.parseLong(currentHourMinute) >= 1330 && Long.parseLong(currentHourMinute) <= 1730) {
                invertalHours = invertalHours - 1;
            } else if (days >= 1 && Long.parseLong(receiveHourMinute) >= 900
                    && Long.parseLong(receiveHourMinute) <= 1230 && Long.parseLong(currentHourMinute) >= 900
                    && Long.parseLong(currentHourMinute) <= 1230) {
                invertalHours = invertalHours - 1;
            } else if (days >= 1 && Long.parseLong(receiveHourMinute) >= 900
                    && Long.parseLong(receiveHourMinute) <= 1230 && Long.parseLong(currentHourMinute) >= 1330
                    && Long.parseLong(currentHourMinute) <= 1730) {
                invertalHours = invertalHours - 2;
            }
            // System.out.println("invertalHours: " + invertalHours);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invertalHours;
    }

    public static double computeInvertalDays(String receiveDate, String currentDate) {
        double invertalHours = computeInvertalHours(receiveDate, currentDate);
        double days = Math.round((invertalHours / 7.5) * 100) / 100.00;
        return days;
    }

    public static void main(String[] args) throws Exception {
        /**
         * SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); Date submitDate =
         * df.parse("2007-06-05 23:51:23"); Date d = new Date();
         * System.out.println((d.getTime()-submitDate.getTime())/(1000*60*60));
         **/
        // System.out.println(OverDueUtil.computeInvertalHours("2007-06-05 08:51:23"));
        // SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        // Date submitDate = df.parse("09/21/2007 23:21:32");
        // System.out.println(submitDate.getHours()+""+submitDate.getMinutes());
        // String submitDateStr = "09/21/2007 17:21:32";
        // String d = submitDateStr.substring(11,19);
        // submitDateStr =
        // submitDateStr.substring(5,7)+"/"+submitDateStr.substring(8,10)+"/"+submitDateStr.substring(0,4);
        // System.out.println(d.substring(0,2)+""+d.substring(3,5));
        System.out.println("OverdueHours A: "
                + OverDueUtil.computeOverdueHours("2009-12-04 10:30:00", "2009-12-04 19:30:00", 3.0));
        System.out.println("OverdueHours B: "
                + OverDueUtil.computeOverdueHours("2009-12-04 10:30:00", "2009-12-05 19:30:00", 3.0));
        // System.out.println("OverdueHours C: " + OverDueUtil.computeOverdueHours("2009-12-04 10:30:00",
        // "2009-12-06 19:30:00", 3.0));
        // System.out.println("09/23/2007 12:23:34".substring(11,13)+"09/23/2007 12:23:34".substring(14,16));
    }

}
