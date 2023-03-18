/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2006.framework;

/**
 *
 * @author harivonyellias
 */
public class Mapping {
    String className;
    String method;

    public String getClassName() {
        return className;
    }

    public Mapping(String className, String method) {
        this.className = className;
        this.method = method;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
