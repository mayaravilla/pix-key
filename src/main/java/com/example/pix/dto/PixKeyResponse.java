package com.example.pix.dto;

import com.example.pix.entities.PixKey;
import com.example.pix.enums.AccountType;
import com.example.pix.enums.PixKeyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;
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
    private LocalDateTime dataHoraInativacao;

    public PixKeyResponse(PixKey pixKey){
        BeanUtils.copyProperties(pixKey, this);
    }

    public PixKeyResponse(UUID id, PixKeyType tipoChave, String valorChave, AccountType tipoConta,
                          Integer numeroAgencia, Integer numeroConta, String nomeCorrentista,
                          String sobrenomeCorrentista, LocalDateTime dataHoraInclusao) {
    }

    public PixKeyResponse(List<PixKey> chaves) {
    }
}
