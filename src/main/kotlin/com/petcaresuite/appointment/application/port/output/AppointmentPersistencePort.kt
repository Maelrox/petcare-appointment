package com.petcaresuite.appointment.application.port.output

import com.petcaresuite.appointment.domain.model.Appointment
import java.time.LocalDateTime

interface AppointmentPersistencePort {

    fun save(appointment: Appointment): Appointment?

    fun update(appointment: Appointment): Appointment?

    fun findAllByFilter(filter: Appointment): List<Appointment>

    fun findByAppointmentId(appointmentId: Long, companyId: Long): Appointment

    fun findByPatientId(patientId: Long, companyId: Long, excludeStatuses: List<String>): List<Appointment>

    fun findByPatientIdAndAppointmentId(patientId: Long, appointmentId: Long, companyId: Long, excludeStatuses: List<String>): List<Appointment>

    fun findAllByVetIdScheduled(vetId: Long): List<Appointment>

    fun findOwnerIdByPatientIdAndCompanyId(patientId: Long, companyId: Long): Long?

    fun getAppointmentsByStatusAndDate(status: String, beforeDate: LocalDateTime): List<Appointment>

}