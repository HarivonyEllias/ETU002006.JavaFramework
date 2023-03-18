/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import annotation.Url;

/**
 *
 * @author root
 */
public class Emp {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Emp(String name) {
        this.name = name;
    }
    
    @Url("Emp-name")
    public String SayMyName(){
        return "My name is"+ name;
    }
    
    @Url("Emp-Hello")
    public String SayHello(){
        return "Hello !";
    }
    
}
