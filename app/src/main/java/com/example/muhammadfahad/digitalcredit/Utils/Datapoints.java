package com.example.muhammadfahad.digitalcredit.Utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;


import com.example.muhammadfahad.digitalcredit.Model.MobileBean;
import com.example.muhammadfahad.digitalcredit.client.ApiClient;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Datapoints {
    private static Datapoints datapoints;
    private Datapoints() {
    }

    public static Datapoints getInstance() {
        if(datapoints==null){
            datapoints=new Datapoints();
        }
        return datapoints;
    }

    public boolean sms(Context context, Helper helper, PrintWriter printWriter) {
        int recId=0;
        int catId=0;
        boolean response=false;
        int userId= Integer.parseInt(helper.getSession(context).get("user_id").toString());
        Cursor cursor = null;
        List<MobileBean> list;
        try {
           catId= ApiClient.getInstance().getCategory("sms").execute().body().intValue();
            list = new ArrayList<>();

            cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI/*Uri.parse("content://sms/inbox")*/, null/*new String[]{"body", "error_code", "rcs_message_id", "rcs_message_type"}*/,
                    /*"date BETWEEN "+startDate+" AND "+endDate*/ null, null, null);
            if (cursor.moveToFirst()) { // must check the result to prevent exception
                do {
                    recId++;
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        MobileBean bean;
                        if (cursor.getColumnName(i).equalsIgnoreCase("body")) {
                            String patternToMatch = "[\\\\!\"#$%&()*+/;<=>?@\\[\\]^_{|}~|\\s]+";
                            Pattern p = Pattern.compile(patternToMatch);
                            Matcher m = p.matcher(cursor.getString(i));
                            if (m.find()) {
                                bean = new MobileBean(userId,catId,recId, cursor.getColumnName(i), m.replaceAll(" "));
                            } else {
                                bean = new MobileBean(userId,catId,recId, cursor.getColumnName(i), m.replaceAll(" "));
                            }
                        } else {
                            bean = new MobileBean(userId,catId,recId, cursor.getColumnName(i), cursor.getString(i));
                        }
                        list.add(bean);
                    }
                } while (cursor.moveToNext());
            }
            if (list.size() > 0) {
                helper.OpenCsv(list,printWriter);
                response= true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return response;
    }
}
