package com.adatech.blackhawk.raktadaanam.utils;

/**
 * Created by alenave on 16/07/16.
 */
public interface Config {
    static final String ENVIRONMENT = "STAGING";//DEVELOPMENT/STAGING|PRODUCTION
    static final String API_URL_DEVELOPMENT="https://arcane-anchorage-94769.herokuapp.com/";
    static final String API_URL_STAGING="https://arcane-anchorage-94769.herokuapp.com/";
    static final String API_URL_PRODUCTION="https://arcane-anchorage-94769.herokuapp.com/";
    static final String API_URL=ENVIRONMENT.equals("PRODUCTION")?API_URL_PRODUCTION:(ENVIRONMENT.equals("STAGING")?API_URL_STAGING:API_URL_DEVELOPMENT);
    static final String SEGEMENT_KEY = "";
//    static final String
}
