package org.expertojava.jpa.empleados;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.expertojava.jpa.empleados.modelo.Departamento;
import org.expertojava.jpa.empleados.modelo.Despacho;
import org.expertojava.jpa.empleados.modelo.Empleado;
import org.expertojava.jpa.empleados.servicio.EmpleadoServicio;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.Assert.*;

public class TestsSesion2 {
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


    // Test que añade un nuevo empleado al departamento 1
    @Test
    public void testPersist() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Departamento depto = em.find(Departamento.class, 1L);

        // Comprobamos el número de empleados del departamento
        // ¡Cuidado! Esto realiza un 'select' y se trae todos los
        // empleados a memoria
        int numEmpleadosAntes = depto.getEmpleados().size();

        // Creamos un nuevo empleado, actualizamos su departamento
        // y lo guardamos en la base de datos
        Empleado empleado = new Empleado(38000.0);
        // Comprobamos que Hibernate acutaliza el id
        em.persist(empleado);

        assertNotNull(empleado.getId());
        empleado.setNombre("Pedro");

        empleado.setDepartamento(depto);
        // Actualizamos la colección en memoria de empleados del departamento
        depto.getEmpleados().add(empleado);
        int numEmpleadosDespues = depto.getEmpleados().size();

        // Cerramos la transacción y el entity manager

        em.getTransaction().commit();
        em.close();

        assertTrue(numEmpleadosAntes + 1 == numEmpleadosDespues);

        Long empleadoId = empleado.getId();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        empleado = em.find(Empleado.class, empleadoId);
        em.getTransaction().commit();
        em.close();
        assertNotNull(empleado);
    }

    // Test que prueba el método getReference
    @Test
    public void testGetReference() {
        System.out.println("Empezamos testGetReference");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Departamento depto = em.getReference(Departamento.class, 1L);
        Empleado empleado = new Empleado(38000.0);
        em.persist(empleado);

        empleado.setNombre("Pedro");
        empleado.setDepartamento(depto);

        em.getTransaction().commit();
        em.close();

        Long empleadoId = empleado.getId();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        empleado = em.find(Empleado.class, empleadoId);
        em.getTransaction().commit();
        em.close();
        assertNotNull(empleado);
    }

    // Test que realiza varios finds para comprobar que sólo se genera un select
    @Test
    public void testPruebaCache() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Departamento depto = em.find(Departamento.class, 1L);
        Departamento depto2 = em.find(Departamento.class, 1L);
        Departamento depto3 = em.find(Departamento.class, 1L);
        Departamento depto4 = em.find(Departamento.class, 1L);

        em.getTransaction().commit();
        em.close();
    }

    // Test que prueba a actualizar una entidad
    @Test
    public void testUpdate() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Empleado empleado = em.find(Empleado.class, 2L);

        double sueldo = empleado.getSueldo();

        empleado.setSueldo(sueldo + 10000.0);

        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        empleado = em.find(Empleado.class, 2L);
        em.getTransaction().commit();
        em.close();
        assertTrue(empleado.getSueldo() == sueldo + 10000.0);
    }

    // Test que prueba a actualizar una entidad con merge
    @Test
    public void testMerge() {
        EntityManager em = emf.createEntityManager();
        Empleado empleado = em.find(Empleado.class, 2L);
        em.close();

        empleado.setNombre("Pepito Pérez");

        em = emf.createEntityManager();
        em.getTransaction().begin();
        empleado = em.merge(empleado);
        empleado.setSueldo(20000.0);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        em.getTransaction().begin();
        empleado = em.find(Empleado.class, 2L);
        em.getTransaction().commit();
        em.close();
        assertEquals("Pepito Pérez", empleado.getNombre());
        assertEquals(new Double(20000.0), empleado.getSueldo());
    }

    // Test que prueba el método remove
    @Test
    public void testRemove() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Empleado empleado = em.find(Empleado.class, 3L);
        em.remove(empleado);
        em.getTransaction().commit();
        em.close();

        em = emf.createEntityManager();
        empleado = em.find(Empleado.class, 3L);
        assertNull(empleado);
        em.close();
    }

    // Probamos la excpeción cuando intentamos borrar entidades
    // a las que apuntan otras claves ajenas
    @Test
    public void testExcepcionRemoveDespacho() {
        EntityManager em = emf.createEntityManager();
        Despacho desp = null;
        try {
            em.getTransaction().begin();
            Empleado empleado = em.find(Empleado.class, 3L);
            desp = empleado.getDespacho();
            em.remove(desp);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        // Comprobamos que el despacho no se ha borrado
        em = emf.createEntityManager();
        em.getTransaction().begin();
        desp = em.find(Despacho.class, desp.getId());
        em.close();
        em.getTransaction().commit();
        assertNotNull(desp);
    }

    // Ahora sí que lo borramos
    @Test
    public void testRemoveDespacho() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Empleado empleado = em.find(Empleado.class, 3L);
        Despacho desp = empleado.getDespacho();
        empleado.quitaDespacho();
        em.remove(desp);
        em.getTransaction().commit();
        em.close();

        // Comprobamos que el despacho se ha borrado
        // de la base de datos (aunque sigue estando en memoria)

        em = emf.createEntityManager();
        em.getTransaction().begin();
        em.find(Despacho.class, 3L);
        em.getTransaction().commit();
        em.close();
        assertNull(empleado.getDespacho());
    }


    @Test
    public void testFetchTypes() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Empleado empleado = em.find(Empleado.class, 1L);
        em.getTransaction().commit();
        em.close();

        // Comprobamos que los correos se han cargado en memoria
        // por ser el tipo Eager Fetch

        assertTrue(empleado.getCorreos().size() == 2);
    }

    @Test
    public void testServicioFindPorId() {
        EmpleadoServicio empleadoServicio = new EmpleadoServicio();
        empleadoServicio.setEmf(emf);
        Empleado emp1 = empleadoServicio.findPorId(1L);
        assertNotNull(emp1);
    }

    @Test
    public void testServicioActualizaDespacho() {
        EmpleadoServicio empleadoServicio = new EmpleadoServicio();
        empleadoServicio.setEmf(emf);

        Empleado emp1 = empleadoServicio.findPorId(1L);
        Empleado emp2 = empleadoServicio.findPorId(2L);
        Despacho desp1 = emp1.getDespacho();
        Despacho desp2 = emp2.getDespacho();

        empleadoServicio.intercambiaDespachos(1L,2L);

        emp1 = empleadoServicio.findPorId(1L);
        emp2 = empleadoServicio.findPorId(2L);

        assertTrue(emp1.getDespacho().equals(desp2));
        assertTrue(emp2.getDespacho().equals(desp1));
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
