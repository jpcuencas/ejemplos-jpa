package org.expertojava.jpa.mensajes.persistencia;

import org.expertojava.jpa.mensajes.modelo.Autor;

import javax.persistence.Query;
import java.util.List;

public class AutorDao extends Dao<Autor, Long> {
    String FIND_ALL_AUTORES = "SELECT a FROM Autor a ";

    @Override
    public Autor find(Long id) {
        return em.find(Autor.class, id);
    }

    public List<Autor> listAllAutores() {
        Query query = em.createQuery(FIND_ALL_AUTORES);
        return (List<Autor>) query.getResultList();
    }
}
