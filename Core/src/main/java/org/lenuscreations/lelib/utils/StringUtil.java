package org.lenuscreations.lelib.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {

    public static String[] shift(String[] args) {
        if (args.length == 0) return new String[0];
        String[] args2 = new String[args.length - 1];
        System.arraycopy(args, 1, args2, 0, args.length - 1);

        return args2;
    }

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

}
