package org.example.dao;

import jakarta.persistence.PersistenceException;
import org.example.entidades.Personajes;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

/*
DAO: Data Access Object
Patrón de diseño en el cual una clase se encarga de las operaciones de persistencia contra
una tabla de la base de datos
Implementa la lógica de acceso a los datos utilizando Hibernate
 */
public class PersonajesDao implements PersonajesDAOInterface {

    @Override
    public List<Personajes> devolverTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        // Consulta HQL para obtener todos los personajes
        List<Personajes> todos = session.createQuery("from Personajes", Personajes.class).list();
        session.close();
        return todos;
    }

    @Override
    public Personajes buscarPorId(long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        // Buscar un personaje por su ID
        Personajes personaje = session.find(Personajes.class, id);
        session.close();
        return personaje;
    }

    @Override
    public Personajes create(Personajes personaje) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            session.save(personaje); // Guardar el personaje en la base de datos
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return personaje;
    }

    @Override
    public Personajes actualizar(Personajes personaje) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            session.update(personaje); // Actualizar el personaje en la base de datos
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return personaje;
    }

    @Override
    public boolean deleteById(long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            Personajes personaje = this.buscarPorId(id); // Buscar el personaje por ID
            if (personaje != null) {
                session.delete(personaje); // Eliminar el personaje si existe
            } else {
                return false; // Si no existe, retornar false
            }
            session.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        } finally {
            session.close();
        }
        return true;
    }
}