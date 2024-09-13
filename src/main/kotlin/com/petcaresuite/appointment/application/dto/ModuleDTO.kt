package com.petcaresuite.appointment.application.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ModuleDTO(
    val id: Long?,
    val name: String,
    @field:JsonProperty("modulesActions")
    val modulesActionEntities: List<ModulesActionDTO>?,
    val selected: Boolean = false

)