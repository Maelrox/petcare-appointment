package com.petcaresuite.appointment.application.port.input

import com.petcaresuite.appointment.application.dto.*

interface ConsultationUseCase {
    
    fun save(consultationDTO: ConsultationDTO): ResponseDTO

    fun update(consultationDTO: ConsultationDTO): ResponseDTO?

    fun getAllByFilter(filterDTO: ConsultationFilterDTO, companyId: Long): List<ConsultationDTO>

    fun getByAppointmentId(consultationId: Long, companyId: Long): ConsultationDTO

}