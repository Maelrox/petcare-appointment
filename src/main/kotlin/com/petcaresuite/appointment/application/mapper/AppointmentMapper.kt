package com.petcaresuite.appointment.application.mapper

import com.petcaresuite.appointment.application.dto.AppointmentDTO
import com.petcaresuite.appointment.application.dto.AppointmentFilterDTO
import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.infrastructure.persistence.entity.AppointmentEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface AppointmentMapper {

    fun toDomain(appointmentDTO: AppointmentDTO): Appointment

    fun toDomain(appointmentDTO: AppointmentFilterDTO): Appointment

    fun toDTO(appointment: Appointment): AppointmentDTO

    fun toDTO(appointments: List<Appointment>): List<AppointmentDTO>

}