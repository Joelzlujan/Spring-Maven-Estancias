
package com.ejercicio2.Estancias.repositorios;

import com.ejercicio2.Estancias.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository <Cliente,String> {
    
}
