package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Cliente;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.repositorios.ClienteRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio cp;

    @Transactional
    public Cliente crearCliente(String nombre, String calle, Integer numero, String codPostal, String ciudad, String pais) throws ErrorServicio {
        validarCliente(nombre, calle, numero, codPostal, ciudad, pais);
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setCalle(calle);
        cliente.setNumero(numero);
        cliente.setCodPostal(codPostal);
        cliente.setCiudad(ciudad);
        cliente.setPais(pais);
        return cliente;
    }

    @Transactional
    public Cliente modificarCliente(String id, String nombreC, String calle, Integer numero, String CodPostal, String ciudad, String pais) throws ErrorServicio {
        validarCliente(nombreC, calle, numero, CodPostal, ciudad, pais);
        if (id == null || id.trim().isEmpty()) {
            throw new ErrorServicio("El id no puede ser nulo");
        }
        Cliente cliente = cp.getById(id);
        if (cliente != null) {
            cliente.setNombre(nombreC);
            cliente.setCalle(calle);
            cliente.setNumero(numero);
            cliente.setCodPostal(CodPostal);
            cliente.setCiudad(ciudad);
            cliente.setPais(pais);
            return cp.save(cliente);
        } else {
            throw new ErrorServicio("El cliente buscado no existe");
        }
    }
    
    @Transactional(readOnly=true)
    public List<Cliente> listarClientes(){
        return cp.findAll();
    }
    
    @Transactional(readOnly=true)
    public Cliente buscarPorId(String id){
        return cp.getById(id);
    }

    public void validarCliente(String nombreC, String calle, Integer numero, String CodPostal, String ciudad, String pais) throws ErrorServicio {
        if (nombreC == null || nombreC.trim().isEmpty()) {
            throw new ErrorServicio("El nombre del cliente no puede ser nulo");
        }
        if (calle == null || calle.trim().isEmpty()) {
            throw new ErrorServicio("La calle no puede ser nulo");
        }
        if (numero == null || numero < 0) {
            throw new ErrorServicio("El numero no puede ser nulo o menor a 0");
        }
        if (CodPostal == null || CodPostal.trim().isEmpty()) {
            throw new ErrorServicio("El codigo postal no puede ser nulo");
        }
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new ErrorServicio("La ciudad no puede ser nulo");
        }
        if (pais == null || pais.trim().isEmpty()) {
            throw new ErrorServicio("El país no puede ser nulo");
        }
    }
}
