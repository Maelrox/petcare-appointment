package com.petcaresuite.appointment.infrastructure.persistence.repository

import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.infrastructure.persistence.entity.AppointmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface JpaAppointmentRepository : JpaRepository<AppointmentEntity, Long> {

    @Query(
        """
    SELECT a FROM AppointmentEntity a 
    WHERE a.companyId = :#{#filter.companyId}
    AND (:#{#filter.patientId} IS NULL OR :#{#filter.patientId} = 0 OR a.patientId = :#{#filter.patientId})
    AND (:#{#filter.vetId} IS NULL OR :#{#filter.vetId} = 0 OR a.vetId = :#{#filter.vetId})
    AND (:#{#filter.status} IS NULL OR :#{#filter.status} = '' OR a.status = :#{#filter.status})
    ORDER BY a.appointmentDate ASC 
    """
    )
    fun findAllByFilter(filter: Appointment): List<AppointmentEntity>

    fun findByAppointmentIdAndCompanyId(appointmentId: Long, companyId: Long): AppointmentEntity

    @Query(
        """
    SELECT p.owner_id FROM patients p WHERE p.patient_id = :patientId
    """, nativeQuery = true
    )
    fun getPatientOwner(@Param("patientId") patientId: Long): Long?

}