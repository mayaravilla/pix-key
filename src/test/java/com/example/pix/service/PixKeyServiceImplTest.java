package com.example.pix.service;

import com.example.pix.dto.PixKeyRequest;
import com.example.pix.dto.PixKeyResponse;
import com.example.pix.dto.PixKeyUpdateDTO;
import com.example.pix.entities.PixKey;
import com.example.pix.enums.AccountType;
import com.example.pix.enums.ClientType;
import com.example.pix.enums.PixKeyType;
import com.example.pix.exception.DomainException;
import com.example.pix.exception.NotFoundException;
import com.example.pix.repositories.PixKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PixKeyServiceImplTest {

    @Mock
    private PixKeyRepository repository;

    @InjectMocks
    private PixKeyServiceImpl service;

    private PixKeyRequest request;
    private PixKeyUpdateDTO updateDTO;
    private PixKey pixKey;

    @BeforeEach
    void setup() {
        request = new PixKeyRequest(
                PixKeyType.EMAIL,
                "teste@email.com",
                AccountType.CORRENTE,
                1234,
                1234567,
                "Mayara",
                "Villa"
        );

        updateDTO = new PixKeyUpdateDTO(
                AccountType.POUPANCA,
                PixKeyType.CPF,
                "22223333",
                1234,
                1234567,
                "Mayara",
                "Villa"
        );

        pixKey = PixKey.builder()
                .tipoChave(PixKeyType.EMAIL)
                .valorChave("teste@email.com")
                .tipoConta(AccountType.CORRENTE)
                .numeroAgencia(1234)
                .numeroConta(56789012)
                .nomeCorrentista("Mayara")
                .sobrenomeCorrentista("Villa")
                .build();
    }

    @Test
    void testCreateWithValidData() {
        when(repository.existsByValorChave(request.valorChave())).thenReturn(false);
        when(repository.countByNumeroConta(request.numeroConta())).thenReturn(0L);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PixKeyResponse response = service.create(ClientType.FISICA, request);

        assertNotNull(response.getId());
        verify(repository).save(any());
    }

    @Test
    void testCreateThrowsExceptionIfChaveExists() {
        when(repository.existsByValorChave(request.valorChave())).thenReturn(true);

        assertThrows(DomainException.class, () -> service.create(ClientType.FISICA, request));
    }

    @Test
    void testCreateThrowsExceptionIfLimiteExcedido() {
        when(repository.existsByValorChave(request.valorChave())).thenReturn(false);
        when(repository.countByNumeroConta(request.numeroConta())).thenReturn(5L);

        assertThrows(DomainException.class, () -> service.create(ClientType.FISICA, request));
    }

    @Test
    void testUpdateSuccess() {
        UUID id = UUID.randomUUID();
        PixKeyUpdateDTO dto = new PixKeyUpdateDTO(
                AccountType.POUPANCA,
                PixKeyType.EMAIL,
                "teste@email.com",  // mesmo valor da chave original
                1111,
                22223333,
                "Joana",
                "Silva"
        );

        PixKey existing = PixKey.builder()
                .id(id)
                .tipoChave(PixKeyType.EMAIL)
                .valorChave("teste@email.com")
                .tipoConta(AccountType.CORRENTE)
                .numeroAgencia(1234)
                .numeroConta(56789012)
                .nomeCorrentista("Mayara")
                .sobrenomeCorrentista("Villa")
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PixKeyResponse result = service.update(id, dto);

        assertEquals("Joana", result.getNomeCorrentista());
        assertEquals("Silva", result.getSobrenomeCorrentista());
    }

    @Test
    void testUpdateThrowsIfIdNotFound() {
        UUID id = UUID.randomUUID();
        PixKeyUpdateDTO dto = new PixKeyUpdateDTO(
                AccountType.CORRENTE,
                PixKeyType.CPF,
                "12345678901",
                1234,
                56789012,
                "Maria",
                "Souza"
        );

        when(repository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.update(id, dto);
        });

        assertEquals("Chave Pix não encontrada", exception.getMessage());
    }

    @Test
    void testGetByIdThrowsIfAllParamsNull() {
        assertThrows(NotFoundException.class, () -> service.getById(null, null, null));
    }

    @Test
    void testGetByIdThrowsIfIdWithOtherParams() {
        UUID id = UUID.randomUUID();

        assertThrows(DomainException.class, () -> service.getById(id, "Mayara", PixKeyType.CPF));
    }

    @Test
    void testGetByIdSuccess() {
        when(repository.findByFilters("Mayara", PixKeyType.EMAIL)).thenReturn(List.of(pixKey));

        List<PixKeyResponse> result = service.getById(null, "Mayara", PixKeyType.EMAIL);

        assertEquals(1, result.size());
        assertEquals("Mayara", result.get(0).getNomeCorrentista());
    }

    @Test
    void testGetByIdNotFound() {
        when(repository.findByFilters("Mayara", PixKeyType.EMAIL)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> service.getById(null, "Mayara", PixKeyType.EMAIL));
    }

    @Test
    void testDeleteSuccess() {
        when(repository.findById(pixKey.getId())).thenReturn(Optional.of(pixKey));

        PixKeyResponse response = service.delete(pixKey.getId());

        assertEquals(pixKey.getId(), response.getId());
        verify(repository).delete(pixKey);
    }

    @Test
    void testDeleteNotFound() {
        when(repository.findById(pixKey.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.delete(pixKey.getId()));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        PixKeyRequest request = new PixKeyRequest(
                PixKeyType.EMAIL,
                "email-invalido-sem-arroba",
                AccountType.CORRENTE,
                1234,
                56789012,
                "João",
                "Silva"
        );

        lenient().when(repository.existsByValorChave(request.valorChave())).thenReturn(false);
        lenient().when(repository.countByNumeroConta(request.numeroConta())).thenReturn(0L);

        // Act + Assert
        DomainException ex = assertThrows(DomainException.class, () -> service.create(ClientType.FISICA, request));
        assertEquals("Valor inválido para o tipo de chave: EMAIL", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCpfIsInvalid() {
        PixKeyRequest request = new PixKeyRequest(
                PixKeyType.CPF,
                "123", // CPF inválido
                AccountType.CORRENTE,
                1234,
                56789012,
                "Maria",
                "Oliveira"
        );

        lenient().when(repository.existsByValorChave(request.valorChave())).thenReturn(false);
        lenient().when(repository.countByNumeroConta(request.numeroConta())).thenReturn(0L);

        DomainException ex = assertThrows(DomainException.class, () -> service.create(ClientType.FISICA, request));
        assertEquals("Valor inválido para o tipo de chave: CPF", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCnpjIsInvalid() {
        PixKeyRequest request = new PixKeyRequest(
                PixKeyType.CNPJ,
                "12345678",
                AccountType.CORRENTE,
                1234,
                56789012,
                "Carlos",
                "Moraes"
        );

        lenient().when(repository.existsByValorChave(request.valorChave())).thenReturn(false);
        lenient().when(repository.countByNumeroConta(request.numeroConta())).thenReturn(0L);

        DomainException ex = assertThrows(DomainException.class, () -> service.create(ClientType.FISICA, request));
        assertEquals("Valor inválido para o tipo de chave: CNPJ", ex.getMessage());
    }

    @Test
    void testGetByIdThrowsNotFoundWhenNoFilterProvided() {
        assertThrows(NotFoundException.class, () -> service.getById(null, null, null));
    }

    @Test
    void testGetByIdThrowsDomainExceptionWhenIdAndOtherFiltersProvided() {
        UUID id = UUID.randomUUID();
        String nomeCorrentista = "João";

        assertThrows(DomainException.class, () -> service.getById(id, nomeCorrentista, null));
    }

    @Test
    void shouldThrowExceptionWhenLimiteExcedidoParaClienteJuridico() {
        PixKeyRequest request = new PixKeyRequest(
                PixKeyType.CNPJ,
                "12345678901234",
                AccountType.CORRENTE,
                1234,
                56789012,
                "Empresa",
                "LTDA"
        );

        when(repository.existsByValorChave(request.valorChave())).thenReturn(false);
        when(repository.countByNumeroConta(request.numeroConta())).thenReturn(20L);

        DomainException ex = assertThrows(DomainException.class, () ->
                service.create(ClientType.JURIDICA, request));

        assertEquals("Limite de chaves por chavePix excedido", ex.getMessage());
    }
}