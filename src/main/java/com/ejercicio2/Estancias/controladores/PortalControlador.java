package com.ejercicio2.Estancias.controladores;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_PROPIETARIO','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(){
        return "inicio.html";
    }
    
    @GetMapping("/login")
    public String login(@RequestParam (required = false) String error,ModelMap modelo,@RequestParam (required = false)String logout){
    if(error != null){
        modelo.put("error","Nombre de Usuario o clave Incorrecto");
    }
    if(logout != null){
        modelo.put("logout","Cerró su sesión correctamente");
    }
    return "login.html";
    }

}
