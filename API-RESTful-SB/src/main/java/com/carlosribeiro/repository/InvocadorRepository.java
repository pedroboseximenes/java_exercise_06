package com.carlosribeiro.repository;
import com.carlosribeiro.model.Invocador;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InvocadorRepository extends JpaRepository<Invocador, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Invocador p where p.id = :id")
    Optional<Invocador> recuperarInvocadorPorIdComLock(@Param("id") Long id);
}
