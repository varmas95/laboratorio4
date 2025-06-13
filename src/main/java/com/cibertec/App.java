package com.cibertec;

import java.util.Scanner;

import com.cibertec.matricula.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import org.h2.tools.Server;

import com.cibertec.matricula.model.Estudiante;
import com.cibertec.matricula.model.Curso;
import com.cibertec.matricula.model.Matricula;

public class App {
    private static Server h2Server;

    private static Server iniciarServidorH2() {
        try {
            Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println(" H2 Web Console disponible en: http://localhost:8082");
            System.out.println("Conéctate con:");
            System.out.println("JDBC URL: jdbc:h2:mem:matriculaLabDB");
            System.out.println("Usuario: sa");
            System.out.println("Contraseña: (dejar vacío)");
            return webServer;
        } catch (java.sql.SQLException e) {
            System.err.println("Error al iniciar el servidor H2");
            e.printStackTrace();
            return null;
        }
    }

    private static void pausar(Scanner scanner) {
        System.out.print("Presiona ENTER para continuar...");
        scanner.nextLine();
    }

    private static void stopH2Server() {
        if (h2Server != null) {
            h2Server.stop();
        }
    }

    public static void main(String[] args) {
        try {
            Server webServer = iniciarServidorH2();
            Scanner scanner = new Scanner(System.in);
            System.out.println(">>> SERVIDOR H2 INICIADO. Revisa la consola en http://localhost:8082 <<<");

            EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
            EntityTransaction tx = em.getTransaction();

            tx.begin();

            // 1. Creamos entidades
            Estudiante estudianteAna = new Estudiante("Ana", "Torres");
            Curso cursoCalculo = new Curso("Cálculo I");
            Curso cursoFisica = new Curso("Física I");

            // Las guardamos para que tengan un ID
            em.persist(estudianteAna);
            em.persist(cursoCalculo);
            em.persist(cursoFisica);

            // 2. Creamos las matrículas (la relación)
            Matricula matricula1 = new Matricula(estudianteAna, cursoCalculo);
            Matricula matricula2 = new Matricula(estudianteAna, cursoFisica);

            em.persist(matricula1);
            em.persist(matricula2);

            tx.commit();
            System.out.println("Estudiante Ana matriculada en Cálculo y Física.");

            // PAUSA PARA VER LA BD
            pausar(scanner);

            // 3. Consulta con JPQL
            System.out.println("\n--- Buscando los cursos de " + estudianteAna.getNombre() + " usando JPQL ---");
            String jpql = "SELECT m.curso FROM Matricula m WHERE m.estudiante.id = :idEstudiante";

            java.util.List<Curso> cursosDeAna = em.createQuery(jpql, Curso.class)
                    .setParameter("idEstudiante", estudianteAna.getId())
                    .getResultList();

            System.out.println("Cursos encontrados:");
            cursosDeAna.forEach(curso -> System.out.println("- " + curso.getNombre()));

            em.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JPAUtil.shutdown();
            stopH2Server();
            System.out.println("\n>>> APLICACIÓN FINALIZADA <<<");
        }
    }
}

