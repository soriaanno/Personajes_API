package org.example;

import org.example.dao.PersonajesDao;
import org.example.servicios.PersonajesAPIRest;

public class Main {
    public static void main(String[] args) {
        new PersonajesAPIRest((new PersonajesDao()));
        System.out.println("API REST en ejecución...");
    }
}