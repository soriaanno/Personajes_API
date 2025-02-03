package org.example.util;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.entidades.Personajes;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Properties properties = new Properties();

                // Obtener variables de entorno o valores por defecto
                String dbUrl = System.getenv("DATABASE_URL");
                String dbUser = System.getenv("DATABASE_USER");
                String dbPassword = System.getenv("DATABASE_PASSWORD");

                // Si no hay variables de entorno, intentar cargar desde .env
                if (dbUrl == null || dbUser == null || dbPassword == null) {
                    try {
                        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
                        dbUrl = dotenv.get("DATABASE_URL");
                        dbUser = dotenv.get("DATABASE_USER");
                        dbPassword = dotenv.get("DATABASE_PASSWORD");
                    } catch (Exception e) {
                        throw new RuntimeException("No se pudieron cargar las variables de entorno");
                    }
                }

                // Configurar propiedades de Hibernate
                properties.put("hibernate.connection.url", dbUrl);
                properties.put("hibernate.connection.username", dbUser);
                properties.put("hibernate.connection.password", dbPassword);

                // Otras configuraciones de Hibernate
                properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
                properties.put("hibernate.show_sql", "true");
                properties.put("hibernate.hbm2ddl.auto", "create");

                StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .applySettings(properties)
                        .build();

                sessionFactory = new MetadataSources(registry)
                        .addAnnotatedClass(Personajes.class) // Asegúrate de incluir todas tus clases de entidad
                        .buildMetadata()
                        .buildSessionFactory();

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error inicializando SessionFactory", e);
            }
        }
        return sessionFactory;
    }
}