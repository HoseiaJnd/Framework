package com.controller;

import com.annotation.Annotation;
import com.annotation.GET;
import com.mapping.Mapping;
import com.mapping.ModelView;
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
        try {
            findControllerClasses();
        } catch (Exception e) {
            logException(e);
            throw new ServletException(e);
        }
    }

    public void findControllerClasses() throws Exception {
        String controllerPackage = getServletConfig().getInitParameter("controller");
        if (controllerPackage == null || controllerPackage.isEmpty()) {
            throw new Exception("Controller package not specified");
        }

        String path = controllerPackage.replace('.', '/');
        File directory = new File(getServletContext().getRealPath("/WEB-INF/classes/" + path));

        if (!directory.exists() || !directory.isDirectory()) {
            throw new Exception("Package directory not found: " + directory.getAbsolutePath());
        }

        findClassesInDirectory(controllerPackage, directory);
    }

    private void findClassesInDirectory(String packageName, File directory) throws Exception {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                findClassesInDirectory(packageName + "." + file.getName(), file);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                addClassIfController(className);
            }
        }
    }

    private void addClassIfController(String className) throws Exception {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(Annotation.class)) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(GET.class)) {
                        GET getAnnotation = method.getAnnotation(GET.class);
                        String url = getAnnotation.value();
                        if (urlMappings.containsKey(url)) {
                            throw new Exception("Duplicate URL mapping found for: " + url);
                        }
                        Mapping mapping = new Mapping(clazz.getName(), method.getName());
                        urlMappings.put(url, mapping);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new Exception("Class not found: " + className, e);
        }
    }

    protected void processRequested(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
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
                Object result = method.invoke(instance);

                if (result instanceof String) {
                    out.println("Résultat de la méthode : " + result);
                } else if (result instanceof ModelView) {
                    ModelView modelView = (ModelView) result;
                    String viewUrl = modelView.getUrl();
                    Map<String, Object> data = modelView.getData();

                    // Boucle sur les données pour les mettre dans la requête
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }

                    // Dispatcher vers l'URL de la vue
                    RequestDispatcher dispatcher = req.getRequestDispatcher(viewUrl);
                    dispatcher.forward(req, res);
                } else {
                    throw new Exception("Type de retour non reconnu : " + result.getClass().getName());
                }
            } else {
                throw new Exception("Aucune méthode associée à ce chemin");
            }
        } catch (Exception e) {
            logException(e);
            sendErrorPage(res, e.getMessage());
        }
    }

    private void logException(Exception e) {
        e.printStackTrace(System.err);
    }

    private void sendErrorPage(HttpServletResponse res, String errorMessage) throws IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Error Page</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Une erreur est survenue</h1>");
        out.println("<p>" + errorMessage + "</p>");
        out.println("</body>");
        out.println("</html>");
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
