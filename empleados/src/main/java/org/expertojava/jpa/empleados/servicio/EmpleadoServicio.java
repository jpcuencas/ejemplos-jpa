package org.expertojava.jpa.empleados.servicio;


import org.expertojava.jpa.empleados.modelo.Despacho;
import org.expertojava.jpa.empleados.modelo.Empleado;
import org.expertojava.jpa.empleados.persistencia.EmpleadoDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EmpleadoServicio {

    private EntityManagerFactory emf;

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Empleado findPorId(Long idEmpleado) {
        EntityManager em = emf.createEntityManager();
        Empleado empleado = null;
        EmpleadoDao empleadoDao = new EmpleadoDao(em);
        try {
            em.getTransaction().begin();
            empleado = empleadoDao.find(idEmpleado);
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            em.close();
        }
        return empleado;
    }

    public void intercambiaDespachos(Long idEmpleado1, Long idEmpleado2) {
        EntityManager em = emf.createEntityManager();
        EmpleadoDao empleadoDao = new EmpleadoDao(em);
        try {
            em.getTransaction().begin();
            Empleado emp1 = empleadoDao.find(idEmpleado1);
            Empleado emp2 = empleadoDao.find(idEmpleado2);
            Despacho despacho1 = emp1.getDespacho();
            Despacho despacho2 = emp2.getDespacho();

            // Para evitar errores de claves duplicadas primero
            // quitamos el despacho de uno de los emplados y hacemos un flush

            emp2.quitaDespacho();
            em.flush();

            emp1.setDespacho(despacho2);
            emp2.setDespacho(despacho1);

            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
