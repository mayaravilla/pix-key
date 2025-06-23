package com.example.pix.controller;

import com.example.pix.dto.PixKeyRequest;
import com.example.pix.dto.PixKeyResponse;
import com.example.pix.dto.PixKeyUpdateDTO;
import com.example.pix.enums.AccountType;
import com.example.pix.enums.ClientType;
import com.example.pix.enums.PixKeyType;
import com.example.pix.interfaces.PixKeyServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PixKeyControllerTest {

    @InjectMocks
    private PixKeyController controller;

    @Mock
    private PixKeyServicePort servicePort;

    private UUID id;
    private PixKeyRequest request;
    private PixKeyUpdateDTO updateDTO;
    private PixKeyResponse response;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
        request = new PixKeyRequest(
                PixKeyType.EMAIL,
                "teste@email.com",
                AccountType.CORRENTE,
                1234,
                56789012,
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

        response = new PixKeyResponse();
        response.setId(id);
    }

    @Test
    void testCreate() {
            PixKeyRequest request = new PixKeyRequest(
                    PixKeyType.EMAIL,
                    "teste@email.com",
                    AccountType.CORRENTE,
                    1234,
                    56789,
                    "João",
                    "Silva"
            );

            PixKeyResponse expectedResponse = new PixKeyResponse();
            when(servicePort.create(ClientType.FISICA, request)).thenReturn(expectedResponse);

            // Act
            ResponseEntity<PixKeyResponse> response = controller.create("fisica", request);

            // Assert
            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            assertEquals(expectedResponse, response.getBody());

            verify(servicePort).create(ClientType.FISICA, request);

    }

    @Test
    void testUpdate() {
        when(servicePort.update(eq(id), any())).thenReturn(response);

        ResponseEntity<PixKeyResponse> result = controller.update(id, updateDTO);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(id, result.getBody().getId());

        verify(servicePort).update(id, updateDTO);
    }

    @Test
    void testGetByIdOnly() {
        when(servicePort.getById(id, null, null)).thenReturn(List.of(response));

        ResponseEntity<List<PixKeyResponse>> result = controller.getById(id, null, null);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().size());
        assertEquals(id, result.getBody().get(0).getId());

        verify(servicePort).getById(id, null, null);
    }

    @Test
    void testGetByNomeAndTipoChave() {
        when(servicePort.getById(null, "Joana", PixKeyType.CPF)).thenReturn(List.of(response));

        ResponseEntity<List<PixKeyResponse>> result = controller.getById(null, "Joana", PixKeyType.CPF);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().size());

        verify(servicePort).getById(null, "Joana", PixKeyType.CPF);
    }
}