package com.md.baseModule.utils;

import android.text.TextUtils;
import android.widget.TextView;


import com.md.baseModule.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 */
public class StringUtil {


    /**
     * 验证邮箱地址是否正确
     */
    public static boolean checkEmail(String email, boolean isNeedUseRegex) {
        if (isNeedUseRegex) {
            boolean flag = false;
            try {
                //            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                String check = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            } catch (Exception e) {
                flag = false;
            }
            return flag;
        } else {
            if (!TextUtils.isEmpty(email) && email.contains("@") && !email.startsWith("@") & !email.endsWith("@")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        return checkEmail(string, false);
    }

    /**
     * 验证邮箱地址是否正确
     */
    public static boolean checkEmail(TextView tv) {

        return checkEmail(getStringByTv(tv), false);
    }


    /**
     * 验证手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        if (!TextUtils.isEmpty(mobiles)) {//^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|7[0678])\\d{8}$
            try {
                Pattern p = Pattern
                        .compile("0?(11|12|13|14|15|16|17|18|19)[0-9]{9}");
                Matcher m = p.matcher(mobiles);
                flag = m.matches();
            } catch (Exception e) {
                flag = false;
            }
        }
        return flag;
    }


    /**
     * 将TextView中中文本获取到
     */
    public static String getStringByTv(TextView tv) {
        return tv.getText().toString().trim();
    }

    /**
     * 判断textview是否没有文字
     */
    public static boolean isEmpty(TextView textView) {
        return TextUtils.isEmpty(textView.getText().toString().trim());
    }


    /**
     * 获取字符串资源
     */
    public static String getString( int id) {
        return BaseApplication.getApplication().getResources().getString(id);
    }

    /**
     * 判断用户输入姓名信息是否合格
     */
    public static boolean checkNameMessage(String userName) {
        // 用户名只能是中文或者英文
        if (isChineseChar(userName) || isEnglishChar(userName)) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断用户名是否为中文
     */
    public static String getCountStr(int count) {
        String string = "";
        if (count != 0) {
            string = String.valueOf(count);
        }
        return string;
    }

    /**
     * 判断用户名是否为中文
     */
    public static boolean isChineseChar(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户名是否为英文
     */
    public static boolean isEnglishChar(String str) {
        Pattern p = Pattern.compile("^[a-zA-Z]+$");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为数字，英文，汉语中的一种
     */
    public static boolean isStringOk(String str) {
        return (isEnglishChar(str) || isChineseChar(str) || isNumeric(str)) && !StringUtil.isContainEmoji(str);
    }




    /**
     * 去掉html中的标签，提取文字
     */
    public static String removeHtmlTag(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        Pattern p_special;
        Matcher m_special;
        try {
            //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            // 定义HTML标签的正则表达式
            String regEx_html = "<[^>]+>";
            // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            String regEx_special = "\\&[a-zA-Z]{1,10};";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
            m_special = p_special.matcher(htmlStr);
            htmlStr = m_special.replaceAll(""); // 过滤特殊标签
            textStr = htmlStr.replaceAll("</?[^>]+>|\r|\n|", "").trim();
            textStr = textStr.length() <= 100 ? textStr : textStr.substring(0, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }



    /**
     * 提取字符串中的汉字
     */
    public static String extractionChinese(String str) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
        StringBuilder sb = new StringBuilder();
        Matcher m = p.matcher(str);
        while (m.find())
            for (int i = 0; i <= m.groupCount(); i++) {
                sb.append(m.group());
            }
        return sb.toString();
    }



    /**
     * 读取html中所有img标签的src值
     */
    public static List<String> getImgSrc(String htmlStr) {
        if (TextUtils.isEmpty(htmlStr) || !htmlStr.contains("<img")) {
            return new ArrayList<>();
        }
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();
//       String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            // Matcher m =
            // Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                String replace = m.group(1).trim().replace("file://", "");
                pics.add(replace);
            }
        }
        return pics;
    }


    public static boolean isContainEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }
}
