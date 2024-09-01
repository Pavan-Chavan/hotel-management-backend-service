package com.teams.exception;

/**
 * @author dgardi
 */
public class HotelManagementException extends RuntimeException{
    public HotelManagementException(String message){
        super(message);
    }

    public HotelManagementException(String message,Exception e){
        super(message,e);
    }

    public HotelManagementException(Exception e){
        super(e);
    }
}
