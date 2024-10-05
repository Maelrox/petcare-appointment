package com.petcaresuite.appointment.application.dto

import java.time.LocalDateTime

data class ConsultationFilterDTO(
    val consultationId: Long? = null,
    val patientId: Long? = null,
    val vetId: Long? = null,
    val appointmentId: Long? = null,
    val consultationDate: LocalDateTime? = null,
    val reason: String? = null,
    val diagnosis: String? = null,
    val treatment: String? = null,
    val notes: String? = null,
    val followUpDate: LocalDateTime? = null,
    val status: String? = null,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
