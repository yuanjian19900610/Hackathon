package com.hacksthon.team.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	private static final String TAG = Utils.class.getSimpleName();

	public static final char PAUSE = ',';

	public static final char WAIT = ';';

	public static final char WILD = 'N';

	/**
	 * True if c is ISO-LATIN characters 0-9, *, # , +,
	 * WILD, WAIT, PAUSE
	 */
	public final static boolean isNonSeparator(char c) {
		return (c >= '0' && c <= '9') || c == '*' || c == '#' || c == '+' || c == WILD || c == WAIT || c == PAUSE;
	}

	public static boolean isUserPasswordValid(String userPwd) {
		char[] cs = userPwd.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] >= '0' && cs[i] <= '9') {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 隐藏号码部分内容
	 *
	 * @param phoneNumber
	 * @return
	 */
	public static String formatPhoneNumber(String phoneNumber) {
		char[] cs = phoneNumber.toCharArray();
		char[] res = new char[cs.length];
		for (int i = cs.length - 1, j = 0; i >= 0; i--, j++) {
			if (j >= 4 && j <= 7) {
				res[i] = '*';
			} else {
				res[i] = cs[i];
			}
		}
		return new String(res);
	}

	/**
	 * 判断是否为手机号码
	 *
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		// String pattern =
		// "(13[4-9]|15[7-9]|15[0-2]|18[7-8])[0-9]{8}$";
		// Pattern p = Pattern.compile(pattern);
		// Matcher m = p.matcher(mobile);
		// boolean flag = m.matches();
		// return flag;
		String pattern = "^((\\+86)|(86))?0*(1)\\d{10}$";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(mobile);
		boolean flag = m.matches();
		return flag;
	}

	/**
	 * 判断电话号码合法性
	 *
	 * @param number
	 * @return
	 */
	public static boolean isPhoneNumberValid(String number) {
		if (!isMobile(number)) {

			return isTelephoneValid(number);
		}
		return true;
	}

	/**
	 * 判断为空，则直接返回空字符串
	 */
	public static String getStrValue(String value){
		if(TextUtils.isEmpty(value)) return "" ;
		else return value ;

	}

	/**
	 * 得到票据上需要展示的点电话号码格式
     */
	public static String getCustomerPhone(String number){
		if (number != null && number.length() > 7) {
			return "(" + number.substring(0, number.length() - (number.substring(3)).length()) + "****" + number.substring(7)+ ")" ;
		}
		return "" ;
	}

	/**
	 *
	 * @Title:isUserNameValid
	 * @Description: ^[a-zA-Z]{1}[a-zA-Z0-9_]{3,15}$
	 * @param @param name
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isUserNameValid(String name) {
		String pattern = "^[a-zA-Z]{1}[a-zA-Z0-9_]{0,15}$";

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(name);

		boolean flag = m.matches();
		return flag;
	}

	/**
	 *
	 * @Title:isTelephoneValid
	 * @Description:0\\d{2,3 \\d{7,8}
	 * @param @param name
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isTelephoneValid(String telephone) {
		String pattern = "0\\d{2,3}\\d{7,8}||\\d{7,8}";

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(telephone);

		boolean flag = m.matches();
		return flag;
	}

	/**
	 *
	 * @Title:isCustomerNameValid
	 * @Description:^([a-zA-Z]{1
	 *                           [a-zA-Z0-9_]{0,15})|([\u0391
	 *                           - \uFFE5 ] { 2 , 8 } ) $
	 * @param @param name
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isCustomerNameValid(String name) {
		String pattern = "^([a-zA-Z]{1}[a-zA-Z0-9_]{0,15})|([\u0391-\uFFE5]{0,8})$";

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(name);

		boolean flag = m.matches();
		return flag;
	}

	public static String stripSeparators(String phoneNumber) {
		if (phoneNumber == null) {
			return null;
		}
		int len = phoneNumber.length();
		StringBuilder ret = new StringBuilder(len);

		for (int i = 0; i < len; i++) {
			char c = phoneNumber.charAt(i);
			if (isNonSeparator(c)) {
				ret.append(c);
			}
		}

		return ret.toString();
	}

	public static String quoteString(String s) {
		if (s == null) {
			return null;
		}
		if (!s.matches("^\".*\"$")) {
			return "\"" + s + "\"";
		} else {
			return s;
		}
	}

	public static String genUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 金额默认为小数点 后两位 例如 10.00元 12.10 转化后变成 12.00
	 */
	@Deprecated
	public static String transfer(String param) {
		double tmp = Math.floor(Double.parseDouble(param));
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(tmp);
	}

	/**
	 * <pre>
	 * 类名称:Utils.java
	 * 方法名称:isNum
	 * 参数说明: 判断字符串是否为数字
	 *     @param str
	 *     @return
	 * 返回类型:
	 *     @return boolean
	 * </pre>
	 */
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/**
	 * 金额默认两位小数
	 *
	 */
	public static String transferDot2(String param) {
		if (TextUtils.isEmpty(param))
			param = "0.00";
		double tmp = Double.parseDouble(param);
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(tmp);
	}

	/**
	 * 金额默认两位小数
	 *
	 */
	public static String transferDot2(Float param) {
		if(param == null){
			param = 0F;
		}
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(param);
	}

	/**
	 * 金额默认两位小数
	 * @param param
	 * @return
     */
	public static String transferDot2(BigDecimal param) {
		String tempStr ;
		if(param == null) tempStr = "0.00";
		else tempStr = param.toString();
		return transferDot2(tempStr);
	}

	/**
	 * <pre>
	 * 类名称:Utils.java
	 * 方法名称:getCharNum
	 * 参数说明:
	 *     @param str
	 *     @return
	 * 返回类型:
	 *     @return int  为英文字符和数字
	 * 方法说明:
	 *   创建历史:
	 *      创建日期:2014-1-16 上午11:25:58
	 *      创建人员:wangshuang
	 *   修改历史:
	 *      修改人员:
	 *      修改日期:
	 *      修改目的:
	 * </pre>
	 */
	public static int getCharNum(String str) {
		int[] count = new int[2];
		// 判断每个字符
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if ((chars[i] >= 65 && chars[i] <= 90) || (chars[i] >= 97 && chars[i] <= 122)) {
				// 英文字符
				count[0]++;
			} else if (chars[i] >= 48 && chars[i] <= 57) {
				// 数字
				count[0]++;
			} else if (chars[i] == '!' || chars[i] == '[' || chars[i] == ']' || chars[i] == '-' || chars[i] == '|'
				|| chars[i] == '[') {
				count[0]++;
			} else {
				count[1]++;
			}
		}
		return count[0];
	}

	/**
	 * <pre>
	 * 类名称:Utils.java
	 * 方法名称:float2String
	 * 参数说明:
	 *     @param f
	 *     @return
	 * 返回类型:
	 *     @return String
	 * 方法说明:
	 *   创建历史:
	 *      创建日期:Jan 19, 2014 6:38:09 PM
	 *      创建人员:noway
	 *   修改历史:
	 *      修改人员:
	 *      修改日期:
	 *      修改目的:
	 * </pre>
	 */
	public static String float2String(float f) {
		DecimalFormat format = new DecimalFormat("##0.0");
		return format.format(f);
	}

	/**
	 * <pre>
	 * 类名称:Utils.java
	 * 方法名称:isToday
	 * 参数说明:
	 *     @param time 一个表示时间的long值
	 *     @return
	 * 返回类型:
	 *     @return boolean  是否是今天的时间，true是，false 不是
	 * 业务处理描述:
	 * 		可见性原因:需要被其他应用调用
	 * 		目的:方法说明.
	 * 		适用的前提条件:
	 * 		后置条件:
	 * 例外处理:无
	 * 已知问题:
	 *   创建历史:
	 *      创建日期:2014年5月10日 下午1:31:00
	 *      创建人员:z_dlong
	 *   修改历史:
	 *      修改人员:
	 *      修改日期:
	 *      修改目的:
	 * 调用的例子:
	 * 是否建议使用:
	 * </pre>
	 */
	public static boolean isToday(long time) {
		if (time > getActiveDate()[0] && time < getActiveDate()[1]) {
			return true;
		}
		return false;
	}

	/**
	 * <pre>
	 * 类名称:Utils.java
	 * 方法名称:getActiveDate
	 * 参数说明:
	 *     @return
	 * 返回类型:
	 *     @return Long[]  当天时间long的上下限
	 * 业务处理描述:
	 * 		可见性原因:需要被其他应用调用
	 * 		目的:方法说明.
	 * 		适用的前提条件:
	 * 		后置条件:
	 * 例外处理:无
	 * 已知问题:
	 *   创建历史:
	 *      创建日期:2014年5月10日 下午1:30:48
	 *      创建人员:z_dlong
	 *   修改历史:
	 *      修改人员:
	 *      修改日期:
	 *      修改目的:
	 * 调用的例子:
	 * 是否建议使用:
	 * </pre>
	 */
	public static Long[] getActiveDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Calendar cal = sdf.getCalendar();
		try {
			cal.setTime(sdf.parse(sdf.format(System.currentTimeMillis())));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
		Long[] res = new Long[2];
		res[0] = cal.getTimeInMillis();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		res[1] = cal.getTimeInMillis();
		return res;
	}

	/**
	 * <pre>
	 * 类名称:Utils.java
	 * 方法名称:getNumberTowDecimal
	 * 参数说明: 保留一位小数
	 *     @param price
	 *     @return
	 * 返回类型:
	 *     @return String
	 * 方法说明:
	 *   创建历史:
	 *      创建日期:2014-5-19 下午5:30:32
	 *      创建人员:wangshuang
	 *   修改历史:
	 *      修改人员:
	 *      修改日期:
	 *      修改目的:
	 * </pre>
	 */
	public static String getNumberTowDecimal(String price) {
		if ("0.0".equals(price)) {
			return "0";
		}
		return new DecimalFormat("#.0").format(Double.parseDouble(price));
	}

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static String trim(String valueStr) {
		if (TextUtils.isEmpty(valueStr)) {
			return "";
		}
		return valueStr.trim();
	}

	public static boolean isEqualsZero(BigDecimal bigDecimal){
		if(bigDecimal == null) return true ;
		return bigDecimal.compareTo(BigDecimal.ZERO) == 0 ;
	}

	/**
	 * 当BigDecimal 为空，则直接转化成值为0的对象
	 * @param bigDecimal
	 * @return
     */
	public static BigDecimal emptyReturnToZero(BigDecimal bigDecimal){
		if(bigDecimal == null) return BigDecimal.ZERO ;
		return bigDecimal ;
	}

	public static boolean isNotEmpty(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	/**
	 * 构建列表对象
	 * @param t
	 * @param <T>
     * @return
     */
	public static <T> List<T> asList(T t){
		List<T> list = new ArrayList<>();
		list.add(t);

		return list;
	}


	public static int size(Collection<?> collection) {
		if (collection == null) return 0;
		return collection.size();
	}



	public static Animation shakeAnimation(int counts) {

		Animation translateAnimation = new TranslateAnimation(0, 15, 0, 0);
		// RotateAnimation animation =new
		// RotateAnimation(-4f,4f,Animation.RELATIVE_TO_SELF,
		// 0.5f,Animation.RELATIVE_TO_SELF,0.5f);

		// 设置一个循环加速器，使用传入的次数就会出现摆动的效果。

		translateAnimation.setInterpolator(new CycleInterpolator(counts));

		translateAnimation.setDuration(500);
		// animation.setInterpolator(new
		// CycleInterpolator(counts))
		//
		// animation.setDuration(400);

		return translateAnimation;
	}


	/**
	 * 如果两个数值相等就返回true
	 *
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean isEquals(BigDecimal value1, BigDecimal value2) {
		if (value1 == null && value2 == null) {
			return true;
		}
		if (value1 == null || value2 == null || value1.compareTo(value2) != 0) {
			return false;
		}
		return true;
	}

	/**
	 * 如果两个数值不相等就返回true
	 *
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static boolean isNotEquals(BigDecimal value1, BigDecimal value2) {
		return !isEquals(value1, value2);
	}

	/**
	 *
	 * @Author zhaos@shishike.com
	 * @Title: checkPointIndex
	 * @Description: 验证小数点后只能输入一位小数
	 * @Param @param value
	 * @Param @param index
	 * @Param @return TODO
	 * @Return Boolean 返回类型
	 */
	public static Boolean checkPointOneIndex(String value) {
		int pointIndex = value.indexOf('.');
		if (pointIndex >= 0 && (value.length() - 1) - pointIndex > 1) {
			return false;
		}
		return true;
	}

	/**
	 *
	 * @Author zhaos@shishike.com
	 * @Title: checkPointTwoIndex
	 * @Description: 验证小数点后只能输入两位小数
	 * @Param @param value
	 * @Param @param index
	 * @Param @return TODO
	 * @Return Boolean 返回类型
	 */
	public static Boolean checkPointTwoIndex(String value) {
		int pointIndex = value.indexOf('.');
		if (pointIndex >= 0 && (value.length() - 1) - pointIndex > 2) {
			return false;
		}
		return true;
	}

	private static long lastClickTime;

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 将单个数据组装成一个数组
	 *
	 * @Author renlx@shishike.com
	 * @Title: entity2List
	 * @Param @param t
	 * @Return List<T> 返回类型
	 */
	public static <T> List<T> entity2List(T t) {
		List<T> list = new ArrayList<T>();
		list.add(t);

		return list;
	}

	/**
	 * 比较两个数是否相等
	 * @param n1
	 * @param n2
	 */
	public static boolean equals(Number n1, Number n2){
		if(n1 == null || n2 == null){
			return false;
		}

		return n1.equals(n2);
	}

	public static int add(int a,int b){
		return a+b;
	}

	/**
	 * 使用*代替字符串中某些字符
	 * @param account
	 * @param start 开始位置,传正值
	 * @param offset 显示*的长度,传正值
	 * @return
	 */
	public static String useStarReplace(String account, int start, int offset) {
		StringBuilder builder = new StringBuilder();

		int end = start + offset;
		if (account.length() > start && account.length() < end) {
			builder.append(account.substring(0, start));
			for (int i = 0; i <= account.length() - offset; i++) {
				builder.append("*");
			}
		} else if (account.length() >= end) {
			builder.append(account.substring(0, start));
			for (int i = 0; i < offset; i++) {
				builder.append("*");
			}
			builder.append(account.substring(end, account.length()));
		} else {
			builder.append(account);
		}

		return builder.toString();
	}

	/**
	 * 对操作员、会员等名称长度做处理，返回处理后的字符串
	 * @param name
	 * @return
     */
	public static String getDisplayName(String name){
		return getDisplayStr(name, 5);
	}

	/**
	 * 对字符串进行最长显示处理
	 * @param name
	 * @param maxDisplayLength
     * @return
     */
	public static String getDisplayStr(String name, int maxDisplayLength){
		if (name != null && name.length() > maxDisplayLength) {
			StringBuilder builder = new StringBuilder(name.substring(0, maxDisplayLength - 1));
			builder.append("...");
			return builder.toString();
		} else {
			return name;
		}
	}

	/**
	 * 把年月日时分秒格式转成年月日格式
	 * @param dateStr
	 * @return
     */
	public static String toDate(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

		Date date = sdf.parse(dateStr);
		return sdfnew.format(date);
	}

	/**
	 * @param urlstr,url字串
	 * @param map 参数以map形式封装起来
	 * @return 在get方式访问网络时把url和map参数 组合成url?param1=XXX&param2=XXX的字串类型,做数据转换
	 */
	public static String createGetUrl(final String urlstr,
									  final Map<String, String> map) {
		StringBuilder sb = new StringBuilder(urlstr);
		int len = 0;
		int mapsize = 0;
		if (map != null && map.size() > 0) {
			sb.append("?");
			mapsize = map.size();
			for (String key : map.keySet()) {
				try {
					len++;
					sb.append(key);
					sb.append("=");
					sb.append(URLEncoder.encode(map.get(key).trim(), "utf-8"));
					if (len < mapsize) {
						sb.append("&");
					}
				} catch (Exception e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
		return sb.toString().trim();
	}

	/**
	 * @param urlstr,url字串
	 * @param map          参数以map形式封装起来
	 * @return 在get方式访问网络时把url和map参数 组合成url?param1=XXX&param2=XXX的字串类型,不做数据转换
	 */
	public static String createGetUrl2(final String urlstr,
									   final Map<String, String> map) {
		StringBuilder sb = new StringBuilder(urlstr);
		int len = 0;
		int mapsize = 0;
		if (map != null && map.size() > 0) {
			sb.append("?");
			mapsize = map.size();
			for (String key : map.keySet()) {
				try {
					len++;
					sb.append(key);
					sb.append("=");
					sb.append(map.get(key).trim());
					if (len < mapsize) {
						sb.append("&");
					}
				} catch (Exception e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
		return sb.toString().trim();
	}

	public static int toInt(String string) {
		try {
			return Integer.parseInt(string);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return 0;
		}
	}

	public static long toLong(String string) {
		if (string == null || string.length() == 0) {
			return 0;
		}
		try {
			return Long.parseLong(string);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return 0;
		}
	}

	public static String listToString(List<String> stringList){
		if(stringList == null || stringList.size() == 0){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < stringList.size(); i++) {
			if(i == stringList.size()-1){
				builder.append(stringList.get(i));
			}else {
				builder.append(stringList.get(i)).append(",");
			}
		}
		return builder.toString();
	}
}
