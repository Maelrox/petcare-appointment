package com.petcaresuite.appointment.application.mapper

import com.petcaresuite.appointment.application.dto.ConsultationDTO
import com.petcaresuite.appointment.application.dto.ConsultationFilterDTO
import com.petcaresuite.appointment.domain.model.Consultation
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ConsultationMapper {

    fun toDomain(consultationDTO: ConsultationDTO): Consultation

    fun toDomain(consultationDTO: ConsultationFilterDTO): Consultation

    fun toDTO(consultation: Consultation): ConsultationDTO

    fun toDTO(consultations: List<Consultation>): List<ConsultationDTO>

}