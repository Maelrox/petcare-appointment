package com.petcaresuite.appointment.domain.model

data class Patient(
    val id: Long?,
    val name: String,
    val species: String?,
    val breed: String?,
    val age: Int?,
    val owner: Owner
)