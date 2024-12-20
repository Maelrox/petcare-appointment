package com.petcaresuite.appointment.domain.model

import java.time.LocalDateTime

data class Appointment(
    val appointmentId: Long?,
    val patientId: Long?,
    val vetId: Long?,
    val appointmentDate: LocalDateTime?,
    val reason: String?,
    var companyId: Long?,
    var status: String?,
    var ownerId: Long?,
    val specieName: String?,
    var initialDate: LocalDateTime?,
    val finalDate: LocalDateTime?,
    val serviceId: Long? = null,
    var serviceName: String? = null,
    )