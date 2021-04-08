package org.expertojava.jpa.empleados.main;

import org.expertojava.jpa.empleados.modelo.Departamento;
import org.expertojava.jpa.empleados.modelo.Empleado;
import org.expertojava.jpa.empleados.modelo.FacturaGasto;
import org.expertojava.jpa.empleados.modelo.Proyecto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class PruebaQuery {
    private static EntityManagerFactory emf;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        emf = Persistence
                .createEntityManagerFactory("empleados-mysql");

        //
        // Query 1
        //

        EntityManager em = emf.createEntityManager();

        String FIND_ALL_EMPLEADOS = "SELECT e FROM Empleado e ";

        System.out.println("------Query 1: " + FIND_ALL_EMPLEADOS);

        Query query = em.createQuery(FIND_ALL_EMPLEADOS);

        List<Empleado> empleados = query.getResultList();
        if (empleados != null) {
            for (Empleado empleado : empleados) {
                System.out.println(empleado.getNombre());
            }
        }

        em.close();

        //
        // Query 2b
        //

        em = emf.createEntityManager();

        String FIND_ALL_SUELDOS_EMPLEADOS =
                "SELECT e.sueldo FROM Empleado e";

        System.out.println("------Query 2b: " + FIND_ALL_SUELDOS_EMPLEADOS);

        query = em.createQuery(FIND_ALL_SUELDOS_EMPLEADOS);

        List<Double> sueldos = query.getResultList();
        if (sueldos != null) {
            for (Double sueldo : sueldos) {
                System.out.println(sueldo);
            }
        }

        em.close();

        //
        // Query 2
        //

        em = emf.createEntityManager();

        String FIND_DEPARTAMENTOS_ALL_EMPLEADOS =
                "SELECT e.departamento FROM Empleado e";

        System.out.println("------Query 2: " + FIND_DEPARTAMENTOS_ALL_EMPLEADOS);

        query = em.createQuery(FIND_DEPARTAMENTOS_ALL_EMPLEADOS);

        List<Departamento> departamentos = query.getResultList();
        if (departamentos != null) {
            for (Departamento departamento : departamentos) {
                System.out.println(departamento.getNombre());
            }
        }

        em.close();


        //
        // Query 3
        //

        em = emf.createEntityManager();

        String FIND_EMPLEADO_WHERE=
                "SELECT e FROM Empleado e WHERE e.departamento.nombre LIKE '%IA' AND e.sueldo BETWEEN 2000 AND 2500";

        System.out.println("------Query 3: " + FIND_EMPLEADO_WHERE);

        query = em.createQuery(FIND_EMPLEADO_WHERE);

        List<Empleado> empleados2 = query.getResultList();
        if (empleados2 != null) {
            for (Empleado empleado : empleados2) {
                System.out.println(empleado.getNombre());
            }
        }

        em.close();


        //
        // Query 4
        //

        em = emf.createEntityManager();

        String PROYECCION_NOMBRE_SALARIO=
                "SELECT e.nombre, e.sueldo FROM Empleado e";

        System.out.println("------Query 4: " + PROYECCION_NOMBRE_SALARIO);

        query = em.createQuery(PROYECCION_NOMBRE_SALARIO);

        List result = query.getResultList();
        Iterator tuplas = result.iterator();
        while (tuplas.hasNext()) {
            Object[] tupla = (Object[]) tuplas.next();
            String nombre = (String) tupla[0];
            Double salario = (Double) tupla[1];
            System.out.println(nombre + "--" + salario);
        }

        em.close();

        //
        // Query 5
        //

        em = emf.createEntityManager();

        String JOIN_EMPLEADO_FACTURA=
                "SELECT f.concepto FROM Empleado e, FacturaGasto f WHERE e = f.empleado AND e.departamento.nombre='DCCIA'";

        System.out.println("------Query 5: " + JOIN_EMPLEADO_FACTURA);

        query = em.createQuery(JOIN_EMPLEADO_FACTURA);

        List<String>conceptos = query.getResultList();
        if (conceptos != null) {
            for (String concepto : conceptos) {
                System.out.println(concepto);
            }
        }

        em.close();


        //
        // Query 6
        //

        em = emf.createEntityManager();

        String JOIN_EMPLEADO_FACTURA_2=
                "SELECT f.concepto FROM Empleado e JOIN e.gastos f WHERE e.departamento.nombre='DCCIA'";

        System.out.println("------Query 6: " + JOIN_EMPLEADO_FACTURA_2);

        query = em.createQuery(JOIN_EMPLEADO_FACTURA_2);

        List<String>conceptos2 = query.getResultList();
        if (conceptos2 != null) {
            for (String concepto : conceptos2) {
                System.out.println(concepto);
            }
        }

        em.close();

        //
        // Query 7
        //

        em = emf.createEntityManager();

        String FIND_DEPTOS_DISTINTOS=
                "SELECT DISTINCT e.departamento FROM Empleado e";

        System.out.println("------Query 7: " + FIND_DEPTOS_DISTINTOS);

        query = em.createQuery(FIND_DEPTOS_DISTINTOS);

        List<Departamento> departamentos2 = query.getResultList();
        if (departamentos2 != null) {
            for (Departamento departamento : departamentos2) {
                System.out.println(departamento.getNombre());
            }
        }

        //
        // Query 8
        //

        em = emf.createEntityManager();

        String FIND_DEPTOS_DISTINTOS_2=
                "SELECT DISTINCT d FROM Empleado e JOIN e.departamento d";

        System.out.println("------Query 8: " + FIND_DEPTOS_DISTINTOS_2);

        query = em.createQuery(FIND_DEPTOS_DISTINTOS_2);

        List<Departamento> departamentos4 = query.getResultList();
        if (departamentos4 != null) {
            for (Departamento departamento : departamentos4) {
                System.out.println(departamento.getNombre());
            }
        }

        em.close();

        //
        // Query 9
        //

        em = emf.createEntityManager();

        String FIND_DEPTOS_UA_PROYECTO =
                "SELECT DISTINCT d FROM Empleado e JOIN e.departamento d JOIN e.proyectos p WHERE d.direccion.campus='UA' AND p.nombre='Reconocimiento de caras'";

        System.out.println("------Query 9: " + FIND_DEPTOS_UA_PROYECTO);

        query = em.createQuery(FIND_DEPTOS_UA_PROYECTO);

        List<Departamento> departamentos3 = query.getResultList();
        if (departamentos3 != null) {
            for (Departamento departamento : departamentos3) {
                System.out.println(departamento.getNombre());
            }
        }

        em.close();


        //
        // Query 10
        //

        em = emf.createEntityManager();

        String FIND_PROYECTOS_DEPTO =
                "SELECT DISTINCT p FROM Departamento d JOIN d.empleados e JOIN e.proyectos p WHERE d.nombre='DLSI'";

        System.out.println("------Query 10: " + FIND_PROYECTOS_DEPTO);

        query = em.createQuery(FIND_PROYECTOS_DEPTO);

        List<Proyecto> proyectos = query.getResultList();
        if (proyectos != null) {
            for (Proyecto proyecto : proyectos) {
                System.out.println(proyecto.getNombre());
            }
        }

        em.close();



        //
        // Query 11
        //

        em = emf.createEntityManager();

        String JOIN_FETCH =
                "SELECT e FROM Empleado e JOIN FETCH e.departamento";

        System.out.println("------Query 11: " + JOIN_FETCH);

        query = em.createQuery(JOIN_FETCH);

        List<Empleado> empleados3 = query.getResultList();

        em.close();

        if (empleados3 != null) {
            for (Empleado empleado : empleados3) {
                System.out.println(empleado.getDepartamento().getDireccion().getEdificio());
            }
        }


        buscaTodosLosEmpleadosConCriteria();
        emf.close();
    }

    private static void buscaTodosLosEmpleadosConCriteria() {
        EntityManager em = emf.createEntityManager();
        System.out.println("------Query 1---- API CRITERIA -----");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Empleado> cq = cb.createQuery(Empleado.class);
        Root<Empleado> e = cq.from(Empleado.class);
        cq.select(e);
        Query query = em.createQuery(cq);
        List<Empleado> emps = query.getResultList();
        if (emps != null) {
            for (Empleado empleado : emps) {
                System.out.println(empleado.getNombre());
            }
        }
        em.close();
    }
}
