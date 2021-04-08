package org.expertojava.jpa.mensajes.persistencia;

import org.expertojava.jpa.mensajes.modelo.Mensaje;

import javax.persistence.Query;
import java.util.List;

public class MensajeDao extends Dao<Mensaje, Long> {
    String FIND_ALL_MENSAJES = "SELECT m FROM Mensaje m";

    @Override
    public Mensaje find(Long id) {
        return em.find(Mensaje.class, id);
    }

    public List<Mensaje> listAllMensajes() {
        Query query = em.createQuery(FIND_ALL_MENSAJES);
        return (List<Mensaje>) query.getResultList();
    }
}
