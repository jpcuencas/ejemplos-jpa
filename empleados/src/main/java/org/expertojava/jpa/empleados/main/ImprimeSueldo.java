package org.expertojava.jpa.empleados.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expertojava.jpa.empleados.modelo.Empleado;
import org.expertojava.jpa.empleados.persistencia.EmpleadoDao;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ImprimeSueldo {

    private static Log logger = LogFactory.getLog(ImprimeSueldo.class);

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence
                .createEntityManagerFactory("empleados-mysql");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        EmpleadoDao empleadoDao = new EmpleadoDao(em);

        try {
            tx.begin();
            Empleado emp1 = em.find(Empleado.class, 1L);
            System.out.println("Sueldo actual: " + emp1.getSueldo());
            tx.commit();
        } catch (RuntimeException ex) {
            try {
                tx.rollback();
                logger.error("Transacción deshecha", ex);
            } catch (RuntimeException rbEx) {
                logger.error("No se ha podido deshacer la transacción", rbEx);
            }
            throw ex;
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