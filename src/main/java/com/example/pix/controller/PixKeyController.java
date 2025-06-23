package com.example.pix.controller;

import com.example.pix.dto.PixKeyRequest;
import com.example.pix.dto.PixKeyResponse;
import com.example.pix.dto.PixKeyUpdateDTO;
import com.example.pix.enums.PixKeyType;
import com.example.pix.interfaces.PixKeyServicePort;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@AllArgsConstructor
@RequestMapping("/pix-keys")
public class PixKeyController {

    private final PixKeyServicePort servicePort;

    @PostMapping
    public ResponseEntity<PixKeyResponse> create(@Valid @RequestBody PixKeyRequest dto) {
        PixKeyResponse response = servicePort.create(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PixKeyResponse> update(@PathVariable UUID id, @Valid @RequestBody PixKeyUpdateDTO dto) {
        PixKeyResponse response = servicePort.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PixKeyResponse>> getById(@RequestParam(value = "id", required = false) UUID id,
                                                        @RequestParam(value = "nomeCorrentista", required = false) String nomeCorrentista,
                                                        @RequestParam(value = "tipoChave", required = false) PixKeyType tipoChave
    ) {
        List<PixKeyResponse> response = servicePort.getById(id, nomeCorrentista, tipoChave);
        return ResponseEntity.ok(response);
    }
}

