package com.nttdata.microservice.bankpassiveaccounts.utils;


import com.nttdata.microservice.bankpassiveaccounts.collections.Movement;
import com.nttdata.microservice.bankpassiveaccounts.dto.MovementCommissionDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.MovementDto;

public interface MovementUtil {
	Movement toMovement(MovementDto operationDto);
	MovementCommissionDto toMovementCommissionDto(Movement movement);
}
