
package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Propietario;
import com.ejercicio2.Estancias.servicios.PropietarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/propietario")
public class PropietarioControlador {
    
    @Autowired
    private PropietarioServicio ps;
    
    @GetMapping
    public String propietario(){
        return "propietario.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listarPropietarios")
    public String listarPropietario(ModelMap modelo){
       List<Propietario> propietariosLista = ps.listarPropietarios();
       modelo.addAttribute("propietarios",propietariosLista);
       return "listarPropietarios.html";
    }
}
