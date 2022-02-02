
package com.ejercicio2.Estancias.entidades;

import javax.persistence.Entity;
import javax.persistence.OneToOne;


@Entity
public class Familia extends Usuario{

    private String nombre;
    private Integer edadMin;
    private Integer edadMax;
    private Integer numHijos;
    @OneToOne
    private Casa casa;

//    public String getId() {
//        return id;
//    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEdadMin() {
        return edadMin;
    }

    public void setEdadMin(Integer edadMin) {
        this.edadMin = edadMin;
    }

    public Integer getEdadMax() {
        return edadMax;
    }

    public void setEdadMax(Integer edadMax) {
        this.edadMax = edadMax;
    }

    public Integer getNumHijos() {
        return numHijos;
    }

    public void setNumHijos(Integer numHijos) {
        this.numHijos = numHijos;
    }

    public Casa getCasa() {
        return casa;
    }

    public void setCasa(Casa casa) {
        this.casa = casa;
    }

    
    
}
