package com.ejercicio2.Estancias.repositorios;

import com.ejercicio2.Estancias.entidades.Reserva;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, String> {

    //@Query("SELECT (SELECT r FROM Reserva r WHERE r.id = :id) FROM Reserva r WHERE r.fechaDesde BETWEEN :fechaDesdeCasa AND :fechaHastaCasa AND r.fechaHasta BETWEEN :fechaDesdeCasa AND :fechaHastaCasa")
    @Query(value = "SELECT * FROM reserva r "
            + "WHERE r.casa_id = ?1 "
            + "AND  (  (r.fecha_desde BETWEEN ?2 AND ?3)"
            + " OR (r.fecha_hasta BETWEEN ?2 AND ?3) "
            + "OR (  (?2 BETWEEN r.fecha_desde AND r.fecha_hasta) AND (?3 BETWEEN r.fecha_desde AND r.fecha_hasta)) ) AND r.alta=TRUE ;", nativeQuery = true)
    public List<Reserva> listarReservasOcupadas(String idCasa, Date fechaDesdeReserva, Date fechaHastaReserva);


@Query("SELECT r FROM Reserva r WHERE r.cliente.id = :idCliente")
public List<Reserva>listarReservasPorCliente (@Param("idCliente")String idCliente);


@Query(value= "SELECT r.* FROM reserva r "
        +"INNER JOIN casa c ON r.casa_id=c.id "
        +"INNER JOIN propietario p ON c.id=p.casa_id WHERE p.id=?;", nativeQuery = true)
public List<Reserva> listarReservaPorPropietario (String idPropietario);
 


}