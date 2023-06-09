package etu2006.framework.servlet;
import etu2006.framework.model.Employe;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.View;

import org.apache.commons.io.FilenameUtils;

public class FrontServlet extends HttpServlet {
      HashMap<String,Mapping> MappingUrls;
    public void init (){
        // PrintWriter out = response.getWriter();
        MappingUrls = new HashMap<>();
          try {
            String directory =getServletContext().getRealPath("/WEB-INF/etu2006.framework.servlet/model");
            String [] classe = reset(directory);
            for(int i =0 ;i< classe.length; i++){
                 String className = classe[i];
                className = "etu2006.framework.model." +className;
                Class<?> clazz;
                clazz = Class.forName(className);
                Method [] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                     Annotation[] an = method.getAnnotations();
                     if(an.length!=0){
                         GetUrl annotation = method.getAnnotation(GetUrl.class);
                         MappingUrls.put(annotation.url(),new Mapping(className,method.getName()));
                     }
                }


            }
         } catch (Exception ex) {
              ex.printStackTrace();
         }
    }
    public String[] reset(String Directory){
        ArrayList<String> rar=new ArrayList<>();
        File dossier = new File(Directory);
        String[] contenu = dossier.list();
        for(int i=0; i<contenu.length; i++){
            String fe=FilenameUtils.getExtension(contenu[i]);
            if(fe.equalsIgnoreCase("java")){
                String [] value = contenu[i].split("[.]");
                rar.add(value[0]);
            }
        }
       return rar.toArray(new String[rar.size()]); 
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
         PrintWriter out = response.getWriter();
        //  out.println(MappingUrls.size());
        //  String url=request.getRequestURI();
                // RequestDispatcher dispat=request.getRequestDispatcher("url.jsp");
                // dispat.forward(request,response);

                out.println(MappingUrls.values());
                out.println(MappingUrls.size());
                out.println(request.getRequestURI().replace(request.getContextPath()+"/",""));

                try{
                    Mapping m = MappingUrls.get(request.getRequestURI().replace(request.getContextPath()+"/",""));
                    Object o = Class.forName(m.getClassName()).getConstructor().newInstance() ;            
                    Object vao = o.getClass().getMethod(m.getMethod()).invoke(o);
                    String urls = "/"+vao.getClass().getSimpleName()+".jsp";
                    ModelView view = new ModelView(urls);
                        out.println("ok");
                        Employe emp = new Employe("Aro", 20);
                        Employe emp1 = new Employe("Rotsy", 20);

                        view.addItem("Personne", emp);
                        view.addItem("Personne", emp1);

                        HashMap<String, Object> donnees = view.getData();
                        Collection<Object>objet = donnees.values();
                        for(Object value : objet){
                            request.setAttribute("Personne",value);
                            // out.println(value);
                        }
                        RequestDispatcher dispat = request.getRequestDispatcher(view.getUrl());
                        dispat.forward(request, response);
                }catch(Exception e){
                    out.println(e);
                }
           
        }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          
          try {
              processRequest(request, response);
          } catch (Exception ex) {
              Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
          }
         
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
          try {
              processRequest(request, response);
          } catch (Exception ex) {
              Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
          }
         
    }
}