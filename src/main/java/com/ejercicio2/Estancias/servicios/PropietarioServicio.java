package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Propietario;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ejercicio2.Estancias.repositorios.PropietarioRepositorio;

@Service
public class PropietarioServicio {
    
    @Autowired
    private PropietarioRepositorio pr;

    @Transactional
    public Propietario crearPropietario(String nombre, String descripcion, String telefono) throws ErrorServicio {
        validarPropietario(nombre,descripcion,telefono);
        Propietario propietario = new Propietario();
        propietario.setNombre(nombre);
        propietario.setDescripcion(descripcion);
        propietario.setTelefono(telefono);
        propietario.setCalificacion(null);
        return propietario;
    }
    
    @Transactional
    public Propietario modificarPropietario(String id,String nombre,String descripcion, String telefono)throws ErrorServicio{
        validarPropietario(nombre,descripcion,telefono);
        if(id==null || id.trim().isEmpty()){
            throw new ErrorServicio("El id no puede ser nulo");
        }
        Propietario propietario = pr.getById(id);
        if(propietario != null){
            propietario.setNombre(nombre);
            propietario.setDescripcion(descripcion);
            propietario.setTelefono(telefono);
            return pr.save(propietario);
        }else{
            throw new ErrorServicio("El propietario no estaba seteado");
        }
    }
    @Transactional(readOnly=true)
    public Propietario buscarPorId(String id){
        return pr.getById(id);
    }
    
    @Transactional(readOnly=true)
    public List<Propietario> listarPropietarios(){
        return pr.findAll();        
    }

    public void validarPropietario(String nombre,String descripcion, String telefono) throws ErrorServicio {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new ErrorServicio("La descripcion no puede ser nula o vacia");
        }
        if (telefono == null ||  telefono.length()!=10){
            throw new ErrorServicio ("El numero de telefono no puede ser nulo o el numero no puede ser menor a 10 digitos");
        }
    }
}
