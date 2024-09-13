package com.petcaresuite.appointment.infrastructure.persistence.mapper

import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.infrastructure.persistence.entity.AppointmentEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface AppointmentEntityMapper {

    fun toEntity(roleModel: Appointment): AppointmentEntity

    fun toDomain(roleEntity: AppointmentEntity): Appointment

    fun toDomain(appointments: List<AppointmentEntity>): List<Appointment>

}