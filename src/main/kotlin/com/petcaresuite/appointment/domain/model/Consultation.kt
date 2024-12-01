package com.petcaresuite.appointment.domain.model

import java.time.LocalDateTime

data class Consultation(
    val consultationId: Long? = null,
    val patientId: Long? = null,
    val vetId: Long? = null,
    var companyId: Long? = null,
    val appointmentId: Long? = null,
    val consultationDate: LocalDateTime?,
    val reason: String? = null,
    val diagnosis: String? = null,
    val treatment: String? = null,
    val notes: String? = null,
    val followUpDate: LocalDateTime? = null,
    var status: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    var ownerId: Long?,
    val veterinaryName: String? = null,
    val ownerName: String? = null,
    val patientName: String? = null,

) {

}