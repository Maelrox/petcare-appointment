package com.petcaresuite.appointment.domain.model

data class Vet(
    val id: Long?,
    val identification: String,
    val name: String,
    val phone: String?,
    val specialization: String?
)