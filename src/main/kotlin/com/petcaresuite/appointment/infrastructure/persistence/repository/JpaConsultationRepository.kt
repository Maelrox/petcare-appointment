package com.petcaresuite.appointment.infrastructure.persistence.repository

import com.petcaresuite.appointment.domain.model.Consultation
import com.petcaresuite.appointment.infrastructure.persistence.entity.ConsultationEntity
import com.petcaresuite.appointment.infrastructure.persistence.mapper.ConsultationProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface JpaConsultationRepository : JpaRepository<ConsultationEntity, Long> {

    @Query(
        value = """
            SELECT c.consultation_id as consultationId, c.patient_id as patientId, c.vet_id as vetId, 
                   c.company_id as companyId, c.appointment_id as appointmentId,
                   c.reason as reason, c.diagnosis as diagnosis, c.treatment as treatment, c.notes as notes,
                   c.follow_up_date as followUpDate, c.consultation_date as consultationDate, v.name as veterinaryName,
                   o.name as ownerName, p.name as patientName, a.appointment_date as appointmentDate
            FROM consultations c
            JOIN patients p ON c.patient_id = p.patient_id
            JOIN owners o ON p.owner_id = o.id
            JOIN vets v ON c.vet_id = v.vet_id
            JOIN appointments a ON a.appointment_id = c.appointment_id
            WHERE c.company_id = :#{#filter.companyId}
            AND (:#{#filter.patientId} IS NULL OR :#{#filter.patientId} = 0 OR c.patient_id = :#{#filter.patientId})
            AND (:#{#filter.vetId} IS NULL OR :#{#filter.vetId} = 0 OR c.vet_id = :#{#filter.vetId})
            AND (:#{#filter.status} IS NULL OR :#{#filter.status} = '' OR c.status = :#{#filter.status})
            ORDER BY a.appointment_date ASC
        """,
        nativeQuery = true
    )
    fun findAllByFilter(filter: Consultation): List<ConsultationProjection>

    fun findByConsultationIdAndCompanyId(consultationId: Long, companyId: Long): ConsultationEntity

}