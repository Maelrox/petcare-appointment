package com.petcaresuite.appointment.domain.service

import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.application.service.messages.Responses
import com.petcaresuite.appointment.domain.exception.AppointmentConflictException
import com.petcaresuite.appointment.domain.exception.AppointmentInvalidException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AppointmentDomainService(private val appointmentPersistencePort : AppointmentPersistencePort) {

    val appointmentMaxMinutesDuration: Long = 29

    fun validateDate(appointmentDate: LocalDateTime) {
        if (appointmentDate.isBefore(LocalDateTime.now())) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_INVALID_DATE)
        }
    }

    /**
     * Checks if there is no other appointment for the veterinarian
     * within a 30-minute range starting from the appointmentDate.
     */
    fun validateVeterinaryAvailability(vetId: Long, appointmentDate: LocalDateTime, appointmentId: Long?) {
        val startOfDay = appointmentDate.toLocalDate().atStartOfDay()
        val endOfDay = appointmentDate.toLocalDate().atTime(23, 59, 59)

        val appointmentStart: LocalDateTime = appointmentDate
        val appointmentEnd: LocalDateTime = appointmentDate.plusMinutes(appointmentMaxMinutesDuration)

        val appointmentStartMinusBuffer = appointmentDate.minusMinutes(appointmentMaxMinutesDuration)
        val appointmentEndPlusBuffer = appointmentEnd.plusMinutes(appointmentMaxMinutesDuration)

        val conflictingAppointments = appointmentPersistencePort.findConflictingAppointments(
            vetId,
            startOfDay,
            endOfDay,
            appointmentStart,
            appointmentEnd,
            appointmentStartMinusBuffer,
            appointmentEndPlusBuffer
        )
        val isAppointmentInList = appointmentId != null && conflictingAppointments.any { it.appointmentId == appointmentId }

        if (!isAppointmentInList && conflictingAppointments.isNotEmpty()) {
            throw AppointmentConflictException(Responses.APPOINTMENT_CONFLICT)
        }
    }

}