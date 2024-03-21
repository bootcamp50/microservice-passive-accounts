package com.nttdata.microservice.bankpassiveaccounts.utils.impl;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.nttdata.microservice.bankpassiveaccounts.collections.Movement;
import com.nttdata.microservice.bankpassiveaccounts.dto.MovementCommissionDto;
import com.nttdata.microservice.bankpassiveaccounts.dto.MovementDto;
import com.nttdata.microservice.bankpassiveaccounts.utils.MovementUtil;

@Component
public class MovementUtilImpl implements MovementUtil {
	@Override
    public Movement toMovement(MovementDto movementDto) {
        return Movement.builder()
                .operationNumber(UUID.randomUUID().toString())
                .type(movementDto.getType())
                .amount(movementDto.getAmount())
                .build();
    }
	
	@Override
    public MovementCommissionDto toMovementCommissionDto(Movement movement) {
        return MovementCommissionDto.builder()
                .operationNumber(movement.getOperationNumber())
                .amount(movement.getAmount())
                .time(movement.getTime())
                .commission(movement.getCommission())
                .build();
    }
	
}
