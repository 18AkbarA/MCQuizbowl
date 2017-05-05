package com.mcquizbowl.main;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class customFilter implements Filter {

@Override
public boolean isLoggable(LogRecord record) {

    if(record.getMessage().indexOf("level") > -1) {
        return false;
    }
    else {
        return true;
            }
      }
}