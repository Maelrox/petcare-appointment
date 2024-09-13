package com.petcaresuite.appointment.application.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ModulesActionDTO(
    val id: Long?,
    val name: String,
    val module: ModuleDTO?,
)