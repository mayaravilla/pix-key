package com.example.pix.interfaces;

import com.example.pix.dto.PixKeyRequest;
import com.example.pix.dto.PixKeyResponse;
import com.example.pix.dto.PixKeyUpdateDTO;
import com.example.pix.enums.ClientType;
import com.example.pix.enums.PixKeyType;

import java.util.List;
import java.util.UUID;

public interface PixKeyServicePort {

    PixKeyResponse create (ClientType clientType, PixKeyRequest request);
    PixKeyResponse update(UUID id, PixKeyUpdateDTO keyUpdateDTO);
    List<PixKeyResponse> getById(UUID id, String nomeCorrentista, PixKeyType tipoChave);

}
