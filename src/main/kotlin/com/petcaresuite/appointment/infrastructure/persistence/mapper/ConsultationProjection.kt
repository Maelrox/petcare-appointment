package com.petcaresuite.appointment.infrastructure.persistence.mapper

import java.time.LocalDateTime

interface ConsultationProjection {
    val consultationId: Long
    val patientId: Long
    val vetId: Long
    val companyId: Long
    val appointmentId: Long
    val reason: String?
    val diagnosis: String?
    val treatment: String?
    val notes: String?
    val followUpDate: LocalDateTime?
    val status: String
    val consultationDate: LocalDateTime?
    val veterinaryName: String
    val ownerName: String
    val patientName: String
    val appointmentDate: LocalDateTime
}