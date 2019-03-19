package com.hualianzb.sec.utils;

import android.annotation.SuppressLint;

import com.hysd.android.platform_huanuo.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

    public static String getTime(long time) {

        SimpleDateFormat format = null;
        try {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        } catch (Exception e) {
            e.printStackTrace();
            return "time error";
        } finally {
        }
        return format.format(new Date(time));
    }

    public static String getTime1(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String getTimeall(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String getTime11(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    public static String getTime12(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));
    }

    public static String getTime2(long time) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String getDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    public static String getDay(String time) {
        if (time != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(time);
                return format2.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getHourMin(String time) {
        if (time != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
            Date date = null;
            try {
                date = format.parse(time);
                return format2.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getTime01(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("MM-dd");
        Date date = null;
        try {
            date = format.parse(time);
            return format2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTime02(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(time);
            return format2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTime03(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = format.parse(time);
            return format2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * "2016-06-23 16:06:24",
     *
     * @param time
     * @return
     */
    public static Long getLongTimes(String time) {
        if (!StringUtils.isNEmpty(time)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (format != null) {
                Date date = null;
                try {
                    date = format.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date.getTime();
            }
        }
        return 0L;
    }

    /**
     * "2016-06-23 16:06:24",
     *
     * @param time
     * @return
     */
    public static String getYear(String time) {
        if (!StringUtils.isNEmpty(time)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy");
            if (format != null) {
                Date date = null;
                try {
                    date = format.parse(time);
                    return format2.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * "2016-06-23 16:06:24",
     *
     * @param time
     * @return
     */
    public static String getMon(String time) {
        if (!StringUtils.isNEmpty(time)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("MM");
            if (format != null) {
                Date date = null;
                try {
                    date = format.parse(time);
                    return format2.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static boolean compareTime(String nowTime, String endTime) {
        boolean flag = false;
        if (nowTime != null && endTime != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long currentTime = 0;
            long stopTime = 0;
            try {
                stopTime = formatter.parse(endTime).getTime();
                currentTime = formatter.parse(nowTime).getTime();
                if (currentTime > stopTime) {
                    flag = true;
                } else {
                    flag = false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return flag;
    }


    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }


    public static String getChatTime(long timesamp) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));

        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(timesamp);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(timesamp);
                break;
            case 2:
                result = "前天 " + getHourAndMin(timesamp);
                break;

            default:
                // result = temp + "天前 ";
                result = getTime(timesamp);
                break;
        }

        return result;
    }

    public static String getWeekOfDate(String t) {
        Long time = getLongTimes(t);
        Date date = new Date(time);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    /**
     * 将秒转成分秒
     *
     * @return
     */
    public static String getVoiceRecorderTime(int time) {
        int minute = time / 60;
        int second = time % 60;
        if (minute == 0) {
            return String.valueOf(second);
        }
        return minute + ":" + second;

    }

    /**
     * 将时分转成秒
     *
     * @return
     */
    public static String getSecondTime(String time) {
        int second = 0;
        int msecond = 0;
        int minute = 0;
        String[] str = time.split(":");
        if (str[0] != null && str[1] != null) {
            str[0] = str[0].trim();
            str[1] = str[1].trim();
            if ("0".equals(str[0].substring(0, 1))) {
                minute = Integer.parseInt(str[0].substring(1, 2));
            } else {
                minute = Integer.parseInt(str[0]);
            }

            if ("0".equals(str[1].substring(0, 1))) {
                msecond = Integer.parseInt(str[1].substring(1, 2));
            } else {
                msecond = Integer.parseInt(str[1]);
            }
            second = minute * 60 + msecond;
        }
        return String.valueOf(second);

    }

    public static String paseAnswerTime(String answerTime) {
        String str = "";
        if (!StringUtils.isNEmpty(answerTime)) {
            int time = Integer.parseInt(answerTime);
            if (time > 60) {
                str = time / 60 + "分";
                str += time % 60 + "秒";
            } else {
                str = time + "秒";
            }
        }
        return str;
    }

    public static String getCommentTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        Calendar sysCal = Calendar.getInstance();
        Date date = null;
        Date sysDate = new Date();
        try {
            date = format.parse(time);
            cal.setTime(date);
            sysCal.setTime(sysDate);

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DATE);
            int hour = cal.get(Calendar.HOUR);
            int min = cal.get(Calendar.MINUTE);

            int sysYear = sysCal.get(Calendar.YEAR);
            int sysMonth = sysCal.get(Calendar.MONTH);
            int sysDay = sysCal.get(Calendar.DATE);
            int sysHour = sysCal.get(Calendar.HOUR);
            int sysMin = sysCal.get(Calendar.MINUTE);

            if ((sysYear) > year) {
                return format1.format(date);
            } else if ((sysYear) == year) {
                if (sysMonth - month != 0) {
                    return format2.format(date);
                } else if (sysDay - day > 1) {
                    return format2.format(date);
                } else if (sysDay - day == 1) {
                    return "昨天";
                } else if (sysHour - hour > 1) {
                    int hours = sysHour - hour;
                    return hours + "小时前";
                } else if (sysMin - min > 0) {
                    int minutes = sysMin - min;
                    return minutes + "分钟前";
                } else {
                    return "刚刚";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getShowTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        Calendar sysCal = Calendar.getInstance();
        Date date = null;
        Date sysDate = new Date();
        try {
            date = format.parse(time);
            long times = sysDate.getTime() - date.getTime();//这样得到的差值是微秒级别

            if (times >= (long) 1000 * 60 * 60 * 24 * 30 * 12 * 2) {
                return "2年前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 30 * 12 * 1) {
                return "1年前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 30 * 6) {
                return "半年前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 30 * 5) {
                return "5个月前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 30 * 4) {
                return "4个月前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 30 * 3) {
                return "3个月前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 30 * 2) {
                return "2个月前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 30 * 1) {
                return "1个月前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 15) {
                return "半个月前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 14) {
                return "14天前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 13) {
                return "13天前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 12) {
                return "12天前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 11) {
                return "11天前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 10) {
                return "10天前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 9) {
                return "9天前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 8) {
                return "8天前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 7) {
                return "7天前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 6) {
                return "6天前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 5) {
                return "5天前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 4) {
                return "4天前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 3) {
                return "3天前";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 2) {
                return "前天";
            } else if (times >= (long) 1000 * 60 * 60 * 24 * 1) {
                return "昨天";
            } else if (times >= (long) 1000 * 60 * 60 * 23) {
                return "23小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 22) {
                return "22小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 21) {
                return "21小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 20) {
                return "20小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 19) {
                return "19小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 18) {
                return "18小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 17) {
                return "17小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 16) {
                return "16小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 15) {
                return "15小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 14) {
                return "14小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 13) {
                return "13小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 12) {
                return "12小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 11) {
                return "11小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 10) {
                return "10小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 9) {
                return "9小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 8) {
                return "8小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 7) {
                return "7小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 6) {
                return "6小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 5) {
                return "5小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 4) {
                return "4小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 3) {
                return "3小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 2) {
                return "2小时前";
            } else if (times >= (long) 1000 * 60 * 60 * 1) {
                return "1小时前";
            } else {
                return "刚刚";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    //时间转换
    public static String getTextTime(int time) {
        String timeStr = "";
        if (time >= 0) {
            int hour = 0;
            int minute = 0;
            int second = 0;
            if (time <= 0) {
                timeStr = "00:00:00";
            } else {
                minute = time / 60;
                if (minute < 60) {
                    second = time % 60;
                    timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
                } else {
                    hour = minute / 60;
                    if (hour > 99)
                        timeStr = "99:59:59";
                    minute = minute % 60;
                    second = time - hour * 3600 - minute * 60;
                    timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
                }
            }
        }
        return timeStr;
    }

    public static int getDValue(String startTime, String finishTime) {
        int i = 0;
        if (!StringUtils.isNEmpty(startTime) && !StringUtils.isNEmpty(finishTime)) {
            try {
                Long sTime = getLongTimes(startTime);
                Long fTime = getLongTimes(finishTime);
                Long time = fTime - sTime;
                i = Integer.parseInt(String.valueOf(time));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return i / 1000;
        }
        return i;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


    /**
     * 计算两个时间的差值
     */
    public static int calculateTime(String startTime, String finishTime) {
        int time = 0;
        Boolean f1 = StringUtils.isNEmpty(startTime);
        Boolean f2 = StringUtils.isNEmpty(finishTime);
        if (!f2 && !f1) {
            Long sTime = getLongTimes(startTime);
            Long fTime = getLongTimes(finishTime);
            time = Integer.parseInt(String.valueOf(Long.parseLong(String.valueOf(Math.abs(fTime - sTime))) / 1000));
        }
        return time;
    }
}
