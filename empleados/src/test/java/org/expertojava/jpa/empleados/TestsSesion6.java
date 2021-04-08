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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class TestsSesion6 {
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


    // Se ejecuta una vez después de todos los tests
    @AfterClass
    public static void closeEntityManagerFactory() throws Exception {
        // Borramos todos los datos y cerramos la conexión
        // DatabaseOperation.DELETE_ALL.execute(connection, dataset);
        if (emf != null)
            emf.close();
    }
}
