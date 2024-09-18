package com.petcaresuite.appointment.application.service

import com.petcaresuite.appointment.application.dto.*
import com.petcaresuite.appointment.application.mapper.AppointmentMapper
import com.petcaresuite.appointment.application.port.input.AppointmentUseCase
import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.application.service.messages.Responses
import org.springframework.stereotype.Service

@Service
class AppointmentService(
    private val appointmentPersistencePort: AppointmentPersistencePort,
    private val appointmentMapper: AppointmentMapper,
) :
    AppointmentUseCase {
    override fun save(appointmentDTO: AppointmentDTO): ResponseDTO {
        //validateAppointment(appointmentDTO)
        val appointment = appointmentMapper.toDomain(appointmentDTO)
        appointmentPersistencePort.save(appointment)
        return ResponseDTO(message = Responses.APPOINTMENT_SCHEDULED)
    }

    override fun update(appointmentDTO: AppointmentDTO): ResponseDTO {
        //validateAppointment(appointmentDTO)
        val appointment = appointmentMapper.toDomain(appointmentDTO)
        appointmentPersistencePort.update(appointment)
        return ResponseDTO(message = Responses.APPOINTMENT_UPDATED)
    }

    override fun getAllByFilter(filterDTO : AppointmentFilterDTO, companyId: Long): List<AppointmentDTO> {
        val filter = appointmentMapper.toDomain(filterDTO)
        filter.companyId = companyId
        val appointments = appointmentPersistencePort.findAllByFilter(filter)
        return appointmentMapper.toDTO(appointments)
    }

    override fun getByAppointmentId(appointmentId: Long, companyId: Long): AppointmentDTO {
        val appointment = appointmentPersistencePort.findByAppointmentId(appointmentId, companyId)
        return appointmentMapper.toDTO(appointment)
    }

}