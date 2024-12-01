package com.petcaresuite.appointment.application.port.input

import com.petcaresuite.appointment.application.dto.*

interface AppointmentUseCase {
    
    fun save(appointmentDTO: AppointmentDTO): ResponseDTO

    fun update(appointmentDTO: AppointmentDTO): ResponseDTO?

    fun getAllByFilter(filterDTO: AppointmentFilterDTO, companyId: Long): List<AppointmentDTO>

    fun getByAppointmentId(appointmentId: Long, companyId: Long): AppointmentDTO

    fun getByPatientId(patientId: Long, companyId: Long): List<AppointmentDTO>

    fun cancel(appointmentId: Long, companyId: Long): ResponseDTO?

}