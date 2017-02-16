package com.hack.blackhawk.raktadaanam.utils;

/**
 * Created by dharmendra on 06/02/17.
 */

public class CustomDate {

    //Check date if in dd/MM/yyyy
    public static boolean checkDateDDmmYYYY(String dateString) {
        String[] a = dateString.split("/");
        boolean result = false;
        try {
            int date = Integer.parseInt(a[0]);
            int month = Integer.parseInt(a[1]);
            int year = Integer.parseInt(a[2]);
            if (year > 1900 && year <= 9999){
                if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && date <=31) {
                    result = true;
                } else if(month == 2) {
                    result = isLeapYear(year) && date<=29 ? true : date <29 ? true : false;
                } else if (month < 12 && date <31) {
                    result = true;
                }
            }
        } catch (Exception e) {
            System.out.print("Error while converting dateString to number");
        }
        finally {
            return result;
        }
    }

    public static boolean isLeapYear(int year) {

        if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
            return true;
        }
        return false;
    }
}
