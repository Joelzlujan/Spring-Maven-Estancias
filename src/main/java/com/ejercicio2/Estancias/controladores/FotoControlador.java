
package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Casa;
import com.ejercicio2.Estancias.entidades.Usuario;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.servicios.CasaServicio;
import com.ejercicio2.Estancias.servicios.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foto")
public class FotoControlador {
    @Autowired
    private CasaServicio cs;
    
    @Autowired
    private UsuarioServicio us;
    
    @GetMapping("/casa/{id}")
    public ResponseEntity<byte[]> foto (@PathVariable String id){  //este responseEntity permite dividir el back y el front.
        try{
            Casa casa= cs.buscarPorId(id);
            if(casa.getFoto()==null){
                throw new Exception ("La casa no tiene foto asignada");
            }
            byte [] foto = casa.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto,headers,HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(FotoControlador.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario (@PathVariable String id){  //este responseEntity permite dividir el back y el front.
        try{
            Usuario usuario= us.buscarPorId(id);
            if(usuario.getFoto()==null){
                throw new Exception ("El usuario no tiene foto asignada");
            }
            byte [] foto = usuario.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto,headers,HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(FotoControlador.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }    
}
