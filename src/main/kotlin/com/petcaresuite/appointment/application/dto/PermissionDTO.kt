package com.petcaresuite.appointment.application.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PermissionDTO(
    val id: Long?,
    val name: String?,
    var modulesAction: MutableSet<ModulesActionDTO>?,
    val role: RoleDTO?,
)
