package com.petcaresuite.appointment.infrastructure.persistence.mapper

import java.math.BigDecimal
import java.time.LocalDateTime

interface ConsultationBillingProjection {
    val consultationId: Long
    val reason: String?
    val status: String
    val consultationDate: LocalDateTime
    val serviceId: Long
    val serviceName: String
    val price: BigDecimal
}