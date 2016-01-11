package com.holy.jast.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.GZIPOutputStream;

public class Date {

    public static String httpDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat greenwichDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        greenwichDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        return  greenwichDate.format(cal.getTime());
    }



}