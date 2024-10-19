package com.petcaresuite.appointment.infrastructure.persistence.repository

import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.infrastructure.persistence.entity.AppointmentEntity
import com.petcaresuite.appointment.infrastructure.persistence.mapper.AppointmentProjection
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

    @Query(
        value = """
            SELECT a.appointment_id as appointmentId, a.patient_id as patientId, a.vet_id as vetId, 
                   a.company_id as companyId, a.appointment_date as appointmentDate, 
                   a.reason as reason, a.status as status, s.name as specieName
            FROM appointments a
            JOIN patients p ON a.patient_id = p.patient_id
            JOIN species s ON p.specie_id = s.id
            WHERE a.company_id = :#{#filter.companyId}
            AND (:#{#filter.patientId} IS NULL OR :#{#filter.patientId} = 0 OR a.patient_id = :#{#filter.patientId})
            AND (:#{#filter.vetId} IS NULL OR :#{#filter.vetId} = 0 OR a.vet_id = :#{#filter.vetId})
            AND (:#{#filter.status} IS NULL OR :#{#filter.status} = '' OR a.status = :#{#filter.status})
            ORDER BY a.appointment_date ASC
        """,
        nativeQuery = true
    )
    fun findAllByFilterWithSpecieName(filter: Appointment): List<AppointmentProjection>

    fun findByAppointmentIdAndCompanyId(appointmentId: Long, companyId: Long): AppointmentEntity

    @Query(
        """
    SELECT p.owner_id FROM patients p WHERE p.patient_id = :patientId
    """, nativeQuery = true
    )
    fun getPatientOwner(@Param("patientId") patientId: Long): Long?

    fun findAllByPatientIdAndCompanyId(patientId: Long, companyId: Long): List<AppointmentEntity>

}