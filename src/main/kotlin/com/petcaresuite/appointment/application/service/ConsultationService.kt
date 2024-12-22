package com.petcaresuite.appointment.application.service

import com.petcaresuite.appointment.application.dto.*
import com.petcaresuite.appointment.application.mapper.ConsultationMapper
import com.petcaresuite.appointment.application.port.input.ConsultationUseCase
import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.application.port.output.ConsultationPersistencePort
import com.petcaresuite.appointment.application.service.messages.Responses
import com.petcaresuite.appointment.domain.exception.ConsultInvalidException
import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.domain.model.Consultation
import com.petcaresuite.appointment.domain.service.AppointmentDomainService
import com.petcaresuite.appointment.domain.service.ConsultDomainService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ConsultationService(
    private val consultationPersistencePort: ConsultationPersistencePort,
    private val consultationMapper: ConsultationMapper,
    private val appointmentPersistencePort: AppointmentPersistencePort,
    private val consultDomainService: ConsultDomainService,
    private val appointmentDomainService: AppointmentDomainService
) :
    ConsultationUseCase {
    override fun save(consultationDTO: ConsultationDTO): ResponseDTO {
        val appointment = appointmentPersistencePort.findByAppointmentId(consultationDTO.appointmentId, consultationDTO.companyId!!)
        validateConsultation(consultationDTO, appointment)
        consultationDTO.createdAt = LocalDateTime.now()
        consultationDTO.updatedAt = LocalDateTime.now()
        val consultation = consultationMapper.toDomain(consultationDTO)
        consultationPersistencePort.save(consultation)
        appointmentDomainService.applyAttendedStatus(appointment)
        appointmentPersistencePort.save(appointment)
        return ResponseDTO(message = Responses.CONSULTATION_ATTENDED)
    }

    override fun update(consultationDTO: ConsultationDTO): ResponseDTO {
        val appointment = appointmentPersistencePort.findByAppointmentId(consultationDTO.appointmentId, consultationDTO.companyId!!)
        validateConsultation(consultationDTO, appointment)
        val consultation = consultationPersistencePort.findByConsultationId(consultationDTO.consultationId!!, consultationDTO.companyId!!)
        setUpdatableFields(consultation, consultationDTO)
        val consultationUpdate = consultationMapper.toDomain(consultationDTO)
        consultationPersistencePort.update(consultationUpdate)
        return ResponseDTO(message = Responses.CONSULTATION_UPDATED)
    }

    fun setUpdatableFields(consultation: Consultation, consultationDTO: ConsultationDTO) {
        consultationDTO.createdAt = consultation.createdAt
        consultationDTO.updatedAt = consultation.updatedAt
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

    override fun cancel(consultationId: Long, companyId: Long): ResponseDTO? {
        val consultation = consultationPersistencePort.findByConsultationId(consultationId, companyId)
        val appointment = appointmentPersistencePort.findByAppointmentId(consultation.appointmentId!!, companyId)
        consultDomainService.cancelConsultation(appointment, consultation)
        return ResponseDTO(message = Responses.CONSULTATION_CANCELLED)
    }

    private fun validateConsultation(consultationDTO: ConsultationDTO, appointment: Appointment) {
        if (consultationDTO.consultationDate.isBefore(LocalDateTime.now())) {
            throw ConsultInvalidException(Responses.APPOINTMENT_INVALID_DATE)
        }
        if (consultationDTO.consultationId == null) {
            consultDomainService.validateAppointment(appointment, consultationDTO.companyId!!)
        } else {
            consultDomainService.validateUpdateAppointment(consultationDTO.appointmentId, appointment, consultationDTO.companyId!!)
        }
    }

}