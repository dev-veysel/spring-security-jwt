package com.hacker.domain.enums;



public enum RoleType {

    USER("USER"),
    ADMIN("ADMIN");


    private String name;

    //Constructor
    RoleType(String name){
        this.name=name;
    }

    //Getter
    public String getName(){
        return name;
    }

}