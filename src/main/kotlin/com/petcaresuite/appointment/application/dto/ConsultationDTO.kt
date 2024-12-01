package com.petcaresuite.appointment.application.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ConsultationDTO(
    val consultationId: Long? = null,
    val patientId: Long,
    val vetId: Long,
    val appointmentId: Long,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    val consultationDate: LocalDateTime,
    val reason: String? = null,
    val diagnosis: String? = null,
    val treatment: String? = null,
    val notes: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    val followUpDate: LocalDateTime? = null,
    val status: String?,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
    var companyId: Long?,
    val ownerId: Long?,
    val veterinaryName: String?,
    val ownerName: String?,
    val patientName: String?,
)
