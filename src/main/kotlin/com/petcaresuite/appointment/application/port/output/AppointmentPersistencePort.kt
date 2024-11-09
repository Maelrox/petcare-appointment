package com.petcaresuite.appointment.application.port.output

import com.petcaresuite.appointment.domain.model.Appointment
import java.time.LocalDateTime

interface AppointmentPersistencePort {

    fun save(appointment: Appointment): Appointment?

    fun update(appointment: Appointment): Appointment?

    fun findAllByFilter(filter: Appointment): List<Appointment>

    fun findByAppointmentId(appointmentId: Long, companyId: Long): Appointment

    fun findByPatientId(patientId: Long, companyId: Long): List<Appointment>

    fun findConflictingAppointments(
        vetId: Long,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime,
        appointmentStart: LocalDateTime,
        appointmentEnd: LocalDateTime,
        appointmentStartMinusBuffer: LocalDateTime,
        appointmentEndPlusBuffer: LocalDateTime
    ): List<Appointment>

}