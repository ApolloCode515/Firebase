package com.spark.swarajyabiz.Extra;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class PrcConv {
    private static String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,##,##0.00", symbols);
        return decimalFormat.format(price);
    }
}
