package com.petcaresuite.appointment.application.port.output

import com.petcaresuite.appointment.domain.model.Appointment

interface AppointmentPersistencePort {

     fun save(appointment: Appointment): Appointment?

     fun findAllByFilter(filter: Appointment): List<Appointment>

}