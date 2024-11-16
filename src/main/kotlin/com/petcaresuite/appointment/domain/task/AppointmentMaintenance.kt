package com.petcaresuite.appointment.domain.task

import com.petcaresuite.appointment.application.port.output.AppointmentPersistencePort
import com.petcaresuite.appointment.domain.valueobject.AppointmentStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class AppointmentMaintenance(private val appointmentPersistencePort: AppointmentPersistencePort) {

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    @Transactional
    fun cancelNotAttendedAppointments() {
        val beforeDate = LocalDateTime.now().minusDays(1)
        val expiredAppointments =
            appointmentPersistencePort.getAppointmentsByStatusAndDate(AppointmentStatus.SCHEDULED.name, beforeDate)
        expiredAppointments.forEach {
            it.status = AppointmentStatus.CANCELLED.name
            appointmentPersistencePort.save(it)
        }
    }

}