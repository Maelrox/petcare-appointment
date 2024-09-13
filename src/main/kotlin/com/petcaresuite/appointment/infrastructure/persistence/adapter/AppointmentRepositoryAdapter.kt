package com.petcaresuite.appointment.infrastructure.persistence.adapter

import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.infrastructure.persistence.mapper.AppointmentEntityMapper
import com.petcaresuite.appointment.infrastructure.persistence.repository.JpaAppointmentRepository
import org.springframework.stereotype.Component

@Component
class AppointmentRepositoryAdapter(
    private val jpaAppointmentRepository: JpaAppointmentRepository,
    private val appointmentMapper: AppointmentEntityMapper
) : AppointmentPersistencePort {

    override fun save(appointment: Appointment): Appointment? {
        val appointmentEntity = appointmentMapper.toEntity(appointment)
        jpaAppointmentRepository.save(appointmentEntity)
        return appointmentMapper.toDomain(appointmentEntity)
    }

    override fun findAllByFilter(filter: Appointment): List<Appointment> {
        val appointments = jpaAppointmentRepository.findAllByFilter(filter)
        return appointmentMapper.toDomain(appointments)
    }

}