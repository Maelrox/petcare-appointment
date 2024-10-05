package com.petcaresuite.appointment.application.service

import com.petcaresuite.appointment.application.dto.*
import com.petcaresuite.appointment.application.mapper.AppointmentMapper
import com.petcaresuite.appointment.application.mapper.ConsultationMapper
import com.petcaresuite.appointment.application.port.input.AppointmentUseCase
import com.petcaresuite.appointment.application.port.input.ConsultationUseCase
import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.application.port.output.ConsultationPersistencePort
import com.petcaresuite.appointment.application.service.messages.Responses
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ConsultationService(
    private val consultationPersistencePort: ConsultationPersistencePort,
    private val consultationMapper: ConsultationMapper,
) :
    ConsultationUseCase {
    override fun save(consultationDTO: ConsultationDTO): ResponseDTO {
        //validateAppointment(appointmentDTO)
        consultationDTO.createdAt = LocalDateTime.now()
        consultationDTO.updatedAt = LocalDateTime.now()
        val consultation = consultationMapper.toDomain(consultationDTO)
        consultationPersistencePort.save(consultation)
        return ResponseDTO(message = Responses.CONSULTATION_ATTENDED)
    }

    override fun update(consultationDTO: ConsultationDTO): ResponseDTO {
        //validateAppointment(appointmentDTO)
        val appointment = consultationMapper.toDomain(consultationDTO)
        consultationPersistencePort.update(appointment)
        return ResponseDTO(message = Responses.CONSULTATION_UPDATED)
    }

    override fun getAllByFilter(filterDTO : ConsultationFilterDTO, companyId: Long): List<ConsultationDTO> {
        val filter = consultationMapper.toDomain(filterDTO)
        filter.companyId = companyId
        val consultations = consultationPersistencePort.findAllByFilter(filter)
        return consultationMapper.toDTO(consultations)
    }

    override fun getByAppointmentId(consultationId: Long, companyId: Long): ConsultationDTO {
        val consultation = consultationPersistencePort.findByConsultationId(consultationId, companyId)
        return consultationMapper.toDTO(consultation)
    }

}