package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Casa;
import com.ejercicio2.Estancias.entidades.Reserva;
import com.ejercicio2.Estancias.entidades.Usuario;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.servicios.CasaServicio;
import com.ejercicio2.Estancias.servicios.ReservaServicio;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reserva")
public class ReservaControlador {

    @Autowired
    ReservaServicio rs;

    @Autowired
    CasaServicio cs;

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE')")
    @GetMapping("/guardarReserva")
    public String guardarReserva(@RequestParam String idCliente, @RequestParam String idCasa, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta, ModelMap modelo) {

        Casa casa = cs.buscarPorId(idCasa);
        modelo.put("casa", casa);

        modelo.put("idCasa", idCasa);
        modelo.put("idCliente", idCliente);
        modelo.put("fechaDesde", fechaDesde);
        modelo.put("fechaHasta", fechaHasta);
        return "guardarReserva.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE')")
    @PostMapping("/guardarReserva")
    public String confirmarReserva(@RequestParam(required = false) String idCliente,
            @RequestParam(required = false) String idCasa,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta, RedirectAttributes r, ModelMap modelo) {
        try {

            rs.crearReserva(idCliente, idCasa, fechaDesde, fechaHasta);
            r.addFlashAttribute("exito", "La reserva se realizó con exito");

        } catch (ErrorServicio e) {
            r.addFlashAttribute("error", e.getMessage());

        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "redirect:/reserva/guardarReserva?idCliente=" + idCliente + "&idCasa=" + idCasa + "&fechaDesde=" + sdf.format(fechaDesde) + "&fechaHasta=" + sdf.format(fechaHasta);

    }
    
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN')")
    @GetMapping("/listarReservas/{idCliente}")
    public String listarReservasPorCliente(@PathVariable String idCliente, ModelMap modelo) {
        List<Reserva> reservas = rs.listarReservasPorCliente(idCliente);
        modelo.put("reservas", reservas);
        return "listarReservas.html";
    }

    
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE','ROLE_ADMIN','ROLE_PROPIETARIO')")
    @GetMapping("/baja/{idCliente}/{idReserva}")
    public String baja(@PathVariable String idCliente, @PathVariable String idReserva, RedirectAttributes r,HttpSession session) {
        Usuario usuario =(Usuario) session.getAttribute("usuariosession");
        try {
            rs.darBajaReserva(idReserva);
        } catch (Exception e) {
            r.addFlashAttribute("error", e.getMessage());
        }
        switch (usuario.getRol()){
            case CLIENTE:
                return "redirect:/reserva/listarReservas/{idCliente}";
            case PROPIETARIO:
                return "redirect:/reserva/listarReservasporPropietario/"+usuario.getId();
            default:
                return "redirect:/reserva/listarReservas";
        }
        
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listarReservas")
    public String listarReservas(ModelMap modelo){
        List<Reserva> reservas = rs.listarReservas();
        modelo.put("reservas",reservas);
        return "listarReservasAdm.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_PROPIETARIO')")
        @GetMapping("/listarReservasporPropietario/{idPropietario}")
    public String listarReservasPorPropietario(@PathVariable String idPropietario, ModelMap modelo) {
        List<Reserva> reservas = rs.listarReservaPorPropietario(idPropietario);
        modelo.put("reservas", reservas);
        return "listarReservas.html";
    }
}
