package org.expertojava.jpa.mensajes.persistencia;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

abstract class Dao<T, K> {

    @PersistenceContext(unitName = "mensajes")
    EntityManager em;

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