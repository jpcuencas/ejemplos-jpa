package org.expertojava.jpa.empleados;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.expertojava.jpa.empleados.modelo.Departamento;
import org.expertojava.jpa.empleados.modelo.Despacho;
import org.expertojava.jpa.empleados.modelo.Empleado;
import org.expertojava.jpa.empleados.modelo.Proyecto;
import org.expertojava.jpa.empleados.servicio.EmpleadoServicio;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.security.krb5.internal.crypto.Des;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Set;

import static org.junit.Assert.*;

public class TestsSesion4 {
    private static EntityManagerFactory emf;
    private static IDatabaseConnection connection;
    private static IDataSet dataset;


    // Se ejecuta una vez antes de todos los tests
    @BeforeClass
    public static void initDatabaseTest() {
        try {
            // Inicializamos sólo una vez el emf antes de todos los tests
            emf = Persistence.createEntityManagerFactory("empleados-mysql");

            // Inicializamos la conexión a la BD necesaria para
            // que DBUnit cargue los datos de los tests
            Class.forName("com.mysql.jdbc.Driver");
            Connection jdbcConnection = (Connection) DriverManager
                    .getConnection(
                            "jdbc:mysql://localhost:3306/jpa_empleados",
                            "root", "expertojava");
            connection = new DatabaseConnection(jdbcConnection);
            FlatXmlDataSetBuilder flatXmlDataSetBuilder =
                    new FlatXmlDataSetBuilder();
            flatXmlDataSetBuilder.setColumnSensing(true);
            dataset = flatXmlDataSetBuilder.build(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("dbunit/dataset1.xml"));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Excepción al inicializar el emf y DbUnit");
        }
    }

    // Se ejecuta antes de cada test
    @Before
    public void cleanDB() throws Exception {
        // Se hace un "clean insert" de los datos de prueba
        // definidos en el fichero XML. El "clean insert" vacía las
        // tablas de los datos de prueba y después inserta los datos
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
    }

