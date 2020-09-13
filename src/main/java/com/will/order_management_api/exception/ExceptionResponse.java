package com.will.order_management_api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ExceptionResponse {

    private Date timeStamp;

    private String errorMesseage;

    private int status;
}
