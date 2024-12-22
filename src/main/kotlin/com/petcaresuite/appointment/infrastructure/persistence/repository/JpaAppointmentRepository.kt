package com.petcaresuite.appointment.infrastructure.persistence.repository

import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.infrastructure.persistence.entity.AppointmentEntity
import com.petcaresuite.appointment.infrastructure.persistence.mapper.AppointmentProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

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
                   a.reason as reason, a.status as status, s.name as specieName, sv.name as serviceName
            FROM appointments a
            JOIN patients p ON a.patient_id = p.patient_id
            JOIN species s ON p.specie_id = s.id
            JOIN services sv ON a.service_id = sv.service_id
            WHERE a.company_id = :#{#filter.companyId}
            AND (:#{#filter.patientId} IS NULL OR :#{#filter.patientId} = 0 OR a.patient_id = :#{#filter.patientId})
            AND (:#{#filter.ownerId} IS NULL OR :#{#filter.ownerId} = 0 OR p.owner_id = :#{#filter.ownerId})
            AND (:#{#filter.vetId} IS NULL OR :#{#filter.vetId} = 0 OR a.vet_id = :#{#filter.vetId})
            AND (:#{#filter.status} IS NULL OR :#{#filter.status} = '' OR a.status = :#{#filter.status})
            AND (a.appointment_date >= :#{#filter.initialDate})
            AND (a.appointment_date <= :#{#filter.finalDate})
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

    @Query(
        value = """
            SELECT a.appointment_id as appointmentId, a.patient_id as patientId, a.vet_id as vetId, 
                   a.company_id as companyId, a.appointment_date as appointmentDate, 
                   a.reason as reason, a.status as status, s.name as specieName, sv.service_id as serviceId,
                   sv.name as serviceName
            FROM appointments a
            JOIN patients p ON a.patient_id = p.patient_id
            JOIN species s ON p.specie_id = s.id
            JOIN services sv ON a.service_id = sv.service_id
            WHERE a.company_id = :#{#companyId}
            AND (:#{#patientId} IS NULL OR :#{#patientId} = 0 OR a.patient_id = :#{#patientId})
            AND a.status != :#{#cancelled}
            AND a.status != :#{#attended}
            AND a.status != :#{#paid}
            ORDER BY a.appointment_date ASC
        """,
        nativeQuery = true
    )
    fun findAllByPatientIdAndCompanyId(
        patientId: Long,
        companyId: Long,
        cancelled: String,
        attended: String,
        paid: String
    ): List<AppointmentProjection>

    @Query(
        value = """
            SELECT a.appointment_id as appointmentId, a.patient_id as patientId, a.vet_id as vetId, 
                   a.company_id as companyId, a.appointment_date as appointmentDate, 
                   a.reason as reason, a.status as status, s.name as specieName, sv.service_id as serviceId,
                   sv.name as serviceName
            FROM appointments a
            JOIN patients p ON a.patient_id = p.patient_id
            JOIN species s ON p.specie_id = s.id
            JOIN services sv ON a.service_id = sv.service_id
            WHERE a.company_id = :#{#companyId}
            AND a.appointment_id = :#{#appointmentId}
            AND a.patient_id = :#{#patientId}
            AND a.status != :#{#cancelled}
            AND a.status != :#{#paid}
            ORDER BY a.appointment_date ASC
        """,
        nativeQuery = true
    )
    fun findAllByPatientIdAppointmentIdAndCompanyId(
        patientId: Long,
        appointmentId: Long,
        companyId: Long,
        cancelled: String,
        paid: String
    ): List<AppointmentProjection>

    @Query(
        """
        SELECT a FROM AppointmentEntity a
        WHERE a.vetId = :#{#vetId}
        AND a.status = 'SCHEDULED' 
    """
    )
    fun findAllByVetIdScheduled(
        vetId: Long
    ): List<AppointmentEntity>

    @Query(
        """
            SELECT a FROM AppointmentEntity a
            WHERE a.vetId = :#{#vetId}
            AND a.status != 'CANCELLED'
            AND (
                a.appointmentDate <= :appointmentStart
                OR a.appointmentDate + :durationMinutes MINUTE > :appointmentStart
            )
            ORDER BY a.appointmentDate DESC
            LIMIT 1
         """
    )
    fun findNearbyVetAppointment(
        vetId: Long,
        appointmentStart: LocalDateTime,
        durationMinutes: Long
    ): AppointmentEntity?

    @Query(
        """
            SELECT p.owner_id FROM patients p 
            JOIN owners o on o.owner_id = p.owner_id
            WHERE p.patient_id = :patientId and o.company_id = :companyId
        """, nativeQuery = true
    )
    fun findOwnerIdByPatientIdAndCompanyId(patientId: Long, companyId: Long): Long

    @Query(
        "SELECT a FROM AppointmentEntity a WHERE a.status = :status AND a.appointmentDate < :finalDate"
    )
    fun findByStatusAndDateBefore(
        @Param("status") status: String,
        @Param("finalDate") finalDate: LocalDateTime
    ): List<AppointmentEntity>

    @Query(
        """
    SELECT s.name FROM services s WHERE s.service_id = :serviceId
    """, nativeQuery = true
    )
    fun getServiceName(@Param("serviceId") serviceId: Long): String?
}