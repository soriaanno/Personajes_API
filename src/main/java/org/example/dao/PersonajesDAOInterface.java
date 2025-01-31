package org.example.dao;

import org.example.entidades.Personajes;

import java.util.List;

public interface PersonajesDAOInterface {

    List<Personajes> devolverTodos();

    Personajes buscarPorId(long id);

    Personajes create(Personajes personaje);

    Personajes actualizar(Personajes personaje);

    boolean deleteById(long id);
}