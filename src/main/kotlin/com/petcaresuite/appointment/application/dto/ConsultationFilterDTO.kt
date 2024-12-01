package com.petcaresuite.appointment.application.dto

import java.time.LocalDateTime

data class ConsultationFilterDTO(
    val consultationId: Long? = null,
    val patientId: Long? = null,
    val vetId: Long? = null,
    val appointmentId: Long? = null,
    val consultationDate: LocalDateTime?,
    val reason: String? = null,
    val diagnosis: String? = null,
    val treatment: String? = null,
    val notes: String? = null,
    val followUpDate: LocalDateTime? = null,
    val status: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val veterinaryName: String?,
    val ownerName: String?,
    val patientName: String?,
)
