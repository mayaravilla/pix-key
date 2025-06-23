package com.example.pix.service;

import com.example.pix.dto.PixKeyRequest;
import com.example.pix.dto.PixKeyResponse;
import com.example.pix.dto.PixKeyUpdateDTO;
import com.example.pix.entities.PixKey;
import com.example.pix.enums.PixKeyType;
import com.example.pix.exception.DomainException;
import com.example.pix.exception.ExceptionHandlerAdvice;
import com.example.pix.interfaces.PixKeyServicePort;
import com.example.pix.repositories.PixKeyRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PixKeyServiceImpl implements PixKeyServicePort {

    private final PixKeyRepository repository;


    @Override
    public PixKeyResponse create(PixKeyRequest request) {
        if (repository.existsByValorChave(request.valorChave())){
            throw new DomainException("Chave já existente");
        }
        //preciso fazer essa validação aqui?
        if (request.tipoChave() == PixKeyType.EMAIL && !request.valorChave().matches("^[^@]+@[^@]+\\.[^@]+$")) {
            throw new DomainException("E-mail inválido");
        }
        if (request.tipoChave() == PixKeyType.CPF && !request.valorChave().matches("\\d{11}")) {
            throw new DomainException("CPF inválido");
        }
        if (request.tipoChave() == PixKeyType.CNPJ && !request.valorChave().matches("\\d{14}")) {
            throw new DomainException("CNPJ inválido");
        }

        Long chavePix = repository.countByNumeroConta((request.numeroConta()));
        if (chavePix >= 5) {
            throw new DomainException("Limite de chaves por chavePix excedido");
        }

        PixKey pixKey = PixKey.builder()
                .id(UUID.randomUUID())
                .tipoChave(request.tipoChave())
                .valorChave(request.valorChave())
                .tipoConta(request.tipoConta())
                .numeroAgencia(request.numeroAgencia())
                .numeroConta(request.numeroConta())
                .nomeCorrentista(request.nomeCorrentista())
                .sobrenomeCorrentista(request.sobrenomeCorrentista())
                .build();

        return new PixKeyResponse(repository.save(pixKey));
    }

    @Override
    public PixKeyResponse update(UUID id, PixKeyUpdateDTO keyUpdateDTO) {
        PixKey existingKey = repository.findById(keyUpdateDTO.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chave Pix não encontrada"));

        existingKey.setTipoConta(keyUpdateDTO.tipoConta());
        existingKey.setNumeroAgencia(keyUpdateDTO.numeroAgencia());
        existingKey.setNumeroConta(keyUpdateDTO.numeroConta());
        existingKey.setNomeCorrentista(keyUpdateDTO.nomeCorrentista());
        existingKey.setSobrenomeCorrentista(keyUpdateDTO.sobrenomeCorrentista());

        PixKey updatedKey = repository.save(existingKey);
        return new PixKeyResponse(updatedKey);
    }

    @Override
    public List<PixKeyResponse> getById(UUID id, String nomeCorrentista, PixKeyType tipoChave) {

        if (id != null) {
            if (nomeCorrentista != null || tipoChave != null) {
                throw new DomainException("Se o ID for informado, nenhum outro filtro pode ser usado.");
            }

            PixKey chave = repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Chave Pix não encontrada"));

            return List.of(new PixKeyResponse(chave));
        }

        List<PixKey> chaves = repository.findByFilters(nomeCorrentista, tipoChave);

        if (chaves.isEmpty()) {
            throw new DomainException("Nenhuma chave encontrada com os filtros informados.");
        }

        return chaves.stream()
                .map(PixKeyResponse::new)
                .toList();
    }

    @Override
    public PixKeyResponse delete(UUID id) {
        PixKey pixKey = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chave Pix não encontrada"));

        repository.delete(pixKey);
        return new PixKeyResponse(pixKey);
    }
}
