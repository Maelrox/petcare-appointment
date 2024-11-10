package com.petcaresuite.appointment.application.service.messages

object Responses {
    const val APPOINTMENT_SCHEDULED = "Appointment Scheduled"
    const val APPOINTMENT_UPDATED = "Appointment Updated"
    const val CONSULTATION_ATTENDED = "Consult Attended"
    const val CONSULTATION_UPDATED = "Consult Updated"
    const val APPOINTMENT_CONFLICT = "The selected veterinarian is already scheduled at this time. Please choose another slot"
    const val APPOINTMENT_INVALID_DATE = "Appointment date must be in the future."
    const val APPOINTMENT_INVALID_PATIENT = "The appointment has an invalid patient."
    const val APPOINTMENT_ID_NULL = "Appointment id is null."
    const val APPOINTMENT_ID_NOT_NULL = "Appointment id is not null."
    const val CONSULTS_INVALID_DATE = "Consult date must be in the future."

}