package com.petcaresuite.appointment.infrastructure.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "consultations")
data class ConsultationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_id")
    val consultationId: Long? = null,

    @Column(name = "patient_id", nullable = false)
    val patientId: Long,

    @Column(name = "vet_id", nullable = false)
    val vetId: Long,

    @Column(name = "appointment_id", nullable = false)
    val appointmentId: Long,

    @Column(name = "company_id", nullable = false)
    val companyId: Long,

    @Column(name = "consultation_date", nullable = false)
    val consultationDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "reason", columnDefinition = "TEXT")
    val reason: String? = null,

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    val diagnosis: String? = null,

    @Column(name = "treatment", columnDefinition = "TEXT")
    val treatment: String? = null,

    @Column(name = "notes", columnDefinition = "TEXT")
    val notes: String? = null,

    @Column(name = "follow_up_date")
    val followUpDate: LocalDateTime? = null,

    @Column(name = "status", nullable = false, length = 20)
    val status: String = "Scheduled",

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "service_id", length = 50)
    val serviceId: Long? = null,

)