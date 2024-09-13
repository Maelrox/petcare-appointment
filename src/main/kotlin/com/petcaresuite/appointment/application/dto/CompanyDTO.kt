package com.petcaresuite.appointment.application.dto

data class CompanyDTO(
    val id: Long? = 0,
    val name: String,
    val country: String?,
    val companyIdentification: String,
)
