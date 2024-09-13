package com.petcaresuite.appointment.domain.service

import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import org.springframework.stereotype.Service

@Service
class AppointmentDomainService(private val appointmentPersistencePort : AppointmentPersistencePort) {

    fun validateAppointment(name: String, id: Long) {

    }

}