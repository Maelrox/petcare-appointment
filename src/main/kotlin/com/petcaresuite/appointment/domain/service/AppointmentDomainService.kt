package com.petcaresuite.appointment.domain.service

import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.application.port.output.ConsultationPersistencePort
import com.petcaresuite.appointment.application.service.messages.Responses
import com.petcaresuite.appointment.domain.exception.AppointmentConflictException
import com.petcaresuite.appointment.domain.exception.AppointmentInvalidException
import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.domain.valueobject.AppointmentStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AppointmentDomainService(
    private val appointmentPersistencePort: AppointmentPersistencePort,
    private val consultationPersistencePort: ConsultationPersistencePort,
) {

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
        val isAppointmentInList =
            appointmentId != null && conflictingAppointments.any { it.appointmentId == appointmentId }

        if (!isAppointmentInList && conflictingAppointments.isNotEmpty()) {
            throw AppointmentConflictException(Responses.APPOINTMENT_CONFLICT)
        }
    }

    fun applyInitialDate(filter: Appointment) {
        filter.initialDate =
            filter.initialDate ?: LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
    }

    fun applyInitialStatus(appointment: Appointment) {
        appointment.status = AppointmentStatus.SCHEDULED.name
    }

    fun applyAttendedStatus(appointment: Appointment) {
        appointment.status = AppointmentStatus.ATTENDED.name
    }

    fun setUpdatableFields(appointment: Appointment, appointmentNewData: Appointment): Appointment {
        return Appointment(
            appointmentId = appointment.appointmentId,
            patientId = appointmentNewData.patientId,
            vetId = appointmentNewData.vetId,
            appointmentDate = appointmentNewData.appointmentDate,
            reason = appointmentNewData.reason,
            companyId = appointment.companyId,
            status = AppointmentStatus.SCHEDULED.name,
            serviceId = appointmentNewData.serviceId,
            ownerId = null,
            specieName = null,
            initialDate = null,
            finalDate = null
        )
    }

    fun validatePatientAndOwnerId(patientId: Long, companyId: Long) {
        appointmentPersistencePort.findOwnerIdByPatientIdAndCompanyId(patientId, companyId)
            ?: throw AppointmentInvalidException(Responses.APPOINTMENT_INVALID_PATIENT)
    }

    fun cancelAppointment(appointment: Appointment) {
        if (appointment.status == AppointmentStatus.CANCELLED.name) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_ALREADY_CANCELLED)
        }
        if (appointment.status == AppointmentStatus.PAID.name) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_ALREADY_PAID)
        }
        if (appointment.status == AppointmentStatus.ATTENDED.name) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_ALREADY_ATTENDED)
        }
        if (appointment.status != AppointmentStatus.SCHEDULED.name) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_NOT_SCHEDULED)
        }
        if (consultationPersistencePort.existByConsultationId(appointment.appointmentId ?: throw IllegalArgumentException(Responses.APPOINTMENT_ID_REQUIRED))) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_HAS_CONSULT)
        }

        appointment.status = AppointmentStatus.CANCELLED.name
        appointmentPersistencePort.save(appointment)
    }

    fun excludeStatus(appointmentId: Long?, excludeStatuses: MutableList<String>) {
        excludeStatuses.add(AppointmentStatus.CANCELLED.name)
        if (appointmentId == null) {
            excludeStatuses.add(AppointmentStatus.ATTENDED.name)
            excludeStatuses.add(AppointmentStatus.PAID.name)
        }
    }

}