package org.expertojava.jpa.empleados.modelo;

import javax.persistence.*;

@Entity
public class Despacho {
    @Id
    @GeneratedValue
    private Long id;
    private String codigoDespacho;
 //   @OneToOne(mappedBy = "despacho")
 //   private Empleado empleado;

    public Long getId() {
        return id;
    }

    public String getCodigoDespacho() {
        return codigoDespacho;
    }

    public void setCodigoDespacho(String codigoDespacho) {
        this.codigoDespacho = codigoDespacho;
    }

//    public Empleado getEmpleado() {
//        return empleado;
//    }
//
//    public void setEmpleado(Empleado empleado) {
//        this.empleado = empleado;
//        // Actualizamos la relación inversa en memoria y la BD
//        empleado.setDespacho(this);
//    }
//
//    public void quitaEmpleado() {
//        if (this.empleado != null) {
//            // Actualizamos la relación inversa en memoria y la BD
//            this.empleado.quitaDepartamento();
//        }
//        this.empleado = null;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Despacho despacho = (Despacho) o;

        return !(id != null ? !id.equals(despacho.id) : despacho.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
