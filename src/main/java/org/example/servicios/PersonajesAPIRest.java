package org.example.servicios;

import com.google.gson.Gson;
import org.example.dao.PersonajesDAOInterface;
import org.example.entidades.Personajes;
import spark.Spark;

import java.util.List;

public class PersonajesAPIRest {
    private PersonajesDAOInterface dao;
    private Gson gson = new Gson();

    public PersonajesAPIRest(PersonajesDAOInterface implementacion){
        Spark.port(9090);

        dao = implementacion;

        // Endpoint para obtener todos los jugadores
        Spark.get("/personajes", (request, response) -> {
            List<Personajes> personajes = dao.devolverTodos();
            response.type("application/json");
            return gson.toJson(personajes);
        });

        // Endpoint para obtener un jugador por su id
        Spark.get("/personajes/id/:id", (request, response) -> {
            Long id = Long.parseLong(request.params(":id"));
            Personajes personajes = dao.buscarPorId(id);
            response.type("application/json");
            if (personajes != null) {
                return gson.toJson(personajes);
            } else {
                response.status(404);
                return "personajes no encontrado";
            }
        });

        // Endpoint para crear un nuevo personajes
        Spark.post("/personajes", (request, response) -> {
            String body = request.body();
            Personajes nuevoPersonaje = gson.fromJson(body, Personajes.class);
            Personajes creado = dao.create(nuevoPersonaje);
            response.type("application/json");
            return gson.toJson(creado);
        });

        // Endpoint para actualizar un personaje
        Spark.put("/personajes/id/:id", (request, response) -> {
            long id = Long.parseLong(request.params(":id"));
            String body = request.body();
            Personajes personajeActualizado = gson.fromJson(body, Personajes.class);
            personajeActualizado.setId(id);
            Personajes actualizado = dao.actualizar(personajeActualizado);
            if (actualizado != null) {
                return gson.toJson(actualizado);
            } else {
                response.status(404);
                return "No se ha podido actualizar el personaje";
            }
        });

        // Endpoint para eliminar un personaje
        Spark.delete("/personajes/id/:id", (request, response) -> {
            long id = Long.parseLong(request.params(":id"));
            boolean eliminado = dao.deleteById(id);
            response.type("application/json");
            if (eliminado) {
                return "personaje eliminado";
            } else {
                return "No se pudo eliminar el personaje";
            }
        });
    }
}
