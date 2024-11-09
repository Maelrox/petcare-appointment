package com.petcaresuite.appointment.application.service

import com.petcaresuite.appointment.application.dto.*
import com.petcaresuite.appointment.application.mapper.ConsultationMapper
import com.petcaresuite.appointment.application.port.input.ConsultationUseCase
import com.petcaresuite.appointment.application.port.output.ConsultationPersistencePort
import com.petcaresuite.appointment.application.service.messages.Responses
import com.petcaresuite.appointment.domain.exception.ConsultInvalidException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ConsultationService(
    private val consultationPersistencePort: ConsultationPersistencePort,
    private val consultationMapper: ConsultationMapper,
) :
    ConsultationUseCase {
    override fun save(consultationDTO: ConsultationDTO): ResponseDTO {
        validateConsultation(consultationDTO)
        consultationDTO.createdAt = LocalDateTime.now()
        consultationDTO.updatedAt = LocalDateTime.now()
        val consultation = consultationMapper.toDomain(consultationDTO)
        consultationPersistencePort.save(consultation)
        return ResponseDTO(message = Responses.CONSULTATION_ATTENDED)
    }

    override fun update(consultationDTO: ConsultationDTO): ResponseDTO {
        validateConsultation(consultationDTO)
        val appointment = consultationMapper.toDomain(consultationDTO)
        consultationPersistencePort.update(appointment)
        return ResponseDTO(message = Responses.CONSULTATION_UPDATED)
    }

    override fun getAllByFilterPaginated(filterDTO : ConsultationFilterDTO, pageable: Pageable, companyId: Long): Page<ConsultationDTO> {
        val filter = consultationMapper.toDomain(filterDTO)
        filter.companyId = companyId
        return consultationPersistencePort.findAllByFilterPageable(filter, pageable).map { consultationMapper.toDTO(it) }
    }

    override fun getByConsultationId(consultationId: Long, companyId: Long): ConsultationDTO {
        val consultation = consultationPersistencePort.findByConsultationId(consultationId, companyId)
        return consultationMapper.toDTO(consultation)
    }

    override fun getAllAttendedByOwnerId(ownerId: Long, companyId: Long): List<ConsultationDTO> {
        val consultations = consultationPersistencePort.findAllAttendedByOwnerIdAndCompanyId(ownerId, companyId)
        return consultationMapper.toListDomain(consultations)
    }

    private fun validateConsultation(consultationDTO: ConsultationDTO) {
        if (consultationDTO.consultationDate.isBefore(LocalDateTime.now())) {
            throw ConsultInvalidException(Responses.APPOINTMENT_INVALID_DATE)
        }
    }


}