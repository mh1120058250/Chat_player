package com.amap.map3d.demo.chat_player.util;

/**
 * 2017/7/17.
 */

public class StringUtils {
    private static final String USER_NAME_REGEX = "^[a-zA-Z]\\w{2,19}$";
    private static final String USER_PSW_REGEX = "[0-9]{3,20}";

    public static boolean checkUserName(String userName)
    {
        return userName.matches(USER_NAME_REGEX);
    }
    public static boolean checkUserPsw(String psw)
    {
        return psw.matches(USER_PSW_REGEX);
    }
}
