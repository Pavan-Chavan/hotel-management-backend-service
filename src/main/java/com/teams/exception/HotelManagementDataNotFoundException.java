package com.teams.exception;

public class HotelManagementDataNotFoundException extends RuntimeException{

    public HotelManagementDataNotFoundException(String message){
        super(message);
    }

    public HotelManagementDataNotFoundException(String message,Exception e){
        super(message,e);
    }

    public HotelManagementDataNotFoundException(Exception e){
        super(e);
    }
}
