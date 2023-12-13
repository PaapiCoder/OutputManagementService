package com.jp.oms.dtos;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.Accessors;


@Builder
@Getter
public class OnlineRequestDto {

	@NotNull(message = "CIF Number is required")
	private Long cifNumber;


	@NotNull(message = "Account number is required")
	private Long accountNumber;

	public RequestMessage toRequestMessage(){
		return new RequestMessage().setAccountNumber(accountNumber)
				.setCifNumber(cifNumber);
	}
}
