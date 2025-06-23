package com.example.pix.entities;

import com.example.pix.enums.AccountType;
import com.example.pix.enums.PixKeyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PixKey {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private PixKeyType tipoChave;
    @Column(unique = true)
    private String valorChave;
    @Enumerated(EnumType.STRING)
    private AccountType tipoConta;

    @Column(length = 4)
    private Integer numeroAgencia;

    @Column(length = 8)
    private Integer numeroConta;

    private String nomeCorrentista;

    private String sobrenomeCorrentista;

    @CreationTimestamp
    private LocalDateTime dataHoraInclusao;

    private LocalDateTime dataHoraInativacao;
}
