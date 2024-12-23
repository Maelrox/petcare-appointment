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
        val serviceName = jpaAppointmentRepository.getServiceName(appointment.serviceId!!)
        appointment.ownerId = ownerId
        appointment.serviceName = serviceName
        return appointment
    }

    override fun findByPatientId(patientId: Long, companyId: Long, excludeStatuses: List<String>): List<Appointment> {
        val cancelled = excludeStatuses[0]
        val attended = excludeStatuses.getOrNull(1) ?: ""
        val paid = excludeStatuses.getOrNull(2) ?: ""

        val appointmentEntity = jpaAppointmentRepository.findAllByPatientIdAndCompanyId(patientId, companyId, cancelled, attended, paid)
        val appointment = appointmentMapper.projectionToDomain(appointmentEntity)
        return appointment
    }

    override fun findByPatientIdAndAppointmentId(
        patientId: Long,
        appointmentId: Long,
        companyId: Long,
        excludeStatuses: List<String>
    ): List<Appointment> {
        val cancelled = excludeStatuses[0]
        val paid = excludeStatuses.getOrNull(1) ?: ""

        val appointmentEntity = jpaAppointmentRepository.findAllByPatientIdAppointmentIdAndCompanyId(patientId, appointmentId, companyId, cancelled, paid)
        val appointment = appointmentMapper.projectionToDomain(appointmentEntity)
        return appointment
    }

    override fun findAllByVetIdScheduled(vetId: Long): List<Appointment> {
        val scheduledAppoinments = jpaAppointmentRepository.findAllByVetIdScheduled(vetId)
        return appointmentMapper.dtoListToDomain(scheduledAppoinments)
    }

    override fun findOwnerIdByPatientIdAndCompanyId(patientId: Long, companyId: Long): Long {
        return jpaAppointmentRepository.findOwnerIdByPatientIdAndCompanyId(patientId, companyId)
    }

    override fun getAppointmentsByStatusAndDate(status: String, beforeDate: LocalDateTime): List<Appointment> {
        return appointmentMapper.toDomain(jpaAppointmentRepository.findByStatusAndDateBefore(status, beforeDate))
    }

}