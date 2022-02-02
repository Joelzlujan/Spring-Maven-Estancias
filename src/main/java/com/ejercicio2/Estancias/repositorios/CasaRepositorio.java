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
//    @Query(value = "SELECT * FROM casa c "
//            + "WHERE (?1 BETWEEN c.fecha_desde AND c.fecha_hasta) "
//            + "AND (?2 BETWEEN c.fecha_desde AND c.fecha_hasta);"
//            , nativeQuery = true)
    
//QUERY 1 (ORIGINAL) SE AGREGO DISTINCT/
//@Query(value="SELECT DISTINCT cs.* FROM (SELECT * FROM casa c WHERE (?1 BETWEEN c.fecha_desde AND c.fecha_hasta)   AND (?2 BETWEEN c.fecha_desde AND c.fecha_hasta)) cs LEFT JOIN reserva r ON cs.id=r.casa_id WHERE (  (r.fecha_desde NOT BETWEEN ?1 AND ?2) AND (r.fecha_hasta NOT BETWEEN ?1 AND ?2) AND (  (?1 NOT BETWEEN r.fecha_desde AND r.fecha_hasta) AND (?2 NOT BETWEEN r.fecha_desde AND r.fecha_hasta))) OR r.casa_id is NULL;",nativeQuery=true)
    
    /*QUERY 2 */
 /*SE AGREGO DISTINCT PARA NO TRAER LAS CASA REPETIDAS(EJEMPLO TENGO 2 ALQUILERES DE LA MISMA CASA
   QUE NO SE ME SUPERPONEN CON LA FECHA QUE DESEO RESERVAR ENTONCES ME TRAE 2 VECES LA MISMA CASA.
   
    SE AGREGO "NOT IN" PARA NO INCLUIR LAS CASAS QUE TIENEN UNA RESERVA YA HECHA QUE SE SUPERPONE Y OTRA
   RESERVA QUE NO , ENTONCES ME TRAE LA CASA EN DONDE LA RESERVA NO SE SUPERPONE PERO NO DEBERIA MOSTRAR ESA CASA */
//    @Query(value = "SELECT DISTINCT cs.* FROM (SELECT * FROM casa c WHERE (?1 BETWEEN c.fecha_desde AND c.fecha_hasta)   AND (?2 BETWEEN c.fecha_desde AND c.fecha_hasta)) cs LEFT JOIN reserva r ON cs.id=r.casa_id WHERE  (  (  (r.fecha_desde NOT BETWEEN ?1 AND ?2) AND (r.fecha_hasta NOT BETWEEN ?1 AND ?2) AND (  (?1 NOT BETWEEN r.fecha_desde AND r.fecha_hasta) AND (?2 NOT BETWEEN r.fecha_desde AND r.fecha_hasta)))                    AND  r.casa_id NOT IN  (SELECT r.casa_id FROM reserva r WHERE (  (r.fecha_desde BETWEEN ?1 AND ?2) OR (r.fecha_hasta BETWEEN ?1 AND ?2) OR (  (?1 BETWEEN r.fecha_desde AND r.fecha_hasta) AND (?2 BETWEEN r.fecha_desde AND r.fecha_hasta)) )            ))        OR r.casa_id is NULL;", nativeQuery = true)


    /*QUERY 2 PERO ORDENADA VERTICALMENTE
    
//        @Query(value = "SELECT DISTINCT cs.* FROM (SELECT * FROM casa c WHERE (?1 BETWEEN c.fecha_desde AND c.fecha_hasta)   AND (?2 BETWEEN c.fecha_desde AND c.fecha_hasta)) cs "
//                + "LEFT JOIN reserva r ON cs.id=r.casa_id "
//                + "WHERE  (  (  (r.fecha_desde NOT BETWEEN ?1 AND ?2) "
//                + "AND (r.fecha_hasta NOT BETWEEN ?1 AND ?2) "
//                + "AND (  (?1 NOT BETWEEN r.fecha_desde AND r.fecha_hasta) AND (?2 NOT BETWEEN r.fecha_desde AND r.fecha_hasta)))  "
//                + "AND  r.casa_id NOT IN  (SELECT r.casa_id FROM reserva r WHERE (  (r.fecha_desde BETWEEN ?1 AND ?2) OR (r.fecha_hasta BETWEEN ?1 AND ?2) OR (  (?1 BETWEEN r.fecha_desde AND r.fecha_hasta) AND (?2 BETWEEN r.fecha_desde AND r.fecha_hasta)  )  )  )  ) " /LINEA EXCLUIR LAS CASAS QUE/
//                + "OR r.casa_id is NULL;", nativeQuery = true)

    /*LA QUERY ANTERIOR DEBIA HACER DOBLE BUSQUEDA EN LA TABLA DE RESERVA INNECESARIAMENTE, CON ESTA QUERY DIGO
    TRAE TODAS LAS CASAS QUE ESTAN ALQUILADAS MENOS LAS QUE ME VAN ARROJAR LA EXCEPCION DE ESTAR ALQUILAS. ES MAS OPTIMA
    QUE LA ANTERIOR, AL USAR NOT IN HACE UNA BUSQUEDA EXCLUYENTE*/
    @Query(value = "SELECT DISTINCT cs.* FROM (SELECT * FROM casa c WHERE (?1 BETWEEN c.fecha_desde AND c.fecha_hasta)   AND (?2 BETWEEN c.fecha_desde AND c.fecha_hasta)) cs "
            + "LEFT JOIN reserva r ON cs.id=r.casa_id "
            + "WHERE  (  r.casa_id NOT IN  (SELECT r.casa_id FROM reserva r WHERE (  (r.fecha_desde BETWEEN ?1 AND ?2) OR (r.fecha_hasta BETWEEN ?1 AND ?2) OR (  (?1 BETWEEN r.fecha_desde AND r.fecha_hasta) AND (?2 BETWEEN r.fecha_desde AND r.fecha_hasta)  )  )AND r.alta=TRUE  )  ) " /*LINEA PARA EXCLUIR LAS CASAS QUE ME VAN ARROJAR LA EXCEPCION DE QUE ESTAN ALQUILADAS */
            + "OR r.casa_id is NULL; ", nativeQuery = true)
            
    public List<Casa> buscarCasasPorFechaDisponible(Date fechaDesde, Date fechaHasta);
}
