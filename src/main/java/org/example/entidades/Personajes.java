package org.example.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "personajes")
public class Personajes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "apodo", length = 100, nullable = false)
    private String apodo;

    @Column(name = "edad", nullable = false)
    private int edad;

    @Column(name = "trabajo", length = 100, nullable = false)
    private String trabajo;

    // Constructor vacío (obligatorio para JPA)
    public Personajes() {
    }

    // Constructor con todos los campos
    public Personajes(Long id, String nombre, String apodo, int edad, String trabajo) {
        this.id = id;
        this.nombre = nombre;
        this.apodo = apodo;
        this.edad = edad;
        this.trabajo = trabajo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(String trabajo) {
        this.trabajo = trabajo;
    }

    // Método toString
    @Override
    public String toString() {
        return "Personajes{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apodo='" + apodo + '\'' +
                ", edad=" + edad +
                ", trabajo='" + trabajo + '\'' +
                '}';
    }
}
