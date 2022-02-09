
package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Familia;
import com.ejercicio2.Estancias.servicios.FamiliaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/familia")
public class FamiliaControlador {
    
    @Autowired
    private FamiliaServicio fs;
    
    @GetMapping
    public String familia(){
        return "familia.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listarFamilias")
    public String listarFamilia(ModelMap modelo){
       List<Familia> familiasLista = fs.listarFamilias();
       modelo.addAttribute("familias",familiasLista);
       return "listarFamilias.html";
    }
}
