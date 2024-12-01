package com.petcaresuite.appointment.infrastructure.persistence.adapter

import com.petcaresuite.appointment.application.port.output.ConsultationPersistencePort
import com.petcaresuite.appointment.domain.model.Consultation
import com.petcaresuite.appointment.infrastructure.persistence.mapper.ConsultationEntityMapper
import com.petcaresuite.appointment.infrastructure.persistence.repository.JpaAppointmentRepository
import com.petcaresuite.appointment.infrastructure.persistence.repository.JpaConsultationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class ConsultationRepositoryAdapter(
    private val jpaConsultationRepository: JpaConsultationRepository,
    private val jpaAppointmentRepository: JpaAppointmentRepository,
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

    override fun findAllByFilterPageable(filter: Consultation, pageable: Pageable): Page<Consultation> {
        val pagedConsultationsEntity = jpaConsultationRepository.findAllByFilter(filter, pageable)
        return pagedConsultationsEntity.map { consultationMapper.toDomain(it) }

    }

    override fun findByConsultationId(consultationId: Long, companyId: Long): Consultation {
        val consultationEntity = jpaConsultationRepository.findByConsultationIdAndCompanyId(consultationId, companyId)
        val consultation = consultationMapper.toDomain(consultationEntity)
        val ownerId = jpaAppointmentRepository.getPatientOwner(consultation.patientId!!)
        consultation.ownerId = ownerId
        return consultation
    }

    override fun findAllAttendedByOwnerIdAndCompanyId(ownerId: Long, companyId: Long): List<Consultation> {
        val consultationsEntity = jpaConsultationRepository.findAllAttendedByOwnerAndCompanyId(ownerId, companyId)
        return consultationMapper.toDomain(consultationsEntity)
    }

    override fun existByConsultationId(consultationId: Long): Boolean {
        return jpaConsultationRepository.existsById(consultationId)
    }

}