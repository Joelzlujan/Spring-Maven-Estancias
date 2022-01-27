package com.ejercicio2.Estancias.repositorios;

import com.ejercicio2.Estancias.entidades.Casa;
import com.ejercicio2.Estancias.entidades.Reserva;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CasaRepositorio extends JpaRepository<Casa, String> {
//        @Query(value = "SELECT * FROM casa c "
//            + "WHERE (?1 BETWEEN c.fecha_desde AND c.fecha_hasta) "
//            + "AND (?2 BETWEEN c.fecha_desde AND c.fecha_hasta);"
//            , nativeQuery = true)

    @Query(value = "SELECT * FROM (SELECT * FROM casa c WHERE "
            + "(?1 BETWEEN c.fecha_desde AND c.fecha_hasta) "
            + "AND (?2 BETWEEN c.fecha_desde AND c.fecha_hasta))AS cs "
            + "LEFT JOIN reserva r ON cs.id=r.casa_id "
            + "WHERE ((r.fecha_desde NOT BETWEEN ?1 AND ?2) "
            + "OR (r.fecha_hasta NOT BETWEEN ?1 AND ?2) "
            + "OR ((?1 NOT BETWEEN r.fecha_desde AND r.fecha_hasta) "
            + "AND (?2 NOT BETWEEN r.fecha_desde AND r.fecha_hasta))) OR r.casa_id is NULL;",
             nativeQuery = true)

    public List<Casa> buscarCasasPorFechaDisponible(Date fechaDesde, Date fechaHasta);
}
