package com.petcaresuite.appointment.interfaces.web

import com.petcaresuite.appointment.application.dto.*
import com.petcaresuite.appointment.application.port.input.AppointmentUseCase
import com.petcaresuite.appointment.application.service.modules.ModuleActions
import com.petcaresuite.appointment.application.service.modules.Modules
import com.petcaresuite.appointment.infrastructure.security.Permissions
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/appointments")
class AppointmentController(private val appointmentUseCase: AppointmentUseCase) {

    @PostMapping()
    @Permissions(Modules.APPOINTMENTS, ModuleActions.CREATE)
    fun saveAppointment(@Valid @RequestBody dto: AppointmentDTO, request: HttpServletRequest): ResponseEntity<ResponseDTO> {
        dto.companyId  = request.getAttribute("companyId").toString().toLong()
        return ResponseEntity.ok(appointmentUseCase.save(dto))
    }

    @PostMapping("/search")
    @Permissions(Modules.APPOINTMENTS, ModuleActions.VIEW)
    fun getAppointments(@RequestBody filterDTO: AppointmentFilterDTO, request: HttpServletRequest): ResponseEntity<List<AppointmentDTO>> {
        val companyId  = request.getAttribute("companyId").toString().toLong()
        return ResponseEntity.ok(appointmentUseCase.getAllByFilter(filterDTO, companyId))
    }

}