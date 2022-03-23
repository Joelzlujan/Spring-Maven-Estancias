
package com.ejercicio2.Estancias.entidades;

import com.ejercicio2.Estancias.enumeraciones.Rol;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;


@Entity
public class Propietario extends Usuario{

    private String nombre;
    private Integer calificacion;
    @Lob //agregar cargas pesadas de textos
    private String descripcion;
    private String telefono;
    
    @OneToMany(mappedBy = "propietario") // cuando declaras de los dos lados tenes q decir quien es el dueño de la relacion( seria el propietario de atributo de casa
    private List <Casa> casas =new ArrayList(); //

    public List<Casa> getCasas() {
        return casas;
    }

    public void setCasas(List<Casa> casas) {
        this.casas = casas;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


   
    

    
    
}
