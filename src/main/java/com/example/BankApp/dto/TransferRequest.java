package com.example.BankApp.dto;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotNull(message = "ID получателя обязателен")
    private Long recipientId;

    @NotNull(message = "Сумма обязательна")
    @Min(value = 0, message = "Сумма должна быть положительной")
    private BigDecimal amount;
}
