package com.petcaresuite.appointment.application.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AppointmentDTO(
    val appointmentId: Long?,
    val patientId: Long,
    val vetName: String?,
    val vetId: Long?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    val appointmentDate: LocalDateTime,
    val reason: String?,
    val status: String?,
    var companyId: Long?,
    val ownerId: Long?,
    val specieName: String?,
)