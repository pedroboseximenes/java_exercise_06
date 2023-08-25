package com.carlosribeiro.repository;

import com.carlosribeiro.model.Ficha;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FichaRepository extends JpaRepository<Ficha, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Ficha p where p.id = :id")
    Optional<Ficha> recuperarFichaPorIdComLock(@Param("id") Long id);

    @Query("select p from Ficha p left join fetch p.invocador where p.invocador.id = :id")
    List<Ficha> findByInvocadorId(Long id);
}
