package com.petcaresuite.appointment.domain.service

import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.application.port.output.ConsultationPersistencePort
import com.petcaresuite.appointment.application.service.messages.Responses
import com.petcaresuite.appointment.domain.exception.AppointmentInvalidException
import com.petcaresuite.appointment.domain.exception.ConsultInvalidException
import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.domain.model.Consultation
import com.petcaresuite.appointment.domain.valueobject.AppointmentStatus
import com.petcaresuite.appointment.domain.valueobject.ConsultationStatus
import org.springframework.stereotype.Service

@Service
class ConsultDomainService(
    private val appointmentPersistencePort: AppointmentPersistencePort,
    private val consultationPersistencePort: ConsultationPersistencePort
) {

    fun validateAppointment(appointment: Appointment, companyId: Long) {
        if (appointment.status != AppointmentStatus.SCHEDULED.name) {
            throw ConsultInvalidException(Responses.CONSULTATION_INVALID_STATUS)
        }
        if (appointment.companyId != companyId) {
            throw ConsultInvalidException(Responses.CONSULTATION_INVALID_APPOINTMENT)
        }
    }

    fun validateUpdateAppointment(currentAppointmentId: Long, appointment: Appointment, companyId: Long) {
        if (currentAppointmentId != appointment.appointmentId && appointment.status != AppointmentStatus.SCHEDULED.name) {
            throw ConsultInvalidException(Responses.CONSULTATION_INVALID_STATUS)
        }
        if (appointment.companyId != companyId) {
            throw ConsultInvalidException(Responses.CONSULTATION_INVALID_APPOINTMENT)
        }
    }

    fun cancelConsultation(appointment: Appointment, consultation: Consultation) {
        if (consultation.status == ConsultationStatus.CANCELLED.name) {
            throw AppointmentInvalidException(Responses.CONSULTATION_ALREADY_PAID)
        }
        if (consultation.status == ConsultationStatus.PAID.name) {
            throw AppointmentInvalidException(Responses.CONSULTATION_ALREADY_PAID)
        }
        if (consultation.status != ConsultationStatus.ATTENDED.name) {
            throw AppointmentInvalidException(Responses.CONSULTATION_NOT_ATTENDED)
        }
        if (appointment.status != AppointmentStatus.ATTENDED.name) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_NOT_ATTENDED)
        }
        appointment.status = AppointmentStatus.CANCELLED.name
        consultation.status = ConsultationStatus.CANCELLED.name
        appointmentPersistencePort.save(appointment)
        consultationPersistencePort.save(consultation)
    }

}