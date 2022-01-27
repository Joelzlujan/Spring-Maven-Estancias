package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Casa;
import com.ejercicio2.Estancias.entidades.Familia;
import com.ejercicio2.Estancias.entidades.Foto;
import com.ejercicio2.Estancias.errores.ErrorServicio;
import com.ejercicio2.Estancias.repositorios.CasaRepositorio;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CasaServicio {

    @Autowired
    private CasaRepositorio cr;

    @Autowired
    private FamiliaServicio fs;

    @Autowired
    private FotoServicio fotoS;

    @Transactional
    public Casa crearCasa(String idFamilia, String calle, Integer numero, String codPostal,
            String ciudad, String pais, Date fechaDesde, Date fechaHasta, Integer minDias,
            Integer maxDias, Double precio, String tipoVivienda, String descripcion, MultipartFile archivo) throws ErrorServicio {
        validarCasa(calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);
        validarDescripcion(descripcion);
        Familia familia = fs.buscarPorId(idFamilia);
        if (familia != null) {
            Casa c = new Casa();
            c.setCalle(calle);
            c.setNumero(numero);
            c.setCodPostal(codPostal);
            c.setCiudad(ciudad);
            c.setPais(pais);
            c.setFechaDesde(fechaDesde);
            c.setFechaHasta(fechaHasta);
            c.setMinDias(minDias);
            c.setMaxDias(maxDias);
            c.setPrecio(precio);
            c.setTipoVivienda(tipoVivienda);
            c.setDescripcion(descripcion);
            c.setAlta(Boolean.TRUE);
            Foto foto = fotoS.guardar(archivo);
            c.setFoto(foto);
            familia.setCasa(c);
            return cr.save(c);

        } else {
            throw new ErrorServicio("La familia solicitada no se encontró");
        }

    }


    @Transactional
    public Casa modificarCasa(String id, String calle, Integer numero, String codPostal,
            String ciudad, String pais, Date fechaDesde, Date fechaHasta, Integer minDias,
            Integer maxDias, Double precio, String tipoVivienda, String descripcion, MultipartFile archivo) throws ErrorServicio {
        validarCasa(calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);
        validarDescripcion(descripcion);
        Casa c = cr.getById(id);
        if (c != null) {
            c.setCalle(calle);
            c.setNumero(numero);
            c.setCodPostal(codPostal);
            c.setCiudad(ciudad);
            c.setPais(pais);
            c.setFechaDesde(fechaDesde);
            c.setFechaHasta(fechaHasta);
            c.setMinDias(minDias);
            c.setMaxDias(maxDias);
            c.setPrecio(precio);
            c.setTipoVivienda(tipoVivienda);
            c.setDescripcion(descripcion);
            String idFoto = null;
            if (c.getFoto() != null) {
                idFoto = c.getFoto().getId();
            }
            Foto foto = fotoS.actualizar(idFoto, archivo);
            c.setFoto(foto);
            return cr.save(c);
        } else {
            throw new ErrorServicio("No se encontro la casa solicitada");
        }

//revisar foto cuando no ingresemos ninguna en la modificacion de la casa-
    }

    @Transactional(readOnly = true)
    public Casa buscarPorId(String id) {
        return cr.getById(id);
    }
    @Transactional(readOnly=true)
    public List<Casa> buscarCasasPorFechaDisponible (Date fechaDesde,Date fechaHasta){
        return cr.buscarCasasPorFechaDisponible(fechaDesde, fechaHasta);
    }
    

    @Transactional
    public void altaCasa(String id) throws ErrorServicio {
        Casa casa = cr.getById(id);
        if (casa != null) {
            casa.setAlta(Boolean.TRUE);
            cr.save(casa);
        } else {
            throw new ErrorServicio("La casa solicitada no se encuentra");
        }
    }

    @Transactional
    public void bajaCasa(String id) throws ErrorServicio {
        Casa casa = cr.getById(id);
        if (casa != null) {
            casa.setAlta(Boolean.FALSE);
            cr.save(casa);
        } else {
            throw new ErrorServicio("La casa solicitada no se encuentra");
        }
    }

    public void validarCasa(
            String calle,
            Integer numero,
            String codPostal,
            String ciudad,
            String pais,
            Date fechaDesde,
            Date fechaHasta,
            Integer minDias,
            Integer maxDias,
            Double precio,
            String tipoVivienda
    ) throws ErrorServicio {
        if (calle == null || calle.trim().isEmpty()) {
            throw new ErrorServicio("La calle es nula o esta vacía");
        }
        if (numero == null || numero < 0) {
            throw new ErrorServicio("El numero de la calle es nulo o menor a 0");
        }
        if (codPostal == null || codPostal.trim().isEmpty()) {
            throw new ErrorServicio("El cod Postal es nulo o esta vacía");
        }
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new ErrorServicio("La ciudad es nula o esta vacía");
        }
        if (pais == null || pais.trim().isEmpty()) {
            throw new ErrorServicio("La ciudad es nula o esta vacía");
        }
        if (fechaDesde == null) {
            throw new ErrorServicio("La fecha desde es nula");
        }
        if (fechaHasta == null) {
            throw new ErrorServicio("La fecha hasta es nula");
        }
        if (fechaHasta.before(fechaDesde)) {
            throw new ErrorServicio("La fecha hasta ingresada está antes que la fecha desde");
        }
        if (fechaHasta.equals(fechaDesde)) {
            throw new ErrorServicio("El alquiler de la casa no puede ser menor que dos dias");
        }
        if (minDias == null || numero < 0) {
            throw new ErrorServicio("El numero de dias es nulo o menor a 0");
        }
        if (maxDias == null || maxDias < 0) {
            throw new ErrorServicio("El maximo de dias es nulo o menor a 0");
        }
        LocalDate fechaD = fechaDesde.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaH = fechaHasta.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period p = Period.between(fechaD, fechaH);
        int dias = (int) ChronoUnit.DAYS.between(fechaD, fechaH);

        if (dias < minDias) {
            throw new ErrorServicio("La cantidad de dias seleccionados es menor a la cantidad de dias minimos");
        }
        if (dias <= maxDias) {
            throw new ErrorServicio("La cantidad de dias seleccionados no puede superar al maximo de dias");
        }
        if (minDias > maxDias) {
            throw new ErrorServicio("El minimo de dias no puede ser mayor al máximo");
        }
        if (precio == null || precio < 0) {
            throw new ErrorServicio("El precio es nulo o menor a 0");
        }
        if (tipoVivienda == null || tipoVivienda.trim().isEmpty()) {
            throw new ErrorServicio("El tipo de vivienda es nula o esta vacía");
        }
    }
        public void validarDescripcion (String descripcion) throws ErrorServicio{
        if(descripcion.length()>1499){
            throw new ErrorServicio("La descripción no puede ser mayor a 1500 caracteres");
        }
    }
}
