package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Casa;
import com.ejercicio2.Estancias.entidades.Cliente;
import com.ejercicio2.Estancias.entidades.Familia;
import com.ejercicio2.Estancias.entidades.Usuario;
import com.ejercicio2.Estancias.enumeraciones.Rol;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio ur;

    @Autowired
    private FamiliaServicio fs;

    @Autowired
    private ClienteServicio cs;

    @Transactional
    public Usuario crearUsuario(
            String alias,
            String email,
            String clave,
            Rol rol,
            String nombre,
            Integer edadMin,
            Integer edadMax,
            Integer numHijos,
            String nombreC,
            String calle,
            Integer numero,
            String codPostal,
            String ciudad,
            String pais)
            throws ErrorServicio {

        validarUsuario(alias, email);
        validarClave(clave);
        Usuario usuario = ur.buscarUsuarioPorMail(email);
        if (usuario != null) {
            throw new ErrorServicio("El usuario ya existe");
        }
        if (rol == rol.FAMILIA) {
            Familia f = fs.crearFamilia(nombre, edadMin, edadMax, numHijos);
            f.setAlias(alias);
            f.setEmail(email);
            String encriptado = new BCryptPasswordEncoder().encode(clave);
            f.setClave(encriptado);
            f.setFechaAlta(new Date());
            f.setRol(rol);
            f.setAlta(Boolean.TRUE);
            return ur.save(f);
        }
        if (rol == rol.CLIENTE) {
            Cliente c = cs.crearCliente(nombreC, calle, numero, codPostal, ciudad, pais);
            c.setAlias(alias);
            c.setEmail(email);
            String encriptado = new BCryptPasswordEncoder().encode(clave);
            c.setClave(encriptado);
            c.setFechaAlta(new Date());
            c.setRol(rol);
            c.setAlta(Boolean.TRUE);
            return ur.save(c);
        } else {
            throw new ErrorServicio("Upps!.Hay un problema con los roles");
        }
    }

    @Transactional
    public Usuario modificarUsuario(
            String id,
            String alias,
            String email,
            Rol rol,
            String nombre,
            Integer edadMin,
            Integer edadMax,
            Integer numHijos,
            String nombreC,
            String calle,
            Integer numero,
            String codPostal,
            String ciudad,
            String pais) throws ErrorServicio {
        validarUsuario(alias, email);
        //validamos usuario por mail, si ya existe nos avisa, si no lo modificamos no cambia nada

        Usuario usuario = ur.getById(id);
        if (!usuario.getEmail().equals(email)) {
            Usuario usuario1 = ur.buscarUsuarioPorMail(email);
            if (usuario1 != null) {
                throw new ErrorServicio("El usuario ya existe");
            }
        }

        if (rol == rol.FAMILIA) {
            Familia familia = fs.modificarFamilia(id, nombre, edadMin, edadMax, numHijos);
            familia.setAlias(alias);
            familia.setEmail(email);
            return ur.save(familia);
        }

        if (rol == rol.CLIENTE) {
            Cliente cliente = cs.modificarCliente(id, nombreC, calle, numero, codPostal, ciudad, pais);
            cliente.setAlias(alias);
            cliente.setEmail(email);
            return ur.save(cliente);
        } else {
            throw new ErrorServicio("El usuario no tiene rol");
        }
    }

    @Transactional
    public Usuario cambiarClave(String id, String clave1, String clave2) throws ErrorServicio{
        validarClave(clave1);
        validarClave(clave2);
        
        Usuario usuario = ur.getById(id);
        boolean matches = new BCryptPasswordEncoder().matches(clave1, usuario.getClave());
        if (matches == true) {
            String encriptado = new BCryptPasswordEncoder().encode(clave2);
            usuario.setClave(encriptado);
            return ur.save(usuario);
        }else{
            throw new ErrorServicio("La contraseña anterior no coincide con la original");
        }
    }

    @Transactional
    public void darBajaUsuario(String id) throws ErrorServicio {
        Usuario usuario = ur.getById(id);
        if (usuario != null) {
            usuario.setFechaBaja(new Date());
            usuario.setAlta(Boolean.FALSE);
            ur.save(usuario);
        } else {
            throw new ErrorServicio("El usuario no se encontró");
        }
    }

    @Transactional
    public void darAltaUsuario(String id) throws ErrorServicio {
        Usuario usuario = ur.getById(id);
        if (usuario != null) {
            usuario.setFechaBaja(null);
            usuario.setAlta(Boolean.TRUE);
            ur.save(usuario);
        } else {
            throw new ErrorServicio("El usuario no se encontró");
        }
    }

    @Transactional(readOnly = true)
    public List ListarUsuarios() {
        return ur.findAll();
    }

    @Transactional(readOnly = true)
    public List<Usuario> ListarUsuariosActivos() {
        return ur.ListarUsuariosActivos();
    }

    @Transactional(readOnly = true)
    public List<Usuario> ListarUsuariosInactivos() {
        return ur.ListarUsuariosInactivos();
    }
    
    @Transactional(readOnly = true)
    public Usuario buscarPorId(String id){
        return ur.getById(id);
    }

    public void validarUsuario(String alias, String email) throws ErrorServicio {
        if (alias == null || alias.trim().isEmpty()) {              //el trim no permite los espacios
            throw new ErrorServicio("El alias no puede ser nulo");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ErrorServicio("El email no puede ser nulo");
        }

    }

    public void validarClave(String clave) throws ErrorServicio {

        if (clave == null || clave.trim().isEmpty()) {
            throw new ErrorServicio("La clave no puede ser nulo");
        }
        if (clave.length() > 6 || clave.length() < 4) {
            throw new ErrorServicio("La clave no puede ser menor a 4 digitos ni mayor a 6");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = ur.buscarUsuarioPorMail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes(); //le solicita la sesion
            HttpSession session = attr.getRequest().getSession(true);                                                   //
            session.setAttribute("usuariosession", usuario);                                                        //esto va al template

            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }
}
