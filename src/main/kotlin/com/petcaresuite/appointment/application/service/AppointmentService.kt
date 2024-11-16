package com.petcaresuite.appointment.application.service

import com.petcaresuite.appointment.application.dto.*
import com.petcaresuite.appointment.application.mapper.AppointmentMapper
import com.petcaresuite.appointment.application.port.input.AppointmentUseCase
import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.application.service.messages.Responses
import com.petcaresuite.appointment.domain.service.AppointmentDomainService
import org.springframework.stereotype.Service
import java.security.InvalidParameterException

@Service
class AppointmentService(
    private val appointmentPersistencePort: AppointmentPersistencePort,
    private val appointmentMapper: AppointmentMapper,
    private val appointmentDomainService: AppointmentDomainService,
) :
    AppointmentUseCase {
    override fun save(appointmentDTO: AppointmentDTO): ResponseDTO {
        validateCreateAppointment(appointmentDTO)
        val appointment = appointmentMapper.toDomain(appointmentDTO)
        appointmentDomainService.applyInitialStatus(appointment)
        appointmentPersistencePort.save(appointment)
        return ResponseDTO(message = Responses.APPOINTMENT_SCHEDULED)
    }

    override fun update(appointmentDTO: AppointmentDTO): ResponseDTO {
        validateUpdateAppointment(appointmentDTO)
        val appointmentNewData = appointmentMapper.toDomain(appointmentDTO)
        val appointment = appointmentPersistencePort.findByAppointmentId(appointmentDTO.appointmentId!!, appointmentDTO.companyId!!)
        val updatableAppointment = appointmentDomainService.setUpdatableFields(appointment, appointmentNewData)
        appointmentPersistencePort.update(updatableAppointment)
        return ResponseDTO(message = Responses.APPOINTMENT_UPDATED)
    }

    override fun getAllByFilter(filterDTO: AppointmentFilterDTO, companyId: Long): List<AppointmentDTO> {
        val filter = appointmentMapper.toDomain(filterDTO)
        filter.companyId = companyId
        appointmentDomainService.applyInitialDate(filter)
        val appointments = appointmentPersistencePort.findAllByFilter(filter)
        return appointmentMapper.toDTO(appointments)
    }

    override fun getByAppointmentId(appointmentId: Long, companyId: Long): AppointmentDTO {
        val appointment = appointmentPersistencePort.findByAppointmentId(appointmentId, companyId)
        return appointmentMapper.toDTO(appointment)
    }

    override fun getByPatientId(patientId: Long, companyId: Long): List<AppointmentDTO> {
        val appointments = appointmentPersistencePort.findByPatientId(patientId, companyId)
        return appointmentMapper.toDTO(appointments)
    }

    fun validateCreateAppointment(appointmentDTO: AppointmentDTO) {
        if (appointmentDTO.appointmentId != null) {
            throw InvalidParameterException(Responses.APPOINTMENT_ID_NOT_NULL)
        }
        appointmentDomainService.validateDate(appointmentDTO.appointmentDate)
        appointmentDomainService.validateVeterinaryAvailability(
            appointmentDTO.vetId,
            appointmentDTO.appointmentDate,
            null
        )
        appointmentDomainService.validatePatientAndOwnerId(appointmentDTO.patientId, appointmentDTO.companyId!!)
    }

    fun validateUpdateAppointment(appointmentDTO: AppointmentDTO) {
        if (appointmentDTO.appointmentId === null) {
            throw InvalidParameterException(Responses.APPOINTMENT_ID_NOT_NULL)
        }
        appointmentDomainService.validateDate(appointmentDTO.appointmentDate)
        appointmentDomainService.validateVeterinaryAvailability(
            appointmentDTO.vetId,
            appointmentDTO.appointmentDate,
            appointmentDTO.appointmentId
        )
        appointmentDomainService.validatePatientAndOwnerId(appointmentDTO.patientId, appointmentDTO.companyId!!)

    }

}