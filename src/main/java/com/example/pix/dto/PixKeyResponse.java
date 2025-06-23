package com.example.pix.dto;

import com.example.pix.entities.PixKey;
import com.example.pix.enums.AccountType;
import com.example.pix.enums.PixKeyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PixKeyResponse{

    private UUID id;
    private PixKeyType tipoChave;
    private String valorChave;
    private AccountType tipoConta;
    private Integer numeroAgencia;
    private Integer numeroConta;
    private String nomeCorrentista;
    private String sobrenomeCorrentista;
    private LocalDateTime dataHoraInclusao;

    public PixKeyResponse(PixKey entity){
        this.id = entity.getId();
        this.tipoChave = PixKeyType.valueOf(String.valueOf(entity.getTipoChave()));
        this.valorChave = entity.getValorChave();
        this.tipoConta = AccountType.valueOf(String.valueOf(entity.getTipoConta()));
        this.numeroAgencia = entity.getNumeroAgencia();
        this.numeroConta = entity.getNumeroConta();
        this.nomeCorrentista = entity.getNomeCorrentista();
        this.sobrenomeCorrentista = entity.getSobrenomeCorrentista() != null ? entity.getSobrenomeCorrentista() : "";
        this.dataHoraInclusao = entity.getDataHoraInclusao();

    }
}
