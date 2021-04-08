package org.expertojava.jpa.empleados;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.expertojava.jpa.empleados.modelo.Departamento;
import org.expertojava.jpa.empleados.modelo.Empleado;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.*;
import java.lang.Object;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.Assert.*;


public class TestsSesion5 {
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

    @Test
    public void testBuscaSueldoEmpleadoDynamicQuery() {
        double sueldo = findSalarioPorDeptoNombre("DCCIA", "Domingo Gallardo");
        assertTrue(sueldo == 2100.0);
    }

    private double findSalarioPorDeptoNombre(String nombreDepto, String nombreEmpleado) {
        final String SALARIO_POR_DEPTO_NOMBRE = "SELECT e.sueldo " +
                "FROM Empleado e " +
                "WHERE e.departamento.nombre = :deptNombre AND " +
                "      e.nombre = :empNombre";
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Double> query = em.createQuery(SALARIO_POR_DEPTO_NOMBRE, Double.class)
                .setParameter("deptNombre", nombreDepto)
                .setParameter("empNombre", nombreEmpleado);
        double sueldo = query.getSingleResult();
        System.out.println(sueldo);
        em.getTransaction().commit();
        em.close();
        return sueldo;
    }

    @Test
    public void testBuscaEmpleadoPorNombre() {
        Empleado empleado = findEmpleadoPorNombre("Francisco Moreno");
        assertTrue(empleado.getId() == 3L);
    }

