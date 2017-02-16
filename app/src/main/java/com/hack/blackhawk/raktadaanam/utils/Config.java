package com.hack.blackhawk.raktadaanam.utils;

/**
 * Created by alenave on 16/07/16.
 */
public interface Config {
    static final String ENVIRONMENT = "PRODUCTION";//DEVELOPMENT|PRODUCTION
    static final String API_URL_DEVELOPMENT="http://www.adatech.in/";
    static final String API_URL_PRODUCTION="http://www.adatech.in/";
    static final String API_URL=ENVIRONMENT.equals("PRODUCTION")?API_URL_PRODUCTION:API_URL_DEVELOPMENT;
    static final String SEGEMENT_KEY = "";
//    static final String
}
