package com.spark.swarajyabiz.Adapters;

import java.util.ArrayList;
import java.util.Arrays;

public class StringSplit {
    public static boolean matchStrings(String value1, String value2) {
        // Split value1 on '&&' delimiter
        String[] values = value1.split("&&");

        // Check if value2 matches any of the split values
        for (String val : values) {
            if (val.trim().equals(value2.trim())) {
                return true; // Match found
            }
        }

        return false; // No match found
    }
}