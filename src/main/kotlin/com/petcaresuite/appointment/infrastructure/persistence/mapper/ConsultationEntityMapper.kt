package com.petcaresuite.appointment.infrastructure.persistence.mapper

import com.petcaresuite.appointment.domain.model.Consultation
import com.petcaresuite.appointment.infrastructure.persistence.entity.ConsultationEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ConsultationEntityMapper {

    fun toEntity(consultation: Consultation): ConsultationEntity

    fun toDomain(consultationEntity: ConsultationEntity): Consultation

    fun toDomain(consultationEntity: ConsultationProjection): Consultation

    fun toDomain(consultationsEntity: List<ConsultationBillingProjection>): List<Consultation>

    fun dtoToDomain(consultations: List<ConsultationProjection>): List<Consultation>


}