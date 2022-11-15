package net.dirtengineers.squirtgun.util;

import org.antlr.v4.runtime.misc.Triple;
import oshi.util.tuples.Quartet;
//TODO: change to apache.commons
public class Util {

    public static Triple<Integer, Integer, Integer> getRGBfromInt(int pValue) {
        return getRGBfromHexString(Integer.toHexString(pValue));
    }

    public static Triple<Integer, Integer, Integer> getRGBfromHexString(String pValue) {
        pValue = pValue.toUpperCase();
        pValue = String.format("%s%s", "0".repeat(6 - pValue.length()), pValue);
        int result = Integer.parseInt(pValue, 16);
        int RED = (((result) >> 16 & 255) & 0xff);
        int GREEN = (((result) >> 8 & 255) & 0xff);
        int BLUE = (((result)  & 255) & 0xff);

        return new Triple<>(RED, BLUE, GREEN);
    }

    public static Quartet<Integer, Integer, Integer, Integer> getRGBAfromInt(int pValue) {
        return getRGBAfromHexString(Integer.toHexString(pValue));
    }

    public static Quartet<Integer, Integer, Integer, Integer> getRGBAfromHexString(String pValue) {

//        FIX PADDING LATER!
        int RED = 0;
        int BLUE = 0;
        int GREEN = 0;
        int ALPHA = 255;

        if(!pValue.isEmpty()) {
            pValue = pValue.toUpperCase();
            switch (pValue.length()) {
                case 1 -> pValue = String.format("%s%s%s", "00000", pValue, "FF");
                case 2 -> pValue = String.format("%s%s%s", "0000", pValue, "FF");
                case 3 -> pValue = String.format("%s%s%s", "000", pValue, "FF");
                case 4 -> pValue = String.format("%s%s%s", "00", pValue, "FF");
                case 5 -> pValue = String.format("%s%s%s", "0", pValue, "FF");
                case 6 -> pValue = String.format("%s%s", pValue, "FF");
            }
            int result = Integer.parseInt(pValue,16);

//            static private int RGB_GREEN(int rgb) {
//                return (((rgb) >> 8) & 0xff);
//            }/

//            double red = (double)(i >> 16 & 255) / 255.0D;
//            double green = (double)(i >> 8 & 255) / 255.0D;
//            double blue = (double)(i & 255) / 255.0D;

//            GREEN = (((result) >> 8) & 0xff);
            RED = (((result) >> 24) & 0xff);
            GREEN = (((result) >> 16) & 0xff);
            BLUE = (((result) >> 8) & 0xff);
            if(pValue.length() == 8){
                ALPHA = Integer.parseInt(pValue.substring(6, 8),16);
            }
        }
        return new Quartet<>(RED, BLUE, GREEN, ALPHA);
    }
}
