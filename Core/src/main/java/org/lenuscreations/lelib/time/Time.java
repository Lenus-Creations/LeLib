package org.lenuscreations.lelib.time;

import lombok.Getter;

import java.util.Date;

public class Time {

    @Getter
    private Date date;
    @Getter
    private Date time;

    private final String timeRegex = "/[0-9]{2}:[0-9]{2}:[0-9]{2}/";
    private final String dateRegex = "/[0-9]{2}[-/][0-9]{2}[-/][0-9]{4}/";

    public Time(String time) {
        if (time.equalsIgnoreCase("now")) {
            this.date = new Date();
            this.time = new Date();
            return;
        }

        String[] split = time.split("\\|");
        if (split[0].matches(dateRegex)) {
            this.date = new Date(Long.parseLong(split[0]));
        } else if (split[0].matches(timeRegex)) {
            this.time = new Date(Long.parseLong(split[0]));
        }

        if (split.length > 1) {
            if (split[1].matches(dateRegex)) {
                this.date = new Date(Long.parseLong(split[1]));
            } else if (split[1].matches(timeRegex)) {
                this.time = new Date(Long.parseLong(split[1]));
            }
        }
    }

}
