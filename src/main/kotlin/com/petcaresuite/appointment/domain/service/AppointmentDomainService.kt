package com.petcaresuite.appointment.domain.service

import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.application.port.output.ConsultationPersistencePort
import com.petcaresuite.appointment.application.service.messages.Responses
import com.petcaresuite.appointment.domain.exception.AppointmentConflictException
import com.petcaresuite.appointment.domain.exception.AppointmentInvalidException
import com.petcaresuite.appointment.domain.model.Appointment
import com.petcaresuite.appointment.domain.valueobject.AppointmentStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AppointmentDomainService(
    private val appointmentPersistencePort: AppointmentPersistencePort,
    private val consultationPersistencePort: ConsultationPersistencePort,
) {

    val APPOINTMENT_DURATION: Long = 30

    fun validateDate(appointmentDate: LocalDateTime) {
        if (appointmentDate.isBefore(LocalDateTime.now())) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_INVALID_DATE)
        }
    }

    /**
     * Prevents overlap the agenda of a veterinary
     */
    fun validateVeterinaryAvailability(vetId: Long, appointmentDate: LocalDateTime, appointmentId: Long?) {
        val appointmentStart: LocalDateTime = appointmentDate
        val appointmentEnd = appointmentStart.plusMinutes(APPOINTMENT_DURATION)
        val scheduledAppointments = appointmentPersistencePort.findAllByVetIdScheduled(vetId)

        for (scheduledAppointment in scheduledAppointments) {
            val scheduledAppointmentStart = scheduledAppointment.appointmentDate!!
            val scheduledAppointmentEnd = scheduledAppointmentStart.plusMinutes(APPOINTMENT_DURATION)

            // Check if the new appointment overlaps with the scheduled ones
            val appointmentsOverlap =
                scheduledAppointment.appointmentId != appointmentId &&
                        appointmentStart < scheduledAppointmentEnd &&
                        appointmentEnd > scheduledAppointmentStart

            // Ensure at least X minutes gap between appointments
            val isGapSufficient =
                appointmentStart >= scheduledAppointmentEnd ||
                        appointmentEnd <= scheduledAppointmentStart

            if (appointmentsOverlap || !isGapSufficient) {
                throw AppointmentConflictException(Responses.APPOINTMENT_CONFLICT)
            }
        }
    }


    fun applyInitialDate(filter: Appointment) {
        filter.initialDate =
            filter.initialDate ?: LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
    }

    fun applyInitialStatus(appointment: Appointment) {
        appointment.status = AppointmentStatus.SCHEDULED.name
    }

    fun applyAttendedStatus(appointment: Appointment) {
        appointment.status = AppointmentStatus.ATTENDED.name
    }

    fun setUpdatableFields(appointment: Appointment, appointmentNewData: Appointment): Appointment {
        return Appointment(
            appointmentId = appointment.appointmentId,
            patientId = appointmentNewData.patientId,
            vetId = appointmentNewData.vetId,
            appointmentDate = appointmentNewData.appointmentDate,
            reason = appointmentNewData.reason,
            companyId = appointment.companyId,
            status = AppointmentStatus.SCHEDULED.name,
            serviceId = appointmentNewData.serviceId,
            ownerId = null,
            specieName = null,
            initialDate = null,
            finalDate = null
        )
    }

    fun validatePatientAndOwnerId(patientId: Long, companyId: Long) {
        appointmentPersistencePort.findOwnerIdByPatientIdAndCompanyId(patientId, companyId)
            ?: throw AppointmentInvalidException(Responses.APPOINTMENT_INVALID_PATIENT)
    }

    fun cancelAppointment(appointment: Appointment) {
        if (appointment.status == AppointmentStatus.CANCELLED.name) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_ALREADY_CANCELLED)
        }
        if (appointment.status == AppointmentStatus.PAID.name) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_ALREADY_PAID)
        }
        if (appointment.status == AppointmentStatus.ATTENDED.name) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_ALREADY_ATTENDED)
        }
        if (appointment.status != AppointmentStatus.SCHEDULED.name) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_NOT_SCHEDULED)
        }
        if (consultationPersistencePort.existByConsultationId(appointment.appointmentId ?: throw IllegalArgumentException(Responses.APPOINTMENT_ID_REQUIRED))) {
            throw AppointmentInvalidException(Responses.APPOINTMENT_HAS_CONSULT)
        }

        appointment.status = AppointmentStatus.CANCELLED.name
        appointmentPersistencePort.save(appointment)
    }

    fun excludeStatus(appointmentId: Long?, excludeStatuses: MutableList<String>) {
        excludeStatuses.add(AppointmentStatus.CANCELLED.name)
        if (appointmentId == null) {
            excludeStatuses.add(AppointmentStatus.ATTENDED.name)
            excludeStatuses.add(AppointmentStatus.PAID.name)
        }
    }

}