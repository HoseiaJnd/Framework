# Framwork
S4- Framework Spring 

FrontController
    Introduction
        Le FrontController est un servlet conçu pour centraliser la gestion des requêtes HTTP dans une application web Java. Il permet de mapper les URLs aux méthodes correspondantes dans les classes contrôleurs, offrant ainsi une structure de gestion des routes flexible et modulaire.

    Prérequis
        Avant d'utiliser le FrontController, assurez-vous d'avoir :

        -Java 8 ou une version ultérieure
        -Servlet API 4.0 ou une version supérieure
        -Jakarta Servlet API 5.0 (remplace javax.servlet)
        -Un conteneur de servlets compatible (comme Tomcat, Jetty, etc.)
    Configuration
        Annotations
        Le FrontController utilise deux annotations personnalisées :

        1.Annotation @Annotation : Cette annotation marque une classe comme étant un contrôleur.
        2.Annotation @GET : Cette annotation permet de mapper une méthode à une URL GET spécifique.
        
        Assurez-vous que votre projet dispose de ces annotations dans le package com.annotation.

    Classe Mapping
        La classe Mapping est utilisée pour stocker les associations entre les URLs et les méthodes dans les classes contrôleurs. Assurez-vous que la classe Mapping se trouve dans le package com.mapping.

    Servlet FrontController
        Le FrontController est un servlet responsable de la gestion des requêtes HTTP et de la résolution des mappings. Pour l'utiliser, assurez-vous que la classe FrontController est correctement configurée dans votre application web et que le fichier web.xml est mis à jour pour mapper les requêtes vers ce servlet.

    Utilisation
        1.Créez vos contrôleurs :
            Annotez vos classes contrôleurs avec @Annotation.
            Annotez les méthodes que vous souhaitez mapper avec @GET et spécifiez l'URL correspondante.
        2.Démarrez votre serveur :
            Déployez votre application sur un serveur compatible.
        3.Testez votre application :
            Accédez aux URLs mappées pour voir les réponses générées par le FrontController.
    
    Exemple
        Voici un exemple simple d'utilisation du FrontController :

        java(Copier le code):
            package com.controller;

            import com.annotation.Annotation;
            import com.annotation.GET;

            @Annotation
            public class MyController {

                @GET("/hello")
                public void hello() {
                    System.out.println("Hello, world!");
                }
            }
            
        Dans cet exemple, la méthode hello() du contrôleur MyController est mappée à l'URL /hello pour les requêtes HTTP GET.
