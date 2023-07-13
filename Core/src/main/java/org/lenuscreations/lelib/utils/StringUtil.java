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

    public static String[] shift(String[] args, String target) {
        if (args.length == 0) return new String[0];
        String[] args2 = new String[args.length - 1];

        boolean found = false;
        for (int i = 1; i < args.length; i++) {
            if (args[i].equalsIgnoreCase(target) && !found) {
                found = true;
                continue;
            }

            args2[i - 1] = args[i];
        }

        return args2;
    }

}
