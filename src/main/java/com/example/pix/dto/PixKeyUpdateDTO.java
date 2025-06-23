package com.example.pix.dto;

import com.example.pix.enums.AccountType;
import com.example.pix.enums.PixKeyType;
import com.example.pix.interfaces.CnpjGroup;
import com.example.pix.interfaces.CpfGroup;
import com.example.pix.interfaces.EmailGroup;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;


public record PixKeyUpdateDTO(
        @NotNull
        UUID id,

        @NotNull
        AccountType tipoConta,

        @NotNull(message = "Chave não pode ser nula")
        PixKeyType tipoChave,

        @NotBlank
        String valorChave,

        @Min(1)
        @Max(value = 9999, message = "Agência deve conter até 4 dígitos.")
        Integer numeroAgencia,

        @Min(1)
        @Max(value = 99999999, message = "Conta deve conter até 8 dígitos.")
        Integer numeroConta,

        @NotBlank @Size(max = 30) String nomeCorrentista,
        @Size(max = 45) String sobrenomeCorrentista
) {}
