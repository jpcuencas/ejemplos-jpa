package org.expertojava.jpa.empleados.modelo;

import javax.persistence.Embeddable;

@Embeddable
public class Direccion {
    private String campus;
    private String edificio;

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

}
