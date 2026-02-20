package com.hogwai.springdatajpabatchdml.util;

import java.util.Random;

/**
 * Utility for generating random uppercase alphabetic strings.
 */
public class StringUtil {
    private static final Random random = new Random();

    private StringUtil() {
    }

    public static String generateRandomString() {
        // create a string of all characters
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // specify length of random string
        int length = 7;

        for (int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphabet.length());

            // get character specified by index
            // from the string
            char randomChar = alphabet.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
