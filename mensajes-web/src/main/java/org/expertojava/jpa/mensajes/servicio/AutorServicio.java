package org.expertojava.jpa.mensajes.servicio;


import org.expertojava.jpa.mensajes.modelo.Autor;
import org.expertojava.jpa.mensajes.modelo.Mensaje;
import org.expertojava.jpa.mensajes.persistencia.AutorDao;
import org.expertojava.jpa.mensajes.persistencia.MensajeDao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class AutorServicio {

    @Inject
    AutorDao autorDao;
    @Inject
    MensajeDao mensajeDao;

    public Autor createAutorMensaje(String nombre,
                                    String correo,
                                    String texto) {
        Autor autor = new Autor(nombre, correo);
        autor = autorDao.create(autor);
        Mensaje mensaje = new Mensaje(texto, autor);
        mensaje = mensajeDao.create(mensaje);
        return autor;
    }

    public List<Autor> listAllAutores() {
        List<Autor> autores = autorDao.listAllAutores();
        return autores;
    }
}
