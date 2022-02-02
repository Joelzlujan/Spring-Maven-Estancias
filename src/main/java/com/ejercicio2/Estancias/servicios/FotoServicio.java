package com.ejercicio2.Estancias.servicios;

import com.ejercicio2.Estancias.entidades.Foto;
import com.ejercicio2.Estancias.repositorios.FotoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fr;

    @Transactional
    public Foto guardar(MultipartFile archivo) {
        if (!archivo.isEmpty()) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getOriginalFilename());
                foto.setContenido(archivo.getBytes());
                return fr.save(foto);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo) {
        if (!archivo.isEmpty()) {
            try {
                Foto foto = new Foto();
                if (idFoto != null) {
                    foto = fr.getById(idFoto);
                }
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getOriginalFilename());
                foto.setContenido(archivo.getBytes());
                return fr.save(foto);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

}
