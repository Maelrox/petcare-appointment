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

    override fun update(appointment: Appointment): Appointment? {
        val appointmentEntity = appointmentMapper.toEntity(appointment)
        jpaAppointmentRepository.save(appointmentEntity)
        return appointmentMapper.toDomain(appointmentEntity)
    }

    override fun findAllByFilter(filter: Appointment): List<Appointment> {
        val appointments = jpaAppointmentRepository.findAllByFilterWithSpecieName(filter)
        return appointmentMapper.dtoToDomain(appointments)
    }

    override fun findByAppointmentId(appointmentId: Long, companyId: Long): Appointment {
        val appointmentEntity = jpaAppointmentRepository.findByAppointmentIdAndCompanyId(appointmentId, companyId)
        val appointment = appointmentMapper.toDomain(appointmentEntity)
        val ownerId = jpaAppointmentRepository.getPatientOwner(appointment.patientId!!)
        appointment.ownerId = ownerId
        return appointment
    }

    override fun findByPatientId(patientId: Long, companyId: Long): Appointment {
        val appointmentEntity = jpaAppointmentRepository.findByPatientIdAndCompanyId(patientId, companyId)
        val appointment = appointmentMapper.toDomain(appointmentEntity)
        return appointment
    }

}