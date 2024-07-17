package com.hmacadamia.format;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Formatos {

    public static String formatNumber(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        try {
            text = text.replaceAll(",", "");
            Number number = Double.parseDouble(text);
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            decimalFormat.applyPattern("#,###");

            return decimalFormat.format(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return text;
        }
    }
}
