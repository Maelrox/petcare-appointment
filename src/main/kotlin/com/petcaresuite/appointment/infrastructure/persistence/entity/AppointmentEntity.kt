package com.petcaresuite.appointment.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import kotlin.jvm.Transient

@Entity
@Table(name = "appointments")
data class AppointmentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    val appointmentId: Long? = null,

    @Column(name = "patient_id", nullable = false)
    val patientId: Long,

    @Column(name = "vet_id", nullable = false)
    val vetId: Long,

    @Column(name = "company_id", nullable = false)
    val companyId: Long,

    @Column(name = "appointment_date", nullable = false)
    val appointmentDate: LocalDateTime,

    @Column(name = "reason")
    val reason: String? = null,

    @Column(name = "status", length = 50)
    val status: String? = null,

)