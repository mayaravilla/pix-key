package com.example.pix.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorTemplateDTO {

    private String status;
    private String message;
    private String erro;
}
