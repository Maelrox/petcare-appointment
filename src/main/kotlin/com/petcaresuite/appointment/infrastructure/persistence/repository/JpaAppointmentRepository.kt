package com.petcaresuite.appointment.infrastructure.persistence.repository

import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.infrastructure.persistence.entity.AppointmentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface JpaAppointmentRepository : JpaRepository<AppointmentEntity, Long> {

    @Query(
        """
            SELECT a FROM AppointmentEntity a 
            WHERE a.companyId = :#{#filter.companyId}
            AND (:#{#filter.patientId} IS NULL OR a.patientId = :#{#filter.patientId})
            AND (:#{#filter.vetId} IS NULL OR a.vetId = :#{#filter.vetId})
            AND (:#{#filter.status} IS NULL OR a.status = :#{#filter.status})
            ORDER BY a.appointmentDate desc 
            """
    )
    fun findAllByFilter(filter: Appointment): List<AppointmentEntity>

}