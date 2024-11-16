package com.petcaresuite.appointment.infrastructure.persistence.adapter

import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.infrastructure.persistence.mapper.AppointmentEntityMapper
import com.petcaresuite.appointment.infrastructure.persistence.repository.JpaAppointmentRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

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
        val adjustedFilter = filter.copy(
            initialDate = filter.initialDate ?: LocalDateTime.of(2024, 1, 1, 0, 0),
            finalDate = filter.finalDate ?: LocalDateTime.of(2040, 12, 31, 23, 59, 59)
        )
        val appointments = jpaAppointmentRepository.findAllByFilterWithSpecieName(adjustedFilter)
        return appointmentMapper.dtoToDomain(appointments)
    }

    override fun findByAppointmentId(appointmentId: Long, companyId: Long): Appointment {
        val appointmentEntity = jpaAppointmentRepository.findByAppointmentIdAndCompanyId(appointmentId, companyId)
        val appointment = appointmentMapper.toDomain(appointmentEntity)
        val ownerId = jpaAppointmentRepository.getPatientOwner(appointment.patientId!!)
        appointment.ownerId = ownerId
        return appointment
    }

    override fun findByPatientId(patientId: Long, companyId: Long): List<Appointment> {
        val appointmentEntity = jpaAppointmentRepository.findAllByPatientIdAndCompanyId(patientId, companyId)
        val appointment = appointmentMapper.toDomain(appointmentEntity)
        return appointment
    }

    override fun findConflictingAppointments(
        vetId: Long,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime,
        appointmentStart: LocalDateTime,
        appointmentEnd: LocalDateTime,
        appointmentStartMinusBuffer: LocalDateTime,
        appointmentEndPlusBuffer: LocalDateTime
    ): List<Appointment> {
        val conflictingAppointments = jpaAppointmentRepository.findConflictingAppointments(
            vetId,
            startOfDay,
            endOfDay,
            appointmentStart,
            appointmentEnd,
            appointmentStartMinusBuffer,
            appointmentEndPlusBuffer
        )
        return appointmentMapper.dtoListToDomain(conflictingAppointments)
    }

    override fun findOwnerIdByPatientIdAndCompanyId(patientId: Long, companyId: Long): Long {
        return jpaAppointmentRepository.findOwnerIdByPatientIdAndCompanyId(patientId, companyId)
    }

    override fun getAppointmentsByStatusAndDate(status: String, beforeDate: LocalDateTime): List<Appointment> {
        return appointmentMapper.toDomain(jpaAppointmentRepository.findByStatusAndDateBefore(status, beforeDate))
    }

}