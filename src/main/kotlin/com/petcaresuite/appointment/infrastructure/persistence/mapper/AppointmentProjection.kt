package com.petcaresuite.appointment.infrastructure.persistence.mapper

import java.time.LocalDateTime

interface AppointmentProjection {
    val appointmentId: Long
    val patientId: Long
    val vetId: Long
    val companyId: Long
    val appointmentDate: LocalDateTime
    val reason: String?
    val status: String?
    val specieName: String?
}