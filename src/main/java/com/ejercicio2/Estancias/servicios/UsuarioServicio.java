package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Casa;
import com.ejercicio2.Estancias.entidades.Cliente;
import com.ejercicio2.Estancias.entidades.Propietario;
import com.ejercicio2.Estancias.entidades.Foto;
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
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio ur;

    @Autowired
    private PropietarioServicio ps;

    @Autowired
    private ClienteServicio cs;

    @Autowired
    private FotoServicio fotoser;

    @Transactional
    public Usuario crearUsuario(
            String alias,
            String email,
            String clave,
            Rol rol,
            MultipartFile archivo,
            //PARAMETROS PROPIETARIO
            String nombre,
            String descripcion,
            String telefono,
            //PARAMETROS CLIENTE
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
        if (rol == rol.PROPIETARIO) {
            Propietario p = ps.crearPropietario(nombre, descripcion, telefono);
            p.setAlias(alias);
            p.setEmail(email);
            Foto foto = fotoser.guardar(archivo);
            p.setFoto(foto);
            String encriptado = new BCryptPasswordEncoder().encode(clave);
            p.setClave(encriptado);
            p.setFechaAlta(new Date());
            p.setRol(rol);
            p.setAlta(Boolean.TRUE);
            return ur.save(p);
        }
        if (rol == rol.CLIENTE) {
            Cliente c = cs.crearCliente(nombreC, calle, numero, codPostal, ciudad, pais);
            c.setAlias(alias);
            c.setEmail(email);
            Foto foto = fotoser.guardar(archivo);
            c.setFoto(foto);
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
            MultipartFile archivo,
            String nombre,
            String descripcion,
            String telefono,
            String nombreC,
            String calle,
            Integer numero,
            String codPostal,
            String ciudad,
            String pais)
             throws ErrorServicio {
        validarUsuario(alias, email);
        //validamos usuario por mail, si ya existe nos avisa, si no lo modificamos no cambia nada

        Usuario usuario = ur.getById(id);
        if (!usuario.getEmail().equals(email)) {
            Usuario usuario1 = ur.buscarUsuarioPorMail(email);
            if (usuario1 != null) {
                throw new ErrorServicio("El usuario ya existe");
            }
        }

        if (rol == rol.PROPIETARIO) {
            Propietario propietario = ps.modificarPropietario(id, nombre,descripcion, telefono);
            propietario.setAlias(alias);
            propietario.setEmail(email);
            String idFoto = null;
            if (propietario.getFoto() != null) {
                idFoto = propietario.getFoto().getId();
            }
            Foto foto = fotoser.actualizar(idFoto, archivo);
            if (foto!= null){
            propietario.setFoto(foto);
            }
            
            return ur.save(propietario);
        }

        if (rol == rol.CLIENTE) {
            Cliente cliente = cs.modificarCliente(id, nombreC, calle, numero, codPostal, ciudad, pais);
            cliente.setAlias(alias);
            cliente.setEmail(email);
            String idFoto = null;
            if (cliente.getFoto() != null) {
                idFoto = cliente.getFoto().getId();
            }
            Foto foto = fotoser.actualizar(idFoto, archivo);
            if (foto!= null){
            cliente.setFoto(foto);
            }
            return ur.save(cliente);
        } else {
            throw new ErrorServicio("El usuario no tiene rol");
        }
    }

    @Transactional
    public Usuario cambiarClave(String id, String clave1, String clave2) throws ErrorServicio {
        validarClave(clave1);
        validarClave(clave2);

        Usuario usuario = ur.getById(id);
        boolean matches = new BCryptPasswordEncoder().matches(clave1, usuario.getClave());
        if (matches == true) {
            String encriptado = new BCryptPasswordEncoder().encode(clave2);
            usuario.setClave(encriptado);
            return ur.save(usuario);
        } else {
            throw new ErrorServicio("La contraseña anterior no coincide con la original");
        }
    }

    @Transactional
    public void darBajaUsuario(String id) throws ErrorServicio {
        Usuario usuario = ur.getById(id);
        if (usuario != null) {
            if (usuario.getAlias().equals("admin")) {
                throw new ErrorServicio("No se puede dar de baja al admin principal");
            }
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
            if (usuario.getAlias().equals("admin")) {
                throw new ErrorServicio("No se puede dar de alta al admin principal");
            }
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
    public Usuario buscarPorId(String id) {
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
        //sesion
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

    @Transactional
    public Usuario guardarAdmin(String alias, String email, String clave, Rol rol, MultipartFile archivo) throws ErrorServicio {
        validarUsuario(alias, email);
        validarClave(clave);
        Usuario usuario = ur.buscarUsuarioPorMail(email);
        if (usuario != null) {
            throw new ErrorServicio("El usuario ya existe");
        }
        if (rol.equals(Rol.ADMIN)) {
            usuario = new Usuario();
            usuario.setAlias(alias);
            usuario.setEmail(email);
            usuario.setRol(rol);
            Foto foto = fotoser.guardar(archivo);
            usuario.setFoto(foto);
            String encriptado = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptado);
            usuario.setFechaAlta(new Date());
            usuario.setAlta(Boolean.TRUE);
            return ur.save(usuario);
        } else {
            throw new ErrorServicio("El rol no es el de admin");
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return ur.findAllByRol(Rol.ADMIN);
    }

    @Transactional
    public Usuario modificarAdmin(String id, String alias, String email, MultipartFile archivo) throws ErrorServicio {
        validarUsuario(alias, email);

        Usuario usuario = ur.getById(id);
        if (usuario == null) {
            throw new ErrorServicio("El usuario no existe");
        }
        if (!usuario.getEmail().equals(email)) {
            Usuario usuario1 = ur.buscarUsuarioPorMail(email);            //esto lo usamos para ver si existe otro usuario con el mismo email que le queremos agregar
            if (usuario1 != null) {
                throw new ErrorServicio("El usuario ya existe");
            }
        }
        if (usuario.getAlias().equals("admin")) {
            usuario.setEmail(email);
        } else {
            usuario.setEmail(email);
            usuario.setAlias(alias);
        }
        String idFoto = null;
        if (usuario.getFoto() != null) {
            idFoto = usuario.getFoto().getId();
        }
        Foto foto = fotoser.actualizar(idFoto, archivo);
        if (foto!= null){
        usuario.setFoto(foto);
        }
        return ur.save(usuario);
    }
}
