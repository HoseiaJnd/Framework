package com.controller;

import com.annotation.AnnotationController;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FrontController extends HttpServlet {
    
    private boolean checked = false;
    private List<Class<?>> controllerClasses = new ArrayList<>();

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
        checked = true;
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
            if (clazz.isAnnotationPresent(AnnotationController.class)) {
                controllerClasses.add(clazz);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + className);
        }
    }

    protected void processRequested(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();
        try {
            String url = req.getRequestURL().toString();
            out.println("URL: " + url);

            if (!checked) {
                findControllerClasses();
            }

            out.println("Liste des classes controleurs :");
            for (Class<?> controllerClass : controllerClasses) {
                out.println(controllerClass.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequested(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequested(req, res);
    }
}
