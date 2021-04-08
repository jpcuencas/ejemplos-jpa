package mensajes;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertNotNull;

public class TestEmf {

    @Test
    public void createEntityManagerTest() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("mensajes");
        EntityManager em = emf.createEntityManager();
        assertNotNull(em);
        em.close();
    }
}