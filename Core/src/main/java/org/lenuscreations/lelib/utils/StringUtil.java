package org.lenuscreations.lelib.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {

    /**
     * Shifts the array by removing the first element
     * @param args The array to shift
     * @return The shifted array
     */
    public static String[] shift(String[] args) {
        if (args.length == 0) return new String[0];
        String[] args2 = new String[args.length - 1];
        System.arraycopy(args, 1, args2, 0, args.length - 1);

        return args2;
    }

    /**
     * Shifts the array by removing the first element
     * @param args The array to shift
     * @param stringToRemove The string to remove
     * @return The shifted array
     */
    public static String[] shift(String[] args, String stringToRemove) {
        if (args.length == 0) return args;

        int removeIndex = -1;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(stringToRemove)) {
                removeIndex = i;
                break;
            }
        }

        if (removeIndex == -1) return args;  // String not found

        String[] args2 = new String[args.length - 1];
        System.arraycopy(args, 0, args2, 0, removeIndex);
        System.arraycopy(args, removeIndex + 1, args2, removeIndex, args.length - removeIndex - 1);

        return args2;
    }

    /**
     * Generates a random string
     * @return The generated string
     */
    public static String randomString() {
        return randomString(10);
    }

    /**
     * Generates a random string
     * @param includeNumbers Whether to include numbers in the string
     * @return The generated string
     */
    public static String randomString(boolean includeNumbers) {
        return randomString(10, true);
    }

    /**
     * Generates a random string
     * @param length The length of the string
     * @return The generated string
     */
    public static String randomString(int length) {
        return randomString(length, false);
    }

    /**
     * Generates a random string
     * @param length The length of the string
     * @param includeNumbers Whether to include numbers in the string
     * @return The generated string
     */
    public static String randomString(int length, boolean includeNumbers) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        if (includeNumbers) chars += "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }

        return sb.toString();
    }

}
