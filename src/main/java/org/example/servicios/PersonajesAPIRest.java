package org.example.servicios;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;
import org.example.dao.PersonajesDAOInterface;
import org.example.dao.PersonajesDao;
import org.example.entidades.Personajes;
import spark.Spark;

import java.util.ArrayList;
import java.util.List;

public class PersonajesAPIRest {
    private PersonajesDAOInterface dao;
    private Gson gson = new Gson();

    public PersonajesAPIRest(PersonajesDAOInterface implementacion){

        // Obtiene el valor de la variable de entorno PORT
        String puerto = System.getenv("PORT");

// Si no se encuentra la variable de entorno PORT, usar un puerto por defecto (por ejemplo, 8080)
        int port = 9090; // Valor por defecto
        if (puerto != null && !puerto.isEmpty()) {
            try {
                port = Integer.parseInt(puerto);  // Intentamos convertir el valor de puerto
            } catch (NumberFormatException e) {
                System.err.println("El valor de PORT no es válido. Usando puerto por defecto.");
            }
        }

// Configura el puerto
        Spark.port(port);

// Asigna la implementación del DAO
        dao = implementacion;


        //endpoint para obtener todos los personajes
        Spark.get("/*", (request, response) -> {
            //List<Personajes> personaje = dao.devolverTodos();
            //response.type("application/json");
            response.status(200);
            return "hola mundo";
        });

        //endpoint para obtener todos los personajes
        //Spark.get("/personajes", (request, response) -> {
            //List<Personajes> personaje = dao.devolverTodos();
            //response.type("application/json");
            //response.status(200);
            //return createJsonResponse("200", personaje);
        //});

        //endpoint para obtener un personajes por su id
        Spark.get("personaje/id/:id", (request, response) -> {
            Long id = Long.parseLong(request.params(":id"));
            Personajes personaje = dao.buscarPorId(id);
            response.type("application/json");
            List<Personajes> personajeList = new ArrayList<>();
            if(personaje != null){
                personajeList.add(personaje);
                response.status(200);
                return createJsonResponse("200", personajeList);
            }else{
                response.status(404);
                return createJsonResponse("Personaje no encontrado", personajeList);
            }
        });

        Spark.post("/personajes", (request, response) -> {
            String body = request.body();
            //TODO comprobar si han llegado los datos correctos
            Personajes nuevoPersonaje = gson.fromJson(body, Personajes.class);

            //TODO comprobar si se creado el nuevoPersonaje
            Personajes creado = dao.create(nuevoPersonaje);

            response.type("application/json");
            List<Personajes> personajeList = new ArrayList<>();
            personajeList.add(creado);
            response.status(200);
            return createJsonResponse("200", personajeList);
        });

        Spark.put("personajes/id/:id", (request, response) -> {
            Long id = Long.parseLong(request.params(":id"));
            //TODO comprobar si han llegado los datos correctos
            String body = request.body();
            Personajes personajeActualizado = gson.fromJson(body, Personajes.class);
            personajeActualizado.setId(id);
            List<Personajes> personajeList = new ArrayList<>();
            Personajes actualizado = dao.actualizar(personajeActualizado);
            response.type("application/json");
            if(actualizado != null){

                personajeList.add(actualizado);
                response.status(200);
                return createJsonResponse("200", personajeList);
            }else{
                response.status(404);
                return createJsonResponse("No se ha podido realizar la actualización", personajeList);
            }
        });

        Spark.delete("/personajes/id/:id", (request, response) -> {
            Long id = Long.parseLong(request.params(":id"));
            boolean eliminado = dao.deleteById(id);
            response.type("application/json");
            List<Personajes> personajeList = new ArrayList<>();
            if(eliminado){
                response.status(200);
                return createJsonResponse("Mueble eliminado correctamente", personajeList);
            }else{
                response.status(404);
                return createJsonResponse("No se puedo realizar la eliminación", personajeList);
            }
        });
    }

//    public String createJsonResponse(String status, String message) {
//        Gson gson = new Gson();
//
//        JsonObject jsonResponse = new JsonObject();
//        jsonResponse.addProperty("status", status);
//        jsonResponse.addProperty("message", message);
//        return gson.toJson(jsonResponse);
//    }

    public String createJsonResponse(String status, List<Personajes> messages) {
        Gson gson = new Gson();

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", status);

        // Convertir la lista de objetos a JsonArray
        JsonArray messageArray = gson.toJsonTree(messages).getAsJsonArray();
        jsonResponse.add("message", messageArray);

        return gson.toJson(jsonResponse);
    }
}
