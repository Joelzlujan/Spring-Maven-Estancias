package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Cliente;
import com.ejercicio2.Estancias.entidades.Usuario;
import com.ejercicio2.Estancias.enumeraciones.Rol;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.servicios.ClienteServicio;
import com.ejercicio2.Estancias.servicios.PropietarioServicio;
import com.ejercicio2.Estancias.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio us;

    @Autowired
    private ClienteServicio cs;

    @Autowired
    private PropietarioServicio ps;

    @GetMapping("/guardarCliente")
    public String guardarCliente(ModelMap modelo) {
        modelo.put("rol", Rol.CLIENTE);
        return "guardarCliente.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN')")
    @GetMapping("/modificarCliente/{id}")
    public String modificarCliente(@PathVariable String id, ModelMap modelo) {
        modelo.put("cliente", cs.buscarPorId(id));
        return "modificarCliente.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO','ROLE_ADMIN')")
    @GetMapping("/modificarPropietario/{id}")
    public String modificarPropietario(@PathVariable String id, ModelMap modelo) {
        modelo.put("propietario", ps.buscarPorId(id));
        return "modificarPropietario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificarUsuario/{id}")
    public String modificarUsuario(@PathVariable String id, ModelMap modelo) {
        modelo.put("usuario", us.buscarPorId(id));
        return "modificarUsuario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROPIETARIO')")
    @PostMapping("/modificarUsuario/{id}")
    public String modificarUsuario(
            @PathVariable String id,
            ModelMap modelo,
            //DATOS USUARIO
            @RequestParam(required = false) String alias,
            @RequestParam(required = false) String email,
            //@RequestParam(required = false) String clave,

            @RequestParam Rol rol,
            @RequestParam MultipartFile archivo,
            //DATOS CLIENTE

            @RequestParam(required = false) String nombreC,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String codPostal,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String pais,
            //DATOS PROPIETARIO

            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) String telefono,
            RedirectAttributes r,
            HttpSession session) {
        try {
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuariosession");
            switch (rol) {
                case CLIENTE:
                {
                    Usuario usuario = us.modificarUsuario(id, alias, email, rol, archivo, nombre, descripcion, telefono, nombreC, calle, numero, codPostal, ciudad, pais);
                    if(usuarioSesion.getId().equals(usuario.getId())){
                    session.setAttribute("usuariosession", usuario);  
                    }                                                                           //esto nos sirve para actualizar la sesion y que se carguen los datos rapido
                    r.addFlashAttribute("exito", "Modificación exitosa");
                    return "redirect:/usuario/modificarCliente/{id}";
                }
                case PROPIETARIO:
                {
                    Usuario usuario = us.modificarUsuario(id, alias, email, rol, archivo, nombre, descripcion, telefono, nombreC, calle, numero, codPostal, ciudad, pais);
                    if(usuarioSesion.getId().equals(usuario.getId())){
                    session.setAttribute("usuariosession", usuario);            //util para actualizar gracias a la session los datos instantaneamente
                    }
                    r.addFlashAttribute("exito", "Modificación exitosa");
                    return "redirect:/usuario/modificarPropietario/{id}";                 
                }
                default:
                {
                    Usuario usuario = us.modificarAdmin(id, alias, email, archivo);
                    if(usuarioSesion.getId().equals(usuario.getId())){
                    session.setAttribute("usuariosession", usuario);
                    }
                    r.addFlashAttribute("exito", "Modificación exitosa");
                    return "redirect:/usuario/modificarUsuario/{id}";
                }
            }

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
            switch (rol) {
                case CLIENTE:
                    return "redirect:/usuario/modificarCliente/{id}";
                case PROPIETARIO:
                    return "redirect:/usuario/modificarPropietario/{id}";
                default:
                    return "redirect:/usuario/modificarUsuario/{id}";
            }
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROPIETARIO')")
    @GetMapping("/claveUsuario/{id}")
    public String cambiarClave(@PathVariable String id, ModelMap modelo
    ) {
        modelo.put("usuario", us.buscarPorId(id));
        return "claveUsuario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROPIETARIO')")
    @PostMapping("/claveUsuario/{id}")
    public String cambiarClave(@PathVariable String id,
            @RequestParam Rol rol,
            @RequestParam String clave1,
            @RequestParam String clave2, RedirectAttributes r
    ) {
        try {
            us.cambiarClave(id, clave1, clave2);
            r.addFlashAttribute("exito", "Modificación Exitosa");
        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuario/claveUsuario/{id}";
    }
    //agregar el cambio de claves

    @PostMapping("/guardarUsuario")
    public String guardarUsuario(ModelMap modelo,
            @RequestParam(required = false) String alias,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String clave,
            @RequestParam Rol rol,
            @RequestParam MultipartFile archivo,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String nombreC,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String codPostal,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String pais,
            RedirectAttributes r
    ) {
        try {
            us.crearUsuario(alias, email, clave, rol, archivo, nombre, descripcion, telefono, nombreC, calle, numero, codPostal, ciudad, pais);
            r.addFlashAttribute("exito", "Registro Exitoso");      //cuando usamos el redirect attributes tenemos q usar esto, no podemos usar el modelo.put                      
        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuario/guardarCliente";
        }
        switch (rol) {
            case CLIENTE:
                return "redirect:/usuario/guardarCliente";
            case PROPIETARIO:
                return "redirect:/usuario/guardarPropietario";
            default:
                return "index.html";
        }
    }

    @GetMapping("/guardarPropietario")
    public String guardarPropietario(ModelMap modelo)
    {
        modelo.put("rol", Rol.PROPIETARIO);
        return "guardarPropietario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROPIETARIO')")
    @GetMapping("/alta/{id}/{rol}")
    public String alta(@PathVariable String id,
            @PathVariable Rol rol, RedirectAttributes r
    ) {

        try {
            switch (rol) {
                case CLIENTE:
                    us.darAltaUsuario(id);
                    return "redirect:/cliente/listarClientes";
                case PROPIETARIO:
                    us.darAltaUsuario(id);
                    return "redirect:/propietario/listarPropietarios";
                default:
                    us.darAltaUsuario(id);
                    return "redirect:/usuario/listarAdmins";
            }

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
            switch (rol) {
                case CLIENTE:
                    return "redirect:/cliente/listarClientes";
                case PROPIETARIO:
                    return "redirect:/propietario/listarPropietarios";
                default:
                    return "redirect:/usuario/listarAdmins";
            }
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROPIETARIO')")
    @GetMapping("/baja/{id}/{rol}")
    public String baja(@PathVariable String id,
            @PathVariable Rol rol, RedirectAttributes r
    ) {
        try {
            switch (rol) {
                case CLIENTE:
                    us.darBajaUsuario(id);
                    return "redirect:/cliente/listarClientes";
                case PROPIETARIO:
                    us.darBajaUsuario(id);
                    return "redirect:/propietario/listarPropietarios";
                default:
                    us.darBajaUsuario(id);
                    return "redirect:/usuario/listarAdmins";
            }
        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
            switch (rol) {
                case CLIENTE:
                    return "redirect:/cliente/listarClientes";
                case PROPIETARIO:
                    return "redirect:/propietario/listarPropietarios";
                default:
                    return "redirect:/usuario/listarAdmins";
            }
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/guardarAdmin")
    public String guardarAdmin() {
        return "guardarAdmin.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/guardarAdmin")
    public String guardarAdmin(ModelMap modelo,
            @RequestParam(required = false) String alias,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String clave,
            @RequestParam Rol rol,
            @RequestParam MultipartFile archivo,RedirectAttributes r) {
        try {
            us.guardarAdmin(alias, email, clave, rol, archivo);
            r.addFlashAttribute("exito", "Registro Exitoso");

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuario/guardarAdmin";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listarAdmins")
    public String listarAdmins(ModelMap modelo) {
        List<Usuario> usuariosLista = us.listarUsuarios();
        modelo.addAttribute("usuarios", usuariosLista);
        return "listarAdmins.html";
    }
}
