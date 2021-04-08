package org.expertojava.jpa.empleados.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expertojava.jpa.empleados.modelo.Empleado;
import org.expertojava.jpa.empleados.persistencia.EmpleadoDao;
import org.junit.Test;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ListaSueldos {

    private static Log logger = LogFactory.getLog(ListaSueldos.class);

    public static void main(String[] args) {
        List<Empleado> empleados = findAllEmpleadosWhere();
    }

    private static List<Empleado> findAllEmpleadosWhere() {
        final String FIND_ALL_EMPLEADOS_WHERE = "SELECT e FROM Empleado e " +
                "WHERE e.departamento.nombre LIKE '%IA' AND" +
                " e.sueldo BETWEEN 2000 AND 2500";

        EntityManagerFactory emf = Persistence
                .createEntityManagerFactory("empleados-mysql");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Empleado> query = em.createQuery(FIND_ALL_EMPLEADOS_WHERE, Empleado.class);

        List<Empleado> empleados = query.getResultList();

        if (empleados != null) {
            for (Empleado empleado : empleados) {
                System.out.println(empleado.getNombre() + " - " +
                        empleado.getDespacho().getCodigoDespacho());
            }
        }
        em.getTransaction().commit();
        em.close();
        return empleados;
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