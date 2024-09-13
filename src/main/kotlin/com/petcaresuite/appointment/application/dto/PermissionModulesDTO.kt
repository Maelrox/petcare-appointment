package com.petcaresuite.appointment.application.dto

import com.fasterxml.jackson.annotation.JsonInclude


@JsonInclude(JsonInclude.Include.NON_NULL)
data class PermissionModulesDTO(
    val id: Long,
    val name: String,
    val modulesAction: Set<ModulesActionDTO>,
    val moduleId: Long
)
