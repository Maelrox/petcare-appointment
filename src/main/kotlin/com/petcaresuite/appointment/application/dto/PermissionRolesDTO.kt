package com.petcaresuite.appointment.application.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PermissionRolesDTO(
    val id: Long?,
    val name: String?,
    var modulesAction: MutableSet<ModulesActionDTO>?,
    val roles: Set<RoleDTO>?,
    var company: CompanyDTO?,
)
