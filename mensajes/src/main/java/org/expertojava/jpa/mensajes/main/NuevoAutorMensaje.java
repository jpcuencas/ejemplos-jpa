package org.expertojava.jpa.mensajes.main;

import org.expertojava.jpa.mensajes.modelo.Autor;
import org.expertojava.jpa.mensajes.modelo.Mensaje;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class NuevoAutorMensaje {

    public static void main(String[] args) {
        Autor autor;

        EntityManagerFactory emf = Persistence
                .createEntityManagerFactory("mensajes-mysql");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            String email = leerTexto("Introduce el correo electrónico: ");
            String nombre = leerTexto("Introduce nombre: ");
            autor = new Autor(nombre, email);
            em.persist(autor);
            System.out.println("Identificador del autor: " + autor.getId());

            String mensajeStr = leerTexto("Introduce mensaje: ");
            Mensaje mens = new Mensaje(mensajeStr, autor);
            mens.setFecha(new Date());
            em.persist(mens);

            System.out.println("Identificador del mensaje: " + mens.getId());

            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error: " + ex.getMessage() + "\n\n");
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    static private String leerTexto(String mensaje) {
        String texto;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in));
            System.out.print(mensaje);
            texto = in.readLine();
        } catch (IOException e) {
            texto = "Error";
        }
        return texto;
    }
}
