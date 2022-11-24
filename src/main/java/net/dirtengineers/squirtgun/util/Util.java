package net.dirtengineers.squirtgun.util;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import oshi.util.tuples.Quartet;

public class Util {

    public static ImmutableTriple<Integer, Integer, Integer> getRGBfromInt(int pValue) {
        int RED = ((pValue >> 16 & 255) / 255);
        int GREEN = ((pValue >> 8 & 255) / 255);
        int BLUE = ((pValue & 255) / 255);
        return new ImmutableTriple<>(RED, GREEN, BLUE);
    }

    public static ImmutableTriple<Integer, Integer, Integer> getRGBfromHexString(String pValue) {
        int value;
        try {
            value = Integer.parseInt(pValue, 16);
        } catch (Exception e) {
            value = 0;
        }
        return getRGBfromInt(value);
    }

    public static Quartet<Integer, Integer, Integer, Integer> getARGBfromInt(int pValue) {
        int ALPHA = ((pValue >> 24 & 255) / 255);
        int RED = ((pValue >> 16 & 255) / 255);
        int GREEN = ((pValue >> 8 & 255) / 255);
        int BLUE = ((pValue & 255) / 255);
        return new Quartet<>(ALPHA, RED, GREEN, BLUE);
    }

    public static Quartet<Integer, Integer, Integer, Integer> getARGBfromHexString(String pValue) {
        int value;
        try {
            value = Integer.parseInt(pValue, 16);
        } catch (Exception e) {
            value = 0;
        }
        return getARGBfromInt(value);
    }
}
