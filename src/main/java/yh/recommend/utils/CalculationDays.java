package yh.recommend.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalculationDays {

    public long CalculationDay(String time1,String time2){
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd");
        Date sdate = null;
        Date eDate = null;
        try {
            sdate = df.parse(time1);
            eDate = df.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long betweendays = ( long) ((eDate.getTime() - sdate.getTime())
                / ( 1000 * 60 * 60 * 24) + 0.5); // 天数间隔
        return betweendays;
    }

    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
