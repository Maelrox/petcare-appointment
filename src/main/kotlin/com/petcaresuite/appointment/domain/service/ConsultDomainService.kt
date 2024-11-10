package com.petcaresuite.appointment.domain.service

import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.application.service.messages.Responses
import com.petcaresuite.appointment.domain.exception.AppointmentInvalidException
import com.petcaresuite.appointment.domain.exception.ConsultInvalidException
import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.domain.model.Consultation
import com.petcaresuite.appointment.domain.valueobject.AppointmentStatus
import com.petcaresuite.appointment.domain.valueobject.ConsultationStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ConsultDomainService(private val appointmentPersistencePort: AppointmentPersistencePort) {

    val appointmentMaxMinutesDuration: Long = 29

    fun validateDate(consultationDate: LocalDateTime) {
        if (consultationDate.isBefore(LocalDateTime.now())) {
            throw AppointmentInvalidException(Responses.CONSULT_INVALID_APPOINTMENT)
        }
    }

    fun applyInitialStatus(consultation: Consultation) {
        consultation.status = ConsultationStatus.ATTENDED.name
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
                ownerId = null,
                specieName = null,
                initialDate = null,
                finalDate = null
            )
    }

    fun validateAppointment(appointment: Appointment, companyId: Long) {
        if (appointment.status != AppointmentStatus.SCHEDULED.name) {
            throw ConsultInvalidException(Responses.CONSULT_INVALID_APPOINTMENT_STATUS)
        }
        if (appointment.companyId != companyId) {
            throw ConsultInvalidException(Responses.CONSULT_INVALID_APPOINTMENT)
        }
    }

}