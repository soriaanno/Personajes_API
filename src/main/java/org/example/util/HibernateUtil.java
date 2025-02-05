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

                // Variables específicas de Railway
                String host = System.getenv("MYSQLHOST");
                String port = System.getenv("MYSQLPORT");
                String database = System.getenv("MYSQLDATABASE");
                String user = System.getenv("MYSQLUSER");
                String password = System.getenv("MYSQLPASSWORD");

                // Construir URL de conexión
                String dbUrl = String.format("jdbc:mysql://%s:%s/%s", host, port, database);

                // Si no hay variables de Railway, intentar con las variables generales
                if (host == null || port == null) {
                    dbUrl = System.getenv("DATABASE_URL");
                    user = System.getenv("DATABASE_USER");
                    password = System.getenv("DATABASE_PASSWORD");
                }

                // Verificar que tenemos las credenciales necesarias
                if (dbUrl == null || user == null || password == null) {
                    throw new RuntimeException("Faltan variables de entorno necesarias para la conexión a la base de datos");
                }

                // Configurar propiedades de Hibernate
                properties.put("hibernate.connection.url", dbUrl);
                properties.put("hibernate.connection.username", user);
                properties.put("hibernate.connection.password", password);
                properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
                properties.put("hibernate.show_sql", "true");
                properties.put("hibernate.hbm2ddl.auto", "update");

                StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .applySettings(properties)
                        .build();

                sessionFactory = new MetadataSources(registry)
                        .addAnnotatedClass(Personajes.class)
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