package com.petcaresuite.appointment.application.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AppointmentFilterDTO(
    val id: Long?,
    val patientId: Long?,
    val ownerId: Long?,
    val vetName: String?,
    val vetId: Long?,
    val status: String?,
    var companyId: Long?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    val initialDate: LocalDateTime?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    val finalDate: LocalDateTime?,
)