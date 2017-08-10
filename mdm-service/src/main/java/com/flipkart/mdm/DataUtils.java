package com.flipkart.mdm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by setu.poddar on 11/08/17.
 */
public class DataUtils {

    private static final DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

    public static Date getDate(String date) throws ParseException {
        return format.parse(date);
    }
}
