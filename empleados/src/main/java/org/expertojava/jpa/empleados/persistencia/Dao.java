package org.expertojava.jpa.empleados.persistencia;


import javax.persistence.EntityManager;

abstract class Dao<T, K> {
    EntityManager em;

    public Dao(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEntityManager() {
        return this.em;
    }

    public T create(T t) {
        em.persist(t);
        em.flush();
        em.refresh(t);
        return t;
    }

    public T update(T t) {
        return (T) em.merge(t);
    }

    public void delete(T t) {
        t = em.merge(t);
        em.remove(t);
    }

    public abstract T find(K id);

}