package org.expertojava.jpa.empleados.persistencia;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EmfSingleton {
    private static EmfSingleton ourInstance =
            new EmfSingleton();
    static private final String PERSISTENCE_UNIT_NAME = "empleados-mysql";
    private EntityManagerFactory emf = null;

    public static EmfSingleton getInstance() {
        return ourInstance;
    }

    private EmfSingleton() {
    }

    public EntityManagerFactory getEmf() {
        if (this.emf == null)
            this.emf = Persistence
                    .createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        return this.emf;
    }
}
