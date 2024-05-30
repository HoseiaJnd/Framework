package com.controller;

import com.annotation.Annotation;
import com.annotation.GET;
import com.mapping.Mapping;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FrontController extends HttpServlet {

    private Map<String, Mapping> urlMappings = new HashMap<>();

    @Override
    public void init() throws ServletException {
        findControllerClasses();
    }

    public void findControllerClasses() {
        String controllerPackage = getServletConfig().getInitParameter("controller");
        if (controllerPackage == null || controllerPackage.isEmpty()) {
            System.err.println("Controller package not specified");
            return;
        }

        String path = controllerPackage.replace('.', '/');
        File directory = new File(getServletContext().getRealPath("/WEB-INF/classes/" + path));

        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Package directory not found: " + directory.getAbsolutePath());
            return;
        }

        findClassesInDirectory(controllerPackage, directory);
    }

    private void findClassesInDirectory(String packageName, File directory) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                findClassesInDirectory(packageName + "." + file.getName(), file);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                addClassIfController(className);
            }
        }
    }

    private void addClassIfController(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(Annotation.class)) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(GET.class)) {
                        GET getAnnotation = method.getAnnotation(GET.class);
                        Mapping mapping = new Mapping(clazz.getName(), method.getName());
                        urlMappings.put(getAnnotation.value(), mapping);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + className);
        }
    }

    protected void processRequested(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();
        try {
            String url = req.getRequestURL().toString();
            String contextPath = req.getContextPath();
            String path = url.substring(url.indexOf(contextPath) + contextPath.length());

            out.println("URL: " + url);
            out.println("Path: " + path);

            Mapping mapping = urlMappings.get(path);
            if (mapping != null) {
                out.println("Mapping trouvé : " + mapping);

                // Récupérer la classe et la méthode
                Class<?> clazz = Class.forName(mapping.getClassName());
                Method method = clazz.getDeclaredMethod(mapping.getMethodName());

                // Créer une instance de la classe
                Object instance = clazz.getDeclaredConstructor().newInstance();

                // Invoquer la méthode sur l'instance
                String result = (String) method.invoke(instance);

                // Afficher la valeur retournée par la méthode
                out.println("Résultat de la méthode : " + result);
            } else {
                out.println("Aucune méthode associée à ce chemin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Erreur : " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequested(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequested(req, res);
    }
}
