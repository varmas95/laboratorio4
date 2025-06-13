package com.cibertec.matricula.model;
//Estos son los imports que requiere  La entidad
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

@Entity // Marca esta clase como una tabla de base de datos
public class Estudiante {

    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private java.util.List<Matricula> matriculas = new java.util.ArrayList<>();

    @Column(nullable = false, length = 50)
    private String apellido;
    //Crea manualmente este constructor ( es obligatorio para usar JPA)
    public Estudiante() {}
    // Constructores, Getters y Setters (necesarios para que Hibernate funcione)
    // Los Constructores y Gett Sett se pueden crear tambien en Visual Studio Code haciendo click derecho al archivo> Source Action > Generate Getters and Setters
   //  APlica lo mismo para los constructores click derecho al archivo> Source Action > Generate Constructor( y seleccionas todo (menos el Id, este se va autogenerar) 

    public Estudiante(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }
    

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    // Getter y Setter para la nueva lista
    public java.util.List<Matricula> getMatriculas() { return matriculas; }
    public void setMatriculas(java.util.List<Matricula> matriculas) { this.matriculas = matriculas; }
    
    //Sobreescribir el toString click derecho al archivo> Source Action > Generate toString
    @Override
    public String toString() {
        return "Estudiante{" + "id=" + id + ", nombre='" + nombre + "', apellido='" + apellido + "'}";
    }
}