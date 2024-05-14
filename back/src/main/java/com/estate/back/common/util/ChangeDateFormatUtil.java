package com.estate.back.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ChangeDateFormatUtil {
    
    public static String changeYYMMDD(String original) throws Exception {

        // 문자열을 Datetime으로 바꿔주고
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = simpleDateFormat.parse(original);
        // yy.mm.dd 형태로 바꿔줌
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        // 객체를 writeDatetime으로 전달해줌
        String writeDatetime = simpleDateFormat.format(datetime);
        return writeDatetime;
    }

    public static String changeYYMMDD1(String original) throws Exception {

        // 문자열을 Datetime으로 바꿔주고
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = simpleDateFormat.parse(original);
        // yy.mm.dd 형태로 바꿔줌
        simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        // 객체를 writeDatetime으로 전달해줌
        String writeDatetime = simpleDateFormat.format(datetime);
        return writeDatetime;
    }

    public static String changeYYMM(String original) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = simpleDateFormat.parse(original);
        simpleDateFormat = new SimpleDateFormat("yy-MM");
        String writeDatetime = simpleDateFormat.format(datetime);
        return writeDatetime;
    }
}
