package com.petcaresuite.appointment.application.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.petcaresuite.appointment.application.dto.PermissionDTO

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RoleDTO(
    val id: Long? = 0,
    val name: String?,
    val permissions: MutableSet<PermissionDTO>?

)