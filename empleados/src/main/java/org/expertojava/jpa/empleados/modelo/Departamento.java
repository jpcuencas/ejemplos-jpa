package org.expertojava.jpa.empleados.modelo;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Departamento {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    @Embedded()
    private Direccion direccion;
    @OneToMany(mappedBy = "departamento")
    private Set<Empleado> empleados;

    public Departamento() {}

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
        this.getEmpleados().add(empleado);
        // Se actualiza la relación inversa y la BD
        empleado.setDepartamento(this);
    }

    public void quitaEmpleado(Empleado empleado) {
        this.getEmpleados().remove(empleado);
        // Se actualiza la relación inversa y la BD
        empleado.quitaDepartamento();
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Departamento that = (Departamento) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
