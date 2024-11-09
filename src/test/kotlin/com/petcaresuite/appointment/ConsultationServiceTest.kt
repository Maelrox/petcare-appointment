package com.petcaresuite.appointment

import com.petcaresuite.appointment.application.dto.ConsultationDTO
import com.petcaresuite.appointment.application.dto.ConsultationFilterDTO
import com.petcaresuite.appointment.application.mapper.ConsultationMapper
import com.petcaresuite.appointment.application.port.output.ConsultationPersistencePort
import com.petcaresuite.appointment.application.service.ConsultationService
import com.petcaresuite.appointment.application.service.messages.Responses
import com.petcaresuite.appointment.domain.exception.ConsultInvalidException
import com.petcaresuite.appointment.domain.model.Consultation
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class ConsultationServiceTest {

    @Mock
    private lateinit var consultationPersistencePort: ConsultationPersistencePort

    @Mock
    private lateinit var consultationMapper: ConsultationMapper

    private lateinit var consultationService: ConsultationService
    private lateinit var mockConsultationDTO: ConsultationDTO
    private lateinit var mockConsultation: Consultation
    private lateinit var futureDateTime: LocalDateTime

    @BeforeEach
    fun setUp() {
        futureDateTime = LocalDateTime.now().plusDays(1)

        mockConsultationDTO = ConsultationDTO(
            consultationId = null,
            patientId = 1L,
            vetId = 1L,
            appointmentId = 1L,
            consultationDate = futureDateTime,
            reason = "General Check",
            diagnosis = "Healthy",
            treatment = "None required",
            notes = "Regular checkup",
            followUpDate = futureDateTime.plusMonths(1),
            status = "SCHEDULED",
            createdAt = null,
            updatedAt = null,
            companyId = 1L,
            ownerId = 1L
        )

        mockConsultation = Consultation(
            consultationId = null,
            patientId = 1L,
            vetId = 1L,
            appointmentId = 1L,
            consultationDate = futureDateTime,
            reason = "General Check",
            diagnosis = "Healthy",
            treatment = "None required",
            notes = "Regular checkup",
            followUpDate = futureDateTime.plusMonths(1),
            status = "SCHEDULED",
            createdAt = null,
            updatedAt = null,
            companyId = 1L,
            ownerId = 1L
        )

        consultationService = ConsultationService(
            consultationPersistencePort,
            consultationMapper
        )
    }

    @Test
    fun `save - successful consultation creation`() {
        // Given
        Mockito.`when`(consultationMapper.toDomain(mockConsultationDTO)).thenReturn(mockConsultation)
        Mockito.`when`(consultationPersistencePort.save(mockConsultation)).thenReturn(mockConsultation)

        // When
        val result = consultationService.save(mockConsultationDTO)

        // Then
        assert(result.message == Responses.CONSULTATION_ATTENDED)
        Mockito.verify(consultationMapper).toDomain(mockConsultationDTO)
        Mockito.verify(consultationPersistencePort).save(mockConsultation)
    }

    @Test
    fun `save - throws exception for past consultation date`() {
        // Given
        val pastDate = LocalDateTime.now().minusDays(1)
        val invalidDTO = mockConsultationDTO.copy(consultationDate = pastDate)

        // When/Then
        assertThrows<ConsultInvalidException> {
            consultationService.save(invalidDTO)
        }
    }

    @Test
    fun `update - successful consultation update`() {
        // Given
        val updateDTO = mockConsultationDTO.copy(consultationId = 1L)
        val updateConsultation = mockConsultation.copy(consultationId = 1L)

        Mockito.`when`(consultationMapper.toDomain(updateDTO)).thenReturn(updateConsultation)
        Mockito.`when`(consultationPersistencePort.update(updateConsultation)).thenReturn(updateConsultation)

        // When
        val result = consultationService.update(updateDTO)

        // Then
        assert(result.message == Responses.CONSULTATION_UPDATED)
        Mockito.verify(consultationMapper).toDomain(updateDTO)
        Mockito.verify(consultationPersistencePort).update(updateConsultation)
    }

    @Test
    fun `getAllByFilterPaginated - returns filtered consultations successfully`() {
        // Given
        val companyId = 1L
        val pageable = PageRequest.of(0, 10)
        val filterDTO = ConsultationFilterDTO(
            patientId = 1L,
            vetId = 1L,
            consultationId = null,
            status = null,
            appointmentId = null,
            consultationDate = null,
            reason = null,
            diagnosis = null,
            treatment = null,
            notes = null,
            followUpDate = null,
            createdAt = null,
            updatedAt = null
        )
        val consultationsList = listOf(mockConsultation)
        val consultationsPage: Page<Consultation> = PageImpl(consultationsList)
        val consultationDTOList = listOf(mockConsultationDTO)
        val expectedPage: Page<ConsultationDTO> = PageImpl(consultationDTOList)

        Mockito.`when`(consultationMapper.toDomain(filterDTO)).thenReturn(mockConsultation)
        Mockito.`when`(consultationPersistencePort.findAllByFilterPageable(mockConsultation, pageable))
            .thenReturn(consultationsPage)
        Mockito.`when`(consultationMapper.toDTO(mockConsultation)).thenReturn(mockConsultationDTO)

        // When
        val result = consultationService.getAllByFilterPaginated(filterDTO, pageable, companyId)

        // Then
        assert(result.content == expectedPage.content)
        Mockito.verify(consultationMapper).toDomain(filterDTO)
        Mockito.verify(consultationPersistencePort).findAllByFilterPageable(mockConsultation, pageable)
    }

    @Test
    fun `getByConsultationId - returns consultation successfully`() {
        // Given
        val consultationId = 1L
        val companyId = 1L
        val consultation = mockConsultation.copy(consultationId = consultationId)
        val consultationDTO = mockConsultationDTO.copy(consultationId = consultationId)

        Mockito.`when`(consultationPersistencePort.findByConsultationId(consultationId, companyId))
            .thenReturn(consultation)
        Mockito.`when`(consultationMapper.toDTO(consultation)).thenReturn(consultationDTO)

        // When
        val result = consultationService.getByConsultationId(consultationId, companyId)

        // Then
        assert(result == consultationDTO)
        Mockito.verify(consultationPersistencePort).findByConsultationId(consultationId, companyId)
        Mockito.verify(consultationMapper).toDTO(consultation)
    }

    @Test
    fun `getAllAttendedByOwnerId - returns owner consultations successfully`() {
        // Given
        val ownerId = 1L
        val companyId = 1L
        val consultationsList = listOf(mockConsultation)
        val consultationDTOList = listOf(mockConsultationDTO)

        Mockito.`when`(consultationPersistencePort.findAllAttendedByOwnerIdAndCompanyId(ownerId, companyId))
            .thenReturn(consultationsList)
        Mockito.`when`(consultationMapper.toListDomain(consultationsList)).thenReturn(consultationDTOList)

        // When
        val result = consultationService.getAllAttendedByOwnerId(ownerId, companyId)

        // Then
        assert(result == consultationDTOList)
        Mockito.verify(consultationPersistencePort).findAllAttendedByOwnerIdAndCompanyId(ownerId, companyId)
        Mockito.verify(consultationMapper).toListDomain(consultationsList)
    }
}