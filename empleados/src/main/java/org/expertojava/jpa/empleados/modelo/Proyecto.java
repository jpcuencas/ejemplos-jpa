package org.expertojava.jpa.empleados.modelo;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Proyecto {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    @ManyToMany(mappedBy = "proyectos")
    private Set<Empleado> empleados;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Empleado> getEmpleados() {
        return empleados;
    }

    public void añadeEmpleado(Empleado empleado) {
        empleado.getProyectos().add(this);
        this.getEmpleados().add(empleado);
    }

    public void quitaEmpleado(Empleado empleado) {
        empleado.getProyectos().remove(this);
        this.getEmpleados().remove(empleado);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proyecto proyecto = (Proyecto) o;

        return !(id != null ? !id.equals(proyecto.id) : proyecto.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
