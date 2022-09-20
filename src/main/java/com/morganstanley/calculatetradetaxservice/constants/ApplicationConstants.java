package com.morganstanley.calculatetradetaxservice.constants;

public class ApplicationConstants {

    public static final String BASE_API_URL = "morganstanley/api/v1/";
    public static final String CACHE_NAME="calculate_trade_tax_service_cache";

    public static final String SUCCESS = "SUCCESS";
    public static final String SUCCESS_MSG= "Data is successfully stored";
    public static final String TRADE_CALCULATION_SUCCESS_MSG ="Tax Due is calculated successfully";

    public static final String FAILED = "FAILED";
    public static final String ISR_MSG = "Something went wrong, please retry or contact support team";
    public static final String DATA_EXCEPTION="Total No of Share Minimum 1 purchase required or share price each cannot be null";
    public static final String TRADE_ID_NOT_FOUND_MSG="Given Trade ID is not found in the database";
}