    // Pone en el despacho del empleado 1 al empleado 3
    // y deja sin despacho al empleado 1
    @Test
    public void testCambiaDespacho() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Empleado empleado1 = em.find(Empleado.class, 1L);
        Despacho despacho1 = empleado1.getDespacho();
        Empleado empleado3 = em.find(Empleado.class, 3L);
        empleado1.quitaDespacho();
        empleado3.setDespacho(despacho1);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        despacho1 = em.find(Despacho.class, 1L);
        //Empleado empleado = despacho1.getEmpleado();
        em.getTransaction().commit();
        em.close();
        //assertTrue(empleado.equals(empleado3));
    }

    @Test
    public void testActualizaDepartamento() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Departamento depto1 = em.find(Departamento.class, 1L);
        Departamento depto2 = em.find(Departamento.class, 2L);
        Set<Empleado> empleadosDepto1 = depto1.getEmpleados();
        Set<Empleado> empleadosDepto2 = depto2.getEmpleados();
        Empleado empleado1 = em.find(Empleado.class, 1L);
        assertTrue(empleadosDepto1.contains(empleado1));
        assertFalse(empleadosDepto2.contains(empleado1));
        empleado1.quitaDepartamento();
        empleado1.setDepartamento(depto2);
        assertFalse(empleadosDepto1.contains(empleado1));
        assertTrue(empleadosDepto2.contains(empleado1));
        em.getTransaction().commit();
        em.close();
    }

    // Probamos con el otro lado de la relación
    @Test
    public void testActualizaDepartamento2() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Departamento depto1 = em.find(Departamento.class, 1L);
        Departamento depto2 = em.find(Departamento.class, 2L);
        Set<Empleado> empleadosDepto1 = depto1.getEmpleados();
        Set<Empleado> empleadosDepto2 = depto2.getEmpleados();
        Empleado empleado1 = em.find(Empleado.class, 1L);
        assertTrue(empleadosDepto1.contains(empleado1));
        assertFalse(empleadosDepto2.contains(empleado1));
        depto1.quitaEmpleado(empleado1);
        depto2.añadeEmpleado(empleado1);
        assertFalse(empleadosDepto1.contains(empleado1));
        assertTrue(empleadosDepto2.contains(empleado1));
        em.getTransaction().commit();
        em.close();
    }

    // Empleados proyecto2 = {empleado2, empleado3}
    // Proyectos empleado1 = {proyecto1}
    // Añadimos el empleado 1 al proyecto 2 usando los métodos auxiliares
    // Resultado:
    // Empleados proyecto2 = {empleado1, empleado2, empleado3}
    // Proyectos empleado1 = {proyecto1, proyecto2}
    @Test
    public void testAñadeEmpleadoProyecto() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Proyecto proyecto2 = em.find(Proyecto.class, 2L);
        Empleado empleado1 = em.find(Empleado.class, 1L);
        // Actualizamos el lado propietario de la relación
        empleado1.getProyectos().add(proyecto2);
        // Actualizamos el otro lado de la relación en memoria
        proyecto2.getEmpleados().add(empleado1);

        em.getTransaction().commit();
        em.close();

        // Comprobamos que se ha actualizado la BD

        em = emf.createEntityManager();
        em.getTransaction().begin();
        proyecto2 = em.find(Proyecto.class, 2L);
        empleado1 = em.find(Empleado.class, 1L);
        Set<Empleado> empleados = proyecto2.getEmpleados();
        Set<Proyecto> proyectos = empleado1.getProyectos();
        assertTrue(empleados.size() == 3);
        assertTrue(empleados.contains(empleado1));
        assertTrue(proyectos.size() == 2);
        assertTrue(proyectos.contains(proyecto2));
        em.getTransaction().commit();
        em.close();
    }

    // Empleados proyecto2 = {empleado2, empleado3}
    // Proyectos empleado1 = {proyecto1}
    // Añadimos el empleado 1 al proyecto 2 usando los métodos auxiliares
    // Resultado:
    // Empleados proyecto2 = {empleado1, empleado2, empleado3}
    // Proyectos empleado1 = {proyecto1, proyecto2}
    @Test
    public void testAñadeEmpleadoProyecto2() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Proyecto proyecto2 = em.find(Proyecto.class, 2L);
        Empleado empleado1 = em.find(Empleado.class, 1L);
        empleado1.añadeProyecto(proyecto2);

        // Comprobamos que la relación se ha actualizado en memoria
        Set<Empleado> empleados = proyecto2.getEmpleados();
        Set<Proyecto> proyectos = empleado1.getProyectos();
        assertTrue(empleados.size() == 3);
        assertTrue(empleados.contains(empleado1));
        assertTrue(proyectos.size() == 2);
        assertTrue(proyectos.contains(proyecto2));
        em.getTransaction().commit();
        em.close();

        // Comprobamos que se ha actualizado la BD

        em = emf.createEntityManager();
        em.getTransaction().begin();
        proyecto2 = em.find(Proyecto.class, 2L);
        empleado1 = em.find(Empleado.class, 1L);
        empleados = proyecto2.getEmpleados();
        proyectos = empleado1.getProyectos();
        assertTrue(empleados.size() == 3);
        assertTrue(empleados.contains(empleado1));
        assertTrue(proyectos.size() == 2);
        assertTrue(proyectos.contains(proyecto2));
        em.getTransaction().commit();
        em.close();
    }

    // Empleados proyecto2 = {empleado2, empleado3}
    // Proyectos empleado1 = {proyecto1}
    // Añadimos el empleado 1 al proyecto 2 (usando el otro lado de
    // la relación). Resultado:
    // Empleados proyecto2 = {empleado1, empleado2, empleado3}
    // Proyectos empleado1 = {proyecto1, proyecto2}
    @Test
    public void testAñadeEmpleadoProyecto3() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Proyecto proyecto2 = em.find(Proyecto.class, 2L);
        Empleado empleado1 = em.find(Empleado.class, 1L);
        proyecto2.añadeEmpleado(empleado1);

        // Comprobamos que se ha actualizado la BD

        Set<Empleado> empleados = proyecto2.getEmpleados();
        Set<Proyecto> proyectos = empleado1.getProyectos();
        assertTrue(empleados.size() == 3);
        assertTrue(empleados.contains(empleado1));
        assertTrue(proyectos.size() == 2);
        assertTrue(proyectos.contains(proyecto2));
        em.getTransaction().commit();
        em.close();

        // Comprobamos que se ha actualizado la BD

        em = emf.createEntityManager();
        em.getTransaction().begin();
        proyecto2 = em.find(Proyecto.class, 2L);
        empleado1 = em.find(Empleado.class, 1L);
        empleados = proyecto2.getEmpleados();
        proyectos = empleado1.getProyectos();
        assertTrue(empleados.size() == 3);
        assertTrue(empleados.contains(empleado1));
        assertTrue(proyectos.size() == 2);
        assertTrue(proyectos.contains(proyecto2));
        em.getTransaction().commit();
        em.close();
    }

    // Empleados proyecto2 = {empleado2, empleado3}
    // Proyectos empleado3 = {proyecto1, proyecto2, proyecto3}
    // Borramos el empleado 3 del proyecto 2. Resultado:
    // Empleados proyecto2 = {empleado2}
    // Proyectos empleado3 = {proyecto1, proyecto3}
    @Test
    public void testBorraEmpleadoProyecto() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Proyecto proyecto2 = em.find(Proyecto.class, 2L);
        Empleado empleado3 = em.find(Empleado.class, 3L);
        // Actualizamos el lado propietario de la relación
        empleado3.getProyectos().remove(proyecto2);
        // Actualizamos el otro lado de la relación en memoria
        proyecto2.getEmpleados().remove(empleado3);

        em.getTransaction().commit();
        em.close();

        // Comprobamos que se ha actualizado la BD

        em = emf.createEntityManager();
        em.getTransaction().begin();
        proyecto2 = em.find(Proyecto.class, 2L);
        empleado3 = em.find(Empleado.class, 3L);
        Set<Empleado> empleados = proyecto2.getEmpleados();
        Set<Proyecto> proyectos = empleado3.getProyectos();
        assertTrue(empleados.size() == 1);
        assertFalse(empleados.contains(empleado3));
        assertTrue(proyectos.size() == 2);
        assertFalse(proyectos.contains(proyecto2));
        em.getTransaction().commit();
        em.close();
    }

    // Empleados proyecto2 = {empleado2, empleado3}
    // Proyectos empleado3 = {proyecto1, proyecto2, proyecto3}
    // Borramos el empleado 3 del proyecto 2 con método helper. Resultado:
    // Empleados proyecto2 = {empleado2}
    // Proyectos empleado3 = {proyecto1, proyecto3}
    @Test
    public void testBorraEmpleadoProyecto2() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Proyecto proyecto2 = em.find(Proyecto.class, 2L);
        Empleado empleado3 = em.find(Empleado.class, 3L);
        empleado3.quitaProyecto(proyecto2);

        em.getTransaction().commit();
        em.close();

        // Comprobamos que se ha actualizado la BD

        em = emf.createEntityManager();
        em.getTransaction().begin();
        proyecto2 = em.find(Proyecto.class, 2L);
        empleado3 = em.find(Empleado.class, 3L);
        Set<Empleado> empleados = proyecto2.getEmpleados();
        Set<Proyecto> proyectos = empleado3.getProyectos();
        assertTrue(empleados.size() == 1);
        assertFalse(empleados.contains(empleado3));
        assertTrue(proyectos.size() == 2);
        assertFalse(proyectos.contains(proyecto2));
        em.getTransaction().commit();
        em.close();
    }

    // Se ejecuta una vez después de todos los tests
    @AfterClass
    public static void closeEntityManagerFactory() throws Exception {
        // Borramos todos los datos y cerramos la conexión
        // DatabaseOperation.DELETE_ALL.execute(connection, dataset);
        if (emf != null)
            emf.close();
    }
}
