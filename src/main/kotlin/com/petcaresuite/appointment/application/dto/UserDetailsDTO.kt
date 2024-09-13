package com.petcaresuite.appointment.application.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.petcaresuite.appointment.application.dto.RoleDTO

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserDetailsDTO(
    val id: Long?,
    val name: String?,
    val email: String?,
    val phone: String?,
    val country: String?,
    val enabled: Boolean?,
    val companyId: Long?,
    val roles: Set<RoleDTO>?
    )
