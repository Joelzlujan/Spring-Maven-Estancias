package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Familia;
import com.ejercicio2.Estancias.entidades.Usuario;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.repositorios.FamiliaRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FamiliaServicio extends Usuario {
    
    @Autowired
    private FamiliaRepositorio fr;

    @Transactional
    public Familia crearFamilia(String nombre, Integer edadMin, Integer edadMax, Integer numHijos) throws ErrorServicio {
        validarFamilia(nombre,edadMin,edadMax,numHijos);
        Familia familia = new Familia();
        familia.setNombre(nombre);
        familia.setEdadMin(edadMin);
        familia.setEdadMax(edadMax);
        familia.setNumHijos(numHijos);
        return familia;
    }
    
    @Transactional
    public Familia modificarFamilia(String id,String nombre,Integer edadMin,Integer edadMax,Integer numHijos)throws ErrorServicio{
        validarFamilia(nombre,edadMin,edadMax,numHijos);
        if(id==null || id.trim().isEmpty()){
            throw new ErrorServicio("El id no puede ser nulo");
        }
        Familia familia = fr.getById(id);
        if(familia != null){
            familia.setNombre(nombre);
            familia.setEdadMin(edadMin);
            familia.setEdadMax(edadMax);
            familia.setNumHijos(numHijos);
            return fr.save(familia);
        }else{
            throw new ErrorServicio("La familia no estaba seteada");
        }
    }
    @Transactional(readOnly=true)
    public Familia buscarPorId(String id){
        return fr.getById(id);
    }
    
    @Transactional(readOnly=true)
    public List<Familia> listarFamilias(){
        return fr.findAll();        
    }

    public void validarFamilia(String nombre, Integer edadMin, Integer edadMax, Integer numHijos) throws ErrorServicio {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }
        if (edadMin == null || edadMin < 0) {
            throw new ErrorServicio("La edad mínima no puede ser nula o igual a 0");
        }
        if (edadMax == null || edadMax < 0 || edadMax<edadMin) {
            throw new ErrorServicio("La edad máxima no puede ser nula o igual a 0 ni menor a la minima");
        }
        if (numHijos == null || numHijos < 0) {
            throw new ErrorServicio("La cantidad de hijos no puede ser nula o igual a 0");
        }
    }
}
