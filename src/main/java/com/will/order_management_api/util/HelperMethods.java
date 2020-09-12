package com.will.order_management_api.util;

import org.apache.commons.validator.GenericValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * This class provides two frequently used helper methods
 * @author Will
 */


public class HelperMethods {
    //to check if the input date String is a reasonable date (based on SimpleDateFormat format)
    //eg:
    // abcd-d2-3 will not pass the validator
    // 1995-2-5 will pass
    public static boolean isValidDateString(String dateString) {
        return GenericValidator.isDate(dateString, "yyyy-MM-dd", true);
    }

    //if one date string is not valid, the output will be Optional.Empty();
    //if date string is valid, Optional object will contain the correct date object;
    public static Optional<Date> StringToDate(String dateString) {

        Optional<Date> result = null;

        if(!isValidDateString(dateString)) {
            result =  Optional.empty();
        } else {

            try {
                result =  Optional.of(new SimpleDateFormat("yyyy-MM-dd").parse(dateString));
            } catch(ParseException e) {

            }
        }
        return result;
    }
}
