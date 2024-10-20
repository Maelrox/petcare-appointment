package com.petcaresuite.appointment.interfaces.web

import com.petcaresuite.appointment.application.dto.*
import com.petcaresuite.appointment.application.port.input.ConsultationUseCase
import com.petcaresuite.appointment.application.service.modules.ModuleActions
import com.petcaresuite.appointment.application.service.modules.Modules
import com.petcaresuite.appointment.infrastructure.security.Permissions
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/consultation")
class ConsultationController(private val consultationUseCase: ConsultationUseCase) {

    @PostMapping()
    @Permissions(Modules.CONSULTATIONS, ModuleActions.CREATE)
    fun saveConsultation(@Valid @RequestBody dto: ConsultationDTO, request: HttpServletRequest): ResponseEntity<ResponseDTO> {
        dto.companyId  = request.getAttribute("companyId").toString().toLong()
        return ResponseEntity.ok(consultationUseCase.save(dto))
    }

    @PutMapping()
    @Permissions(Modules.CONSULTATIONS, ModuleActions.CREATE)
    fun updateConsultation(@Valid @RequestBody dto: ConsultationDTO, request: HttpServletRequest): ResponseEntity<ResponseDTO> {
        dto.companyId  = request.getAttribute("companyId").toString().toLong()
        return ResponseEntity.ok(consultationUseCase.update(dto))
    }

    @GetMapping("/{consultationId}")
    @Permissions(Modules.CONSULTATIONS, ModuleActions.VIEW)
    fun getConsultation(@PathVariable consultationId: Long, request: HttpServletRequest): ResponseEntity<ConsultationDTO> {
        val companyId  = request.getAttribute("companyId").toString().toLong()
        return ResponseEntity.ok(consultationUseCase.getByConsultationId(consultationId, companyId))
    }

    @PostMapping("/search")
    @Permissions(Modules.CONSULTATIONS, ModuleActions.VIEW)
    fun searchConsultations(@RequestBody filterDTO: ConsultationFilterDTO, @RequestParam(defaultValue = "0") page: Int, @RequestParam(defaultValue = "30") size: Int, request: HttpServletRequest): ResponseEntity<PaginatedResponseDTO<ConsultationDTO>> {
        val pageable = PageRequest.of(page, size)
        val companyId  = request.getAttribute("companyId").toString().toLong()
        val result = consultationUseCase.getAllByFilterPaginated(filterDTO, pageable, companyId)
        val pageDTO = PageDTO(
            page = result.number,
            size = result.size,
            totalElements = result.totalElements,
            totalPages = result.totalPages
        )

        val paginatedResponse = PaginatedResponseDTO(
            data = result.content,
            pagination = pageDTO
        )

        return ResponseEntity.ok(paginatedResponse)
    }

}