package com.petcaresuite.appointment.domain.model

import java.time.LocalDateTime

data class Consultation(
    val consultationId: Long? = null,
    val patientId: Long,
    val vetId: Long,
    var companyId: Long,
    val appointmentId: Long,
    val consultationDate: LocalDateTime = LocalDateTime.now(),
    val reason: String? = null,
    val diagnosis: String? = null,
    val treatment: String? = null,
    val notes: String? = null,
    val followUpDate: LocalDateTime? = null,
    val status: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)