package org.expertojava.jpa.mensajes.persistencia;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EmfSingleton {
    private static EmfSingleton ourInstance =
            new EmfSingleton();
    static private final String PERSISTENCE_UNIT_NAME = "mensajes-mysql";
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
