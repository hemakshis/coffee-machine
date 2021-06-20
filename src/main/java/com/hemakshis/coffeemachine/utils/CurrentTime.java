package com.hemakshis.coffeemachine.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CurrentTime {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public String getCurrentTime() {
        return dtf.format(LocalDateTime.now());
    }
}
