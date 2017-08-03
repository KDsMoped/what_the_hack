package de.hsd.hacking.Utils;

import com.badlogic.gdx.Gdx;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 *
 * @author Julian
 */
public final class DateUtils {
    /**
     * converts days starting from 1 to an date object starting 1.1.
     * @param days number in days starting with 1
     * @return
     */
    public static Date ConvertDaysToDate(int days) {
        Date date = new Date();

        try {
            date = new SimpleDateFormat("D").parse(String.valueOf(days));
        }
        catch (Exception e) {
            Gdx.app.log(Constants.TAG, e.getMessage());
        }

        return date;
    }

    /**
     * converts an date object starting 1.1. to days
     * @param date
     * @return
     */
    public static int ConvertDateToDays(Date date) {
        int days = (int)date.getTime() / (24 * 60 * 60 * 1000) + 2;

        return days;
    }
}
