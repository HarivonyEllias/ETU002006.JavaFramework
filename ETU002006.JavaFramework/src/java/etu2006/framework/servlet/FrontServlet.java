/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu2006.framework.servlet;

import annotation.Url;
import etu2006.framework.Mapping;
import jakarta.servlet.ServletConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author root
 */
public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingUrls= new HashMap<>();
    String directory="";
    
    
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        directory = getServletContext().getInitParameter("classesContainer");
        File dir = new File(directory);
        List<Class<?>> list = scanDirectory(dir, "annotation.Url");
        fillMappingHashMap(list,"annotation.Url");
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("The path from currentURL: " + getPathFromRequest(request));
        out.println("The current classes directory is: " + directory);

        for (String urlPattern : mappingUrls.keySet()) {
            Mapping mapping = mappingUrls.get(urlPattern);
            out.print("URL pattern: " + urlPattern + " -- ");
            out.print("Class name: " + mapping.getClassName()+ " -- ");
            out.print("Method name: " + mapping.getMethod());
            out.println();
        }
    }
    
    public void fillMappingHashMap(List<Class<?>> classList,String annotationName) {
        for (Class<?> clazz : classList) {
            try {
                Method[] methods = getAnnotatedMethods(clazz, (Class<? extends Annotation>) Class.forName(annotationName));
                for (Method method : methods) {
                    if (method.isAnnotationPresent((Class<? extends Annotation>) Class.forName(annotationName))) {
                        Url methodUrlAnnotation = method.getAnnotation(Url.class);
                        Mapping mapping = new Mapping(clazz.getName(), method.getName());
                        mappingUrls.put(methodUrlAnnotation.value(), mapping);
                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<Class<?>> scanDirectory(File directory, String annotationName) {
        List<Class<?>> classesWithAnnotation = new ArrayList<>();
        File[] files = directory. listFiles(new FileFilter() {
        @Override
           public boolean accept(File file) {
                return file.isDirectory() || file.getName().endsWith(".class");
            }
        });
        for (File file : files) {
            if (file.isDirectory()) {
                classesWithAnnotation.addAll(scanDirectory(file, annotationName));
            } else {
                String className = file.getName().replace(".class", "");

                try {             
                    Class<?> clazz = Class.forName(pathToPackage(directory.getPath())+className);
                    if (containAnnotation(clazz, (Class<? extends Annotation>) Class.forName(annotationName))) {
                        classesWithAnnotation.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        return classesWithAnnotation;
    }
    
    
    public  String pathToPackage(String path) {
        if (path.endsWith("classes")){
            return "";
        } else {
            String[] res = path.split("classes/");
            return res[1].replace('/', '.')+".";
        }
    }
    
    public boolean containAnnotation(Class<?> clazz,Class<? extends Annotation> A){
       Method[] M_list=clazz.getDeclaredMethods();
       for(Method m : M_list){
           if(m.isAnnotationPresent(A))return true;
       }
       return false;
    }

    public Method[] getAnnotatedMethods(Class<?> clazz,Class<? extends Annotation> A){
        ArrayList<Method> m_list = new ArrayList<>();
        Method[] methods =clazz.getDeclaredMethods();
        for(Method m : methods){
            if(m.isAnnotationPresent(A))m_list.add(m);
        }
        return m_list.toArray(new Method[m_list.size()]);
    }
    
    private String getPathFromRequest(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestUri = request.getRequestURI();
        String path = requestUri.substring(contextPath.length());
        int queryIndex = path.indexOf('?');
        if (queryIndex >= 0) {
            path = path.substring(0, queryIndex);
        }
        return path;
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
