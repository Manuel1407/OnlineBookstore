package com.ikechukwu.onlinebookstore.model.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class APIResponse {

    private String message;

    private Object data;

}
