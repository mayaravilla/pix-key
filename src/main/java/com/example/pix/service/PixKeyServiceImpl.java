package com.example.pix.service;

import com.example.pix.dto.PixKeyRequest;
import com.example.pix.dto.PixKeyResponse;
import com.example.pix.dto.PixKeyUpdateDTO;
import com.example.pix.entities.PixKey;
import com.example.pix.enums.AccountType;
import com.example.pix.enums.PixKeyType;
import com.example.pix.exception.DomainException;
import com.example.pix.exception.NotFoundException;
import com.example.pix.interfaces.PixKeyServicePort;
import com.example.pix.repositories.PixKeyRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PixKeyServiceImpl implements PixKeyServicePort {

    private final PixKeyRepository repository;


    @Override
    public PixKeyResponse create(PixKeyRequest request) {
        if (repository.existsByValorChave(request.valorChave())) {
            throw new DomainException("Chave já existente");
        }

        if (!request.tipoChave().isValid(request.valorChave())) {
            throw new DomainException("Valor inválido para o tipo de chave: " + request.tipoChave());
        }

        Long chavePix = repository.countByNumeroConta(request.numeroConta());
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
    public PixKeyResponse update(UUID id, PixKeyUpdateDTO dto) {
        PixKey pixKey = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chave Pix não encontrada"));

        pixKey.setValorChave(dto.valorChave());
        pixKey.setTipoConta(AccountType.valueOf(String.valueOf(dto.tipoConta())));
        pixKey.setNumeroAgencia(dto.numeroAgencia());
        pixKey.setNumeroConta(dto.numeroConta());
        pixKey.setNomeCorrentista(dto.nomeCorrentista());
        pixKey.setSobrenomeCorrentista(dto.sobrenomeCorrentista());

        PixKey atualizado = repository.save(pixKey);

        return new PixKeyResponse(atualizado);
    }

    @Override
    public List<PixKeyResponse> getById(UUID id, String nomeCorrentista, PixKeyType tipoChave) {

        if (id == null && nomeCorrentista == null && tipoChave == null) {
            throw new NotFoundException("Chave Pix não informada");
        }

        if (id != null) {
            if (nomeCorrentista != null || tipoChave != null) {
                throw new DomainException("Se o ID for informado, nenhum outro filtro pode ser usado.");
            }
        }

        List<PixKey> chaves = repository.findByFilters(nomeCorrentista, tipoChave);

        if (chaves.isEmpty()) {
            throw new NotFoundException("Nenhuma chave encontrada com os filtros informados.");
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
