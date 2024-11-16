package com.petcaresuite.appointment.domain.service

import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.application.service.messages.Responses
import com.petcaresuite.appointment.domain.exception.ConsultInvalidException
import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.domain.valueobject.AppointmentStatus
import org.springframework.stereotype.Service

@Service
class ConsultDomainService(private val appointmentPersistencePort: AppointmentPersistencePort) {

    fun validateAppointment(appointment: Appointment, companyId: Long) {
        if (appointment.status != AppointmentStatus.SCHEDULED.name) {
            throw ConsultInvalidException(Responses.CONSULT_INVALID_APPOINTMENT_STATUS)
        }
        if (appointment.companyId != companyId) {
            throw ConsultInvalidException(Responses.CONSULT_INVALID_APPOINTMENT)
        }
    }

}