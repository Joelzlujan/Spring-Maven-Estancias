package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Cliente;
import com.ejercicio2.Estancias.servicios.ClienteServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    private ClienteServicio cs;
    
    

@GetMapping()
public String cliente() {
    return "cliente.html";
}

@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@GetMapping("/listarClientes")
public String listarClientes(ModelMap modelo){
    List<Cliente> clientesLista = cs.listarClientes();
    modelo.addAttribute("clientes", clientesLista);
    return "listarClientes";   
}
//@GetMapping("/modificarCliente/{id}")
//public String modificarCliente(@PathVariable String id,ModelMap modelo){
//    modelo.put("cliente",cs.buscarPorId(id));
//    return "modificarCliente.html";
//}
//@PostMapping("/modificarCliente/{id}")
//public String modificarCliente(
//        @PathVariable String id,
//        ModelMap modelo,
//        @RequestParam String nombreC,
//        @RequestParam String calle,
//        @RequestParam Integer numero,
//        @RequestParam String codPostal,
//        @RequestParam String ciudad,
//        @RequestParam String pais,
//        RedirectAttributes r
//        ){
//    try{
//        cs.modificarCliente(id, nombreC, calle, numero, codPostal, ciudad, pais);
//        r.addFlashAttribute("exito", "Modificación exitosa");
//        modelo.put("cliente", cs.buscarPorId(id));
//        return "redirect:/cliente/listarClientes";
//    }catch(Exception e){
//        r.addFlashAttribute("error",e.getMessage());
////        r.addFlashAttribute("cliente",cs.buscarPorId(id));
//        return "redirect:/cliente/modificarCliente/{id}";
//    }
//        
//        }


}
