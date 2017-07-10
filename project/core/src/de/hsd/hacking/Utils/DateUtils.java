package de.hsd.hacking.Utils;

import com.badlogic.gdx.Gdx;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ju on 06.07.17.
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
}
