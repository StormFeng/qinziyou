package midian.baselib.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class FDDataUtils {

    private static final String EMPTY_STRING = "";

    private static final int DEFAULT_INT = 0;
    private static final double DEFAULT_DOUBLE = 0;
    private static final long DEFAULT_LONG = 0;
    private static final float DEFAULT_FLOAT = 0;

    private static final ArrayList<?> EMPTY_ARRAY_LIST = new ArrayList<Object>();

    /**
     * Get string from a map, if not exists, return empty string
     *
     * @param map
     * @param key
     * @return
     */
    public static String getString(Map<String, ?> map, String key) {
        if (map == null || !map.containsKey(key))
            return EMPTY_STRING;
        return getString(map.get(key));
    }

    /**
     * Get string from a object, if not exists, return empty string
     *
     * @return
     */
    public static String getString(Object obj) {
        if (obj == null)
            return EMPTY_STRING;
        return obj.toString();
    }

    public static List<?> getArrayList(Map<String, ?> map, String key) {
        if (map == null || !map.containsKey(key) || !map.get(key).getClass().isAssignableFrom(ArrayList.class))
            return EMPTY_ARRAY_LIST;
        return (map.get(key) == null ? EMPTY_ARRAY_LIST : (ArrayList<?>) map.get(key));
    }

    /**
     * string转int，默认返回DEFAULT_INT
     *
     * @param str
     * @return
     */
    public static int getInteger(String str) {
        return getInteger(str, DEFAULT_INT);
    }

    /**
     * string转int
     *
     * @param str
     * @return
     */
    public static int getInteger(String str, int defaultInt) {
        if (!ValidateUtil.isDigit(str, 0)) {
            return defaultInt;
        }
        Integer integer;
        integer = Integer.valueOf(str);
        return integer.intValue();
    }

    /**
     * string转double，默认返回DEFAULT_DOUBLE
     *
     * @param str
     * @return
     */
    public static double getDouble(String str) {
        return getDouble(str, DEFAULT_DOUBLE);
    }

    /**
     * string转double
     *
     * @param str
     * @return
     */
    public static double getDouble(String str, double defaultDouble) {
        if (!ValidateUtil.isDecimal(str)) {
            return defaultDouble;
        }
        Double mDouble;
        mDouble = Double.valueOf(str);
        return mDouble.doubleValue();
    }

    /**
     * string转float，默认返回DEFAULT_FLOAT
     *
     * @param str
     * @return
     */
    public static float getFloat(String str) {
        return getFloat(str, DEFAULT_FLOAT);
    }

    /**
     * string转float
     *
     * @param str
     * @return
     */
    public static float getFloat(String str, float defaultFloat) {
        if (!ValidateUtil.isDecimal(str)) {
            return defaultFloat;
        }
        Float mFloat;
        mFloat = Float.valueOf(str);
        return mFloat.floatValue();
    }

    /**
     * string转long，默认返回DEFAULT_LONG
     *
     * @param str
     * @return
     */
    public static long getLong(String str) {
        return getLong(str, DEFAULT_LONG);
    }

    /**
     * string转long
     *
     * @param str
     * @return
     */
    public static long getLong(String str, long defaultDouble) {
        if (!ValidateUtil.isDigit(str, 0)) {
            return defaultDouble;
        }
        Long mLong;
        mLong = Long.valueOf(str);
        return mLong.longValue();
    }

    /**
     * 格式化钱币单位，每隔3位数添加逗号
     * @param str
     * @return
     */
    public static String addComma(String str){
        float f = Float.parseFloat(str);
        DecimalFormat df;
        if(str.contains(".0")){
            str.replace(".0","");
            df=new DecimalFormat("#,###");
        }else if(!str.contains(".")){
            df=new DecimalFormat("#,###");
        }else {
            df = new DecimalFormat("#,###.0");
        }
        return df.format(f);
    }


    /**
     * 时间戳转换成日期字符窜
     *
     * @param time
     * @return
     */
    public static String getDateToString(long time) {
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date d = new Date(time);
        return dateFormat.format(d);
    }


    /**
     * 去掉时分秒的日期
     * @param times
     * @return
     */
    public static String getDateToYMD(String times) {
        Date data=null;
        try {
            data=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(times);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
        return sp.format(data);
    }

    /**
     *去掉秒的日期
     * @param times
     * @return
     */
    public static String getDateToYMDhm(String times) {
        Date data=null;
        try {
            data=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(times);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sp.format(data);
    }

    /**
     * 将日期字符串转为时间戳
     *
     * @param time
     * @return
     */
    public static long getStringToDate(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    /**
     * 获取两个日期的时间差
     */
    public static int getTimeInterval(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int interval = 0;
        try {
            Date currentTime = new Date();// 获取现在的时间
            Date beginTime = dateFormat.parse(data);
            interval = (int) ((beginTime.getTime() - currentTime.getTime()) / (1000));// 时间差
            // 单位秒
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return interval;
    }

    /**
     * 未来宝贝，根据返回图片名称返回图片URL
     * @param name
     * @param height
     * @param width
     * @return
     */
    public static String getImageUrl(String name,int height,int width){
        String str="http://mengzhu.img-cn-shenzhen.aliyuncs.com/"+name+"@"+height+"h_"+width+"w";
        return str;
    }

    public static boolean isMobile(String mobiles){

        Pattern p = Pattern.compile("((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\\\d{1}-?\\\\d{8}$)|(^0[3-9] {1}\\\\d{2}-?\\\\d{7,8}$)|(^0[1,2]{1}\\\\d{1}-?\\\\d{8}-(\\\\d{1,4})$)|(^0[3-9]{1}\\\\d{2}-? \\\\d{7,8}-(\\\\d{1,4})$))");

        Matcher m = p.matcher(mobiles);

        return m.matches();

    }
}