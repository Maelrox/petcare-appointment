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
@RequestMapping()
class AppointmentController(private val appointmentUseCase: AppointmentUseCase) {

    @PostMapping()
    @Permissions(Modules.APPOINTMENTS, ModuleActions.CREATE)
    fun saveAppointment(@Valid @RequestBody dto: AppointmentDTO, request: HttpServletRequest): ResponseEntity<ResponseDTO> {
        dto.companyId  = request.getAttribute("companyId").toString().toLong()
        return ResponseEntity.ok(appointmentUseCase.save(dto))
    }

    @PutMapping()
    @Permissions(Modules.APPOINTMENTS, ModuleActions.UPDATE)
    fun updateAppointment(@Valid @RequestBody dto: AppointmentDTO, request: HttpServletRequest): ResponseEntity<ResponseDTO> {
        dto.companyId  = request.getAttribute("companyId").toString().toLong()
        return ResponseEntity.ok(appointmentUseCase.update(dto))
    }

    @PatchMapping("/{appointmentId}")
    @Permissions(Modules.APPOINTMENTS, ModuleActions.UPDATE)
    fun cancelAppointment(@PathVariable appointmentId: Long, request: HttpServletRequest): ResponseEntity<ResponseDTO> {
        val companyId  = request.getAttribute("companyId").toString().toLong()
        return ResponseEntity.ok(appointmentUseCase.cancel(appointmentId, companyId))
    }

    @GetMapping("/{appointmentId}")
    @Permissions(Modules.APPOINTMENTS, ModuleActions.VIEW)
    fun getAppointment(@PathVariable appointmentId: Long, request: HttpServletRequest): ResponseEntity<AppointmentDTO> {
        val companyId  = request.getAttribute("companyId").toString().toLong()
        return ResponseEntity.ok(appointmentUseCase.getByAppointmentId(appointmentId, companyId))
    }

    @PostMapping("/search")
    @Permissions(Modules.APPOINTMENTS, ModuleActions.VIEW)
    fun searchAppointments(@RequestBody filterDTO: AppointmentFilterDTO, request: HttpServletRequest): ResponseEntity<List<AppointmentDTO>> {
        val companyId  = request.getAttribute("companyId").toString().toLong()
        return ResponseEntity.ok(appointmentUseCase.getAllByFilter(filterDTO, companyId))
    }

    @GetMapping("{appointmentId}/patient/{patientId}")
    @Permissions(Modules.APPOINTMENTS, ModuleActions.VIEW)
    fun getAppointmentByPatient(@PathVariable appointmentId: Long, @PathVariable patientId: Long, request: HttpServletRequest): ResponseEntity<List<AppointmentDTO>> {
        val companyId  = request.getAttribute("companyId").toString().toLong()
        return ResponseEntity.ok(appointmentUseCase.getByPatientId(patientId, companyId, appointmentId))
    }

    @GetMapping("/patient/scheduled/{patientId}")
    @Permissions(Modules.APPOINTMENTS, ModuleActions.VIEW)
    fun getScheduledAppointmentByPatient(@PathVariable patientId: Long, request: HttpServletRequest): ResponseEntity<List<AppointmentDTO>> {
        val companyId  = request.getAttribute("companyId").toString().toLong()
        return ResponseEntity.ok(appointmentUseCase.getByPatientId(patientId, companyId, null))
    }

}