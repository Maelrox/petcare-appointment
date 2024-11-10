package com.petcaresuite.appointment

import com.petcaresuite.appointment.application.dto.AppointmentDTO
import com.petcaresuite.appointment.application.dto.AppointmentFilterDTO
import com.petcaresuite.appointment.application.mapper.AppointmentMapper
import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.application.service.AppointmentService
import com.petcaresuite.appointment.application.service.messages.Responses
import com.petcaresuite.appointment.domain.exception.AppointmentConflictException
import com.petcaresuite.appointment.domain.exception.AppointmentInvalidException
import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.domain.service.AppointmentDomainService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.security.InvalidParameterException
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class AppointmentServiceTest {

    @Mock
    private lateinit var appointmentPersistencePort: AppointmentPersistencePort

    @Mock
    private lateinit var appointmentMapper: AppointmentMapper

    @Mock
    private lateinit var appointmentDomainService: AppointmentDomainService

    private lateinit var appointmentService: AppointmentService
    private lateinit var mockAppointmentDTO: AppointmentDTO
    private lateinit var mockAppointment: Appointment
    private lateinit var futureDateTime: LocalDateTime

    @BeforeEach
    fun setUp() {
        futureDateTime = LocalDateTime.now().plusDays(1)

        mockAppointmentDTO = AppointmentDTO(
            appointmentId = null,
            patientId = 1L,
            vetId = 1L,
            appointmentDate = futureDateTime,
            companyId = 1L,
            vetName = null,
            reason = "General Check",
            status = "SCHEDULED",
            ownerId = null,
            specieName = null
        )

        mockAppointment = Appointment(
            appointmentId = null,
            patientId = 1L,
            vetId = 1L,
            appointmentDate = futureDateTime,
            companyId = 1L,
            reason = "General Check",
            status = "SCHEDULED",
            ownerId = 1L,
            specieName = null,
            initialDate = null,
            finalDate = null
        )

        appointmentService = AppointmentService(
            appointmentPersistencePort,
            appointmentMapper,
            appointmentDomainService
        )
    }

    @Test
    fun `save - successful appointment creation`() {
        // Given
        Mockito.`when`(appointmentMapper.toDomain(mockAppointmentDTO)).thenReturn(mockAppointment)
        Mockito.`when`(appointmentPersistencePort.save(mockAppointment)).thenReturn(mockAppointment)

        // When
        val result = appointmentService.save(mockAppointmentDTO)

        // Then
        assert(result.message == Responses.APPOINTMENT_SCHEDULED)
        Mockito.verify(appointmentDomainService).validateDate(mockAppointmentDTO.appointmentDate)
        Mockito.verify(appointmentDomainService).validateVeterinaryAvailability(
            mockAppointmentDTO.vetId,
            mockAppointmentDTO.appointmentDate,
            null
        )
        Mockito.verify(appointmentMapper).toDomain(mockAppointmentDTO)
        Mockito.verify(appointmentPersistencePort).save(mockAppointment)
    }

    @Test
    fun `save - throws exception when appointmentId is not null`() {
        // Given
        val invalidDTO = mockAppointmentDTO.copy(appointmentId = 1L)

        // When/Then
        assertThrows<InvalidParameterException> {
            appointmentService.save(invalidDTO)
        }
    }

    @Test
    fun `update - successful appointment update`() {
        // Given
        val updateDTO = mockAppointmentDTO.copy(appointmentId = 1L)
        val updateAppointment = mockAppointment.copy(appointmentId = 1L)

        Mockito.`when`(appointmentMapper.toDomain(updateDTO)).thenReturn(updateAppointment)
        Mockito.`when`(appointmentDomainService.setUpdatableFields(updateAppointment, updateAppointment)).thenReturn(updateAppointment)
        Mockito.`when`(appointmentPersistencePort.findByAppointmentId(updateDTO.appointmentId!!, mockAppointmentDTO.companyId!!)).thenReturn(updateAppointment)
        Mockito.`when`(appointmentPersistencePort.update(updateAppointment)).thenReturn(updateAppointment)

        // When
        val result = appointmentService.update(updateDTO)

        // Then
        assert(result.message == Responses.APPOINTMENT_UPDATED)
        Mockito.verify(appointmentDomainService).validateDate(updateDTO.appointmentDate)
        Mockito.verify(appointmentDomainService).validateVeterinaryAvailability(
            updateDTO.vetId,
            updateDTO.appointmentDate,
            updateDTO.appointmentId
        )
        Mockito.verify(appointmentMapper).toDomain(updateDTO)
        Mockito.verify(appointmentPersistencePort).update(updateAppointment)
    }

    @Test
    fun `update - throws exception when appointmentId is null`() {
        // When/Then
        assertThrows<InvalidParameterException> {
            appointmentService.update(mockAppointmentDTO)
        }
    }

    @Test
    fun `getAllByFilter - returns filtered appointments successfully`() {
        // Given
        val companyId = 1L
        val filterDTO = AppointmentFilterDTO(
            patientId = 1L,
            vetId = 1L,
            companyId = companyId,
            id = null,
            ownerId = null,
            vetName = null,
            status = null,
            initialDate = null,
            finalDate = null
        )
        val appointmentsList = listOf(mockAppointment)
        val appointmentDTOList = listOf(mockAppointmentDTO)

        Mockito.`when`(appointmentMapper.toDomain(filterDTO)).thenReturn(mockAppointment)
        Mockito.`when`(appointmentPersistencePort.findAllByFilter(mockAppointment)).thenReturn(appointmentsList)
        Mockito.`when`(appointmentMapper.toDTO(appointmentsList)).thenReturn(appointmentDTOList)

        // When
        val result = appointmentService.getAllByFilter(filterDTO, companyId)

        // Then
        assert(result == appointmentDTOList)
        Mockito.verify(appointmentMapper).toDomain(filterDTO)
        Mockito.verify(appointmentPersistencePort).findAllByFilter(mockAppointment)
        Mockito.verify(appointmentMapper).toDTO(appointmentsList)
    }

    @Test
    fun `getByAppointmentId - returns appointment successfully`() {
        // Given
        val appointmentId = 1L
        val companyId = 1L
        val appointment = mockAppointment.copy(appointmentId = appointmentId)
        val appointmentDTO = mockAppointmentDTO.copy(appointmentId = appointmentId)

        Mockito.`when`(appointmentPersistencePort.findByAppointmentId(appointmentId, companyId))
            .thenReturn(appointment)
        Mockito.`when`(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO)

        // When
        val result = appointmentService.getByAppointmentId(appointmentId, companyId)

        // Then
        assert(result == appointmentDTO)
        Mockito.verify(appointmentPersistencePort).findByAppointmentId(appointmentId, companyId)
        Mockito.verify(appointmentMapper).toDTO(appointment)
    }

    @Test
    fun `getByPatientId - returns patient appointments successfully`() {
        // Given
        val patientId = 1L
        val companyId = 1L
        val appointmentsList = listOf(mockAppointment)
        val appointmentDTOList = listOf(mockAppointmentDTO)

        Mockito.`when`(appointmentPersistencePort.findByPatientId(patientId, companyId))
            .thenReturn(appointmentsList)
        Mockito.`when`(appointmentMapper.toDTO(appointmentsList)).thenReturn(appointmentDTOList)

        // When
        val result = appointmentService.getByPatientId(patientId, companyId)

        // Then
        assert(result == appointmentDTOList)
        Mockito.verify(appointmentPersistencePort).findByPatientId(patientId, companyId)
        Mockito.verify(appointmentMapper).toDTO(appointmentsList)
    }

    @Test
    fun `validateDate - throws exception for past date`() {
        // Given
        val pastDate = LocalDateTime.now().minusDays(1)
        val appointmentDTO = mockAppointmentDTO.copy(appointmentDate = pastDate)

        Mockito.`when`(appointmentDomainService.validateDate(pastDate))
            .thenThrow(AppointmentInvalidException(Responses.APPOINTMENT_INVALID_DATE))

        // When/Then
        assertThrows<AppointmentInvalidException> {
            appointmentService.validateCreateAppointment(appointmentDTO)
        }
    }

    @Test
    fun `validateVeterinaryAvailability - throws exception for conflicting appointment`() {
        // Given
        Mockito.`when`(appointmentDomainService.validateVeterinaryAvailability(
            mockAppointmentDTO.vetId,
            mockAppointmentDTO.appointmentDate,
            null
        )).thenThrow(AppointmentConflictException(Responses.APPOINTMENT_CONFLICT))

        // When/Then
        assertThrows<AppointmentConflictException> {
            appointmentService.validateCreateAppointment(mockAppointmentDTO)
        }
    }
}