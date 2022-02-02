package com.ejercicio2.Estancias.controladores;

import com.ejercicio2.Estancias.entidades.Casa;
import com.ejercicio2.Estancias.entidades.Reserva;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.servicios.CasaServicio;
import com.ejercicio2.Estancias.servicios.ReservaServicio;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

@GetMapping("/listarReservas/{idCliente}")
    public String listarReservasPorCliente(@PathVariable String idCliente,ModelMap modelo){
        List<Reserva>reservas= rs.listarReservasPorCliente(idCliente);
        modelo.put("reservas", reservas);
        return "listarReservas.html";
    }
    
@GetMapping("/baja/{idCliente}/{idReserva}")
        public String baja(@PathVariable String idCliente,@PathVariable String idReserva, RedirectAttributes r) {
        try {
            rs.darBajaReserva(idReserva);
        } catch (Exception e) {
            r.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reserva/listarReservas/{idCliente}";
    } 
    
}
