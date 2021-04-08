package org.expertojava.jpa.empleados.persistencia;

import org.expertojava.jpa.empleados.modelo.Empleado;
import org.expertojava.jpa.empleados.modelo.Proyecto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class EmpleadoDao extends Dao<Empleado, Long> {
    String FIND_ALL_EMPLEADOS = "SELECT e FROM Empleado e ";

    String FIND_ALL_SUELDOS_EMPLEADOS =
            "SELECT e.sueldo FROM Empleado e";

    String FIND_PROJECTOS_EMPLEADOS_DEPARTAMENTO = "SELECT DISTINCT p" +
            " FROM Departamento d JOIN d.empleados e JOIN e.proyectos p" +
            " WHERE d.nombre='DLSI'";

    public EmpleadoDao(EntityManager em) {
        super(em);
    }

    @Override
    public Empleado find(Long id) {
        EntityManager em = this.getEntityManager();
        return em.find(Empleado.class, id);
    }

    public List<Empleado> listAllEmpleados() {
        EntityManager em = this.getEntityManager();
        Query query = em.createQuery(FIND_ALL_EMPLEADOS);
        return (List<Empleado>) query.getResultList();
    }

    public List<Double> listAllSueldosEmpleados() {
        EntityManager em = this.getEntityManager();
        Query query = em.createQuery(FIND_ALL_SUELDOS_EMPLEADOS);
        return (List<Double>) query.getResultList();
    }

    public List<Proyecto> listAllProyectosEmpleadosDepartamento(String nombreDepto) {
        EntityManager em = this.getEntityManager();
        Query query = em.createQuery(FIND_PROJECTOS_EMPLEADOS_DEPARTAMENTO);
        return (List<Proyecto>) query.getResultList();
    }
}
