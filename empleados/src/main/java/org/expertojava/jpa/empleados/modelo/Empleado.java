package org.expertojava.jpa.empleados.modelo;

import javax.persistence.*;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(name="Empleado.findAll",
                query="SELECT e FROM Empleado e"),
        @NamedQuery(name="Empleado.findById",
                query="SELECT e FROM Empleado e WHERE e.id = :id"),
        @NamedQuery(name="Empleado.findByNombre",
                query="SELECT e FROM Empleado e WHERE e.nombre = :nombre")
})
public class Empleado {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    @Column(nullable = false)
    private Double sueldo;
    @OneToOne
    @JoinColumn(name = "despacho_id", unique = true)
    private Despacho despacho;
    @OneToMany(mappedBy = "empleado", fetch = FetchType.EAGER)
    private Set<CuentaCorreo> correos;
    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Proyecto> proyectos;
    @OneToMany(mappedBy = "empleado", fetch = FetchType.EAGER)
    private Set<FacturaGasto> gastos;

    public Empleado() {}

    public Empleado(Double sueldo) {
        this.sueldo = sueldo;
    }

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

    public Double getSueldo() {
        return sueldo;
    }

    public void setSueldo(Double sueldo) {
        this.sueldo = sueldo;
    }

    public Despacho getDespacho() {
        return despacho;
    }

    public void setDespacho(Despacho despacho) {
        this.despacho = despacho;
        // Actualizamos relación inversa en memoria
//        if (despacho.getEmpleado() != this) {
//            despacho.setEmpleado(this);
//        }
    }

    public void quitaDespacho() {
        if (this.getDespacho() != null) {
            // Actualizamos la relación inversa en memoria
//            this.getDespacho().quitaEmpleado();
            this.despacho = null;
        }
    }

    public Set<CuentaCorreo> getCorreos() {
        return correos;
    }

    public void setCorreos(Set<CuentaCorreo> correos) {
        this.correos = correos;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
        // Se actualiza la relación en memoria
        departamento.getEmpleados().add(this);
    }

    public void quitaDepartamento() {
        Departamento departamento = this.getDepartamento();
        this.departamento = null;
        // Se actualiza la relación en memoria
        departamento.getEmpleados().remove(this);
    }

    public Set<Proyecto> getProyectos() {
        return proyectos;
    }

    public void añadeProyecto(Proyecto proyecto) {
        this.getProyectos().add(proyecto);
        proyecto.getEmpleados().add(this);
    }

    public void quitaProyecto(Proyecto proyecto) {
        this.getProyectos().remove(proyecto);
        proyecto.getEmpleados().remove(this);
    }

    public Set<FacturaGasto> getGastos() {
        return gastos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Empleado empleado = (Empleado) o;

        return !(id != null ? !id.equals(empleado.id) : empleado.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
