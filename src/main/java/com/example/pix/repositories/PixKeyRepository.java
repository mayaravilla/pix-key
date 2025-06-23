package com.example.pix.repositories;

import com.example.pix.entities.PixKey;
import com.example.pix.enums.PixKeyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PixKeyRepository extends JpaRepository<PixKey, UUID> {

    boolean existsByValorChave(String valorChave);

    @Query("SELECT COUNT(p) FROM PixKey p WHERE p.numeroConta = :numeroConta")
    Long countByNumeroConta(@Param("numeroConta") Integer numeroConta);

    @Query("""
                SELECT p FROM PixKey p
                WHERE (:nomeCorrentista IS NULL OR p.nomeCorrentista = :nomeCorrentista)
                  AND (:tipoChave IS NULL OR p.tipoChave = :tipoChave)
            """)
    List<PixKey> findByFilters(@Param("nomeCorrentista") String nomeCorrentista,
                               @Param("tipoChave") PixKeyType tipoChave);

}
