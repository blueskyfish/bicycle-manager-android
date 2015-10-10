package de.kirchnerei.bicycle.helper;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.kirchnerei.bicycle.R;

public class Formatter {

    private final Context mContext;

    private final SimpleDateFormat mDateFormat;
    private final DecimalFormat mNumberFormat;

    public Formatter(Context context) {
        this.mContext = context;

        String dateFormat = context.getString(R.string.unit_format_date);
        this.mDateFormat = new SimpleDateFormat(dateFormat);

        this.mNumberFormat = new DecimalFormat("#.0");
        DecimalFormatSymbols numberSymbols = new DecimalFormatSymbols();
        String separator = context.getString(R.string.unit_format_separator);
        char sign = separator != null && !separator.isEmpty() ? separator.charAt(0) : '.';
        numberSymbols.setDecimalSeparator(sign);
        mNumberFormat.setDecimalFormatSymbols(numberSymbols);
    }

    public String from(Date date) {
        return mDateFormat.format(date);
    }

    public String from(int number, Unit unit) {
        String value = mNumberFormat.format((double) number / 10);
        return mContext.getString(unit.getFormatId(), value);
    }

    public Date toDate(String s) {
        try {
            return mDateFormat.parse(s);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
