package com.petcaresuite.appointment.application.port.input

import com.petcaresuite.appointment.application.dto.*

interface AppointmentUseCase {
    
    fun save(appointmentDTO: AppointmentDTO): ResponseDTO

    fun getAllByFilter(filterDTO: AppointmentFilterDTO, companyId: Long): List<AppointmentDTO>

}