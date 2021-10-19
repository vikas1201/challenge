package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TransferMoney {

	@NotNull
	@NotEmpty
	private String accountFrom;
	@NotNull
	@NotEmpty
	private String accountTo;
	@NotNull
	@Min(value = 0, message = "amount must be positive.")
	private BigDecimal amount;

}
