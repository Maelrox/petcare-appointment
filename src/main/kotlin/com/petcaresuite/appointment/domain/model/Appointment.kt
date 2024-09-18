package com.petcaresuite.appointment.domain.model

import java.time.LocalDateTime

data class Appointment(
    val appointmentId: Long?,
    val patientId: Long?,
    val vetId: Long?,
    val appointmentDate: LocalDateTime?,
    val reason: String?,
    var companyId: Long?,
    val status: String?,
    var ownerId: Long?

)