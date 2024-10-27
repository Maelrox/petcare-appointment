package com.petcaresuite.appointment.infrastructure.persistence.mapper

import java.time.LocalDateTime

interface ConsultationBillingProjection {
    val consultationId: Long
    val reason: String?
    val status: String
    val consultationDate: LocalDateTime
}