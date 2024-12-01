package com.petcaresuite.appointment.application.port.input

import com.petcaresuite.appointment.application.dto.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ConsultationUseCase {
    
    fun save(consultationDTO: ConsultationDTO): ResponseDTO

    fun update(consultationDTO: ConsultationDTO): ResponseDTO?

    fun getAllByFilterPaginated(filterDTO : ConsultationFilterDTO, pageable: Pageable, companyId: Long): Page<ConsultationDTO>

    fun getByConsultationId(consultationId: Long, companyId: Long): ConsultationDTO

    fun getAllAttendedByOwnerId(ownerId: Long, companyId: Long): List<ConsultationDTO>

    fun cancel(consultationId: Long, companyId: Long): ResponseDTO?
}