    private Empleado findEmpleadoPorNombre(String nombreEmpleado) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Empleado> queryEmpleado =
                em.createNamedQuery("Empleado.findByNombre", Empleado.class)
                        .setParameter("nombre", nombreEmpleado);
        Empleado empleado = queryEmpleado.getSingleResult();
        em.getTransaction().commit();
        em.close();
        return (empleado);
    }

    @Test
    public void testListaEmpleados() {
        List<Empleado> empleados = findAllEmpleados();
        assertTrue(empleados.size() == 3);
    }

    private List<Empleado> findAllEmpleados() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Empleado> queryListaEmpleados =
                em.createNamedQuery("Empleado.findAll", Empleado.class);
        List<Empleado> empleados = queryListaEmpleados.getResultList();
        em.getTransaction().commit();
        em.close();
        return (empleados);
    }

    @Test
    public void testEmpleadosProyecto() {
        List<Empleado> empleados = findEmpleadosProyecto("Reconocimiento de caras");
        for (Empleado empleado : empleados) {
            System.out.println(empleado.getNombre());
        }
        assertTrue(empleados.size() == 2);
    }

    private List<Empleado> findEmpleadosProyecto(String nombreProyecto) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Empleado> queryEmpleadosProyecto =
                em.createQuery(
                        "SELECT e " +
                                "FROM Proyecto p JOIN p.empleados e " +
                                "WHERE p.nombre = ?1", Empleado.class);

        List<Empleado> empleados = queryEmpleadosProyecto
                .setParameter(1, nombreProyecto)
                .getResultList();
        em.getTransaction().commit();
        em.close();
        return empleados;
    }

    @Test
    public void testFindAllEmpleados() {
        List<Empleado> empleados = findAllEmpleados2();
        assertTrue(empleados.size() == 3);
    }

    private List<Empleado> findAllEmpleados2() {
        final String FIND_ALL_EMPLEADOS = "SELECT e FROM Empleado e ";

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Empleado> query = em.createQuery(FIND_ALL_EMPLEADOS, Empleado.class);

        List<Empleado> empleados = query.getResultList();

        if (empleados != null) {
            for (Empleado empleado : empleados) {
                System.out.println(empleado.getNombre() + " - " +
                        empleado.getDespacho().getCodigoDespacho());
            }
        }

        return empleados;
    }

    @Test
    public void testFindAllSueldosEmpleados() {
        List<Double> sueldosEmpleados = findAllSueldosEmpleados();
        assertTrue(sueldosEmpleados.size() == 3);
    }

    private List<Double> findAllSueldosEmpleados() {
        final String FIND_ALL_SUELDOS_EMPLEADOS = "SELECT e.sueldo FROM Empleado e ";

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Double> query = em.createQuery(FIND_ALL_SUELDOS_EMPLEADOS, Double.class);

        List<Double> sueldosEmpleados = query.getResultList();

        if (sueldosEmpleados != null) {
            for (Double sueldoEmpleado : sueldosEmpleados) {
                System.out.println(sueldoEmpleado);
            }
        }
        em.getTransaction().commit();
        em.close();
        return sueldosEmpleados;
    }

    @Test
    public void testFindAllDepartamentos() {
        List<Departamento> departamentos = findAllDepartamentos();
        assertTrue(departamentos.size() == 3);
    }

    private List<Departamento> findAllDepartamentos() {
        final String FIND_ALL_DEPARTAMENTOS = "SELECT e.departamento FROM Empleado e ";

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Departamento> query = em.createQuery(FIND_ALL_DEPARTAMENTOS, Departamento.class);

        List<Departamento> departamentos = query.getResultList();

        if (departamentos != null) {
            for (Departamento departamento : departamentos) {
                System.out.println(departamento.getNombre());
            }
        }
        em.getTransaction().commit();
        em.close();
        return departamentos;
    }


    @Test
    public void testFindAllEmpleadosWhere() {
        List<Empleado> empleados = findAllEmpleadosWhere();
        assertTrue(empleados.size() == 2);
    }

    private List<Empleado> findAllEmpleadosWhere() {
        final String FIND_ALL_EMPLEADOS_WHERE = "SELECT e FROM Empleado e " +
                "WHERE e.departamento.nombre LIKE '%IA' AND" +
                " e.sueldo BETWEEN 2000 AND 2500";

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

    @Test
    public void testFindNombreSueldo() {
        List<Object[]> tuplas = findNombreSueldo();
        assertTrue(tuplas.size() == 3);
    }

    private List<Object[]> findNombreSueldo() {
        final String FIND_NOMBRE_SUELDO = "SELECT e.nombre, e.sueldo FROM Empleado e";

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Object[]> query = em.createQuery(FIND_NOMBRE_SUELDO, Object[].class);

        List<Object[]> tuplas = query.getResultList();

        if (tuplas != null) {
            for (Object[] tupla : tuplas) {
                String nombre = (String) tupla[0];
                Double sueldo = (Double) tupla[1];
                System.out.println(nombre + " - " + sueldo);
            }
        }
        em.getTransaction().commit();
        em.close();
        return tuplas;
    }

    @Test
    public void testGastosDccia() {
        List<String> conceptos = findGastosDccia();
        assertTrue(conceptos.size() == 4);
    }

    private List<String> findGastosDccia() {
        final String FIND_GASTOS_DCCIA = "SELECT f.concepto" +
                "   FROM Empleado e, FacturaGasto f" +
                "   WHERE e = f.empleado AND" +
                "         e.departamento.nombre='DCCIA'";

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<String> query = em.createQuery(FIND_GASTOS_DCCIA, String.class);

        List<String> conceptos = query.getResultList();

        if (conceptos != null) {
            for (String concepto : conceptos) {
                System.out.println(concepto);
            }
        }
        em.getTransaction().commit();
        em.close();
        return conceptos;
    }

    @Test
    public void testGastosDccia2() {
        List<String> conceptos = findGastosDccia2();
        assertTrue(conceptos.size() == 4);
    }

    private List<String> findGastosDccia2() {
        final String FIND_GASTOS_DCCIA = "SELECT f.concepto" +
                "   FROM Empleado e JOIN e.gastos f" +
                "   WHERE e.departamento.nombre='DCCIA'";

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<String> query = em.createQuery(FIND_GASTOS_DCCIA, String.class);

        List<String> conceptos = query.getResultList();

        if (conceptos != null) {
            for (String concepto : conceptos) {
                System.out.println(concepto);
            }
        }
        em.getTransaction().commit();
        em.close();
        return conceptos;
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
