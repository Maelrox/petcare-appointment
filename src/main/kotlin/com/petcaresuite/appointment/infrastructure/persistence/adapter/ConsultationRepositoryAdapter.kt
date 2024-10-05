package com.petcaresuite.appointment.infrastructure.persistence.adapter

import com.petcaresuite.appointment.application.port.output.ConsultationPersistencePort
import com.petcaresuite.appointment.domain.model.Consultation
import com.petcaresuite.appointment.infrastructure.persistence.mapper.ConsultationEntityMapper
import com.petcaresuite.appointment.infrastructure.persistence.repository.JpaConsultationRepository
import org.springframework.stereotype.Component

@Component
class ConsultationRepositoryAdapter(
    private val jpaConsultationRepository: JpaConsultationRepository,
    private val consultationMapper: ConsultationEntityMapper
) : ConsultationPersistencePort {

    override fun save(consultation: Consultation): Consultation? {
        val consultationEntity = consultationMapper.toEntity(consultation)
        jpaConsultationRepository.save(consultationEntity)
        return consultationMapper.toDomain(consultationEntity)
    }

    override fun update(consultation: Consultation): Consultation? {
        val appointmentEntity = consultationMapper.toEntity(consultation)
        jpaConsultationRepository.save(appointmentEntity)
        return consultationMapper.toDomain(appointmentEntity)
    }

    override fun findAllByFilter(filter: Consultation): List<Consultation> {
        val consultations = jpaConsultationRepository.findAllByFilter(filter)
        return consultationMapper.dtoToDomain(consultations)
    }

    override fun findByConsultationId(consultationId: Long, companyId: Long): Consultation {
        val consultationEntity = jpaConsultationRepository.findByConsultationIdAndCompanyId(consultationId, companyId)
        val consultation = consultationMapper.toDomain(consultationEntity)

        return consultation
    }

}