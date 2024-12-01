package com.petcaresuite.appointment.application.service.messages

object Responses {
    const val APPOINTMENT_SCHEDULED = "Appointment Scheduled"
    const val APPOINTMENT_UPDATED = "Appointment Updated"
    const val APPOINTMENT_CANCELLED = "Appointment Cancelled"
    const val CONSULTATION_ATTENDED = "Consult Attended"
    const val CONSULTATION_UPDATED = "Consult Updated"
    const val APPOINTMENT_CONFLICT = "The selected veterinarian is already scheduled at this time. Please choose another slot"
    const val APPOINTMENT_HAS_CONSULT = "The selected appointment was already attended and can't be cancelled"
    const val APPOINTMENT_NOT_SCHEDULED = "The selected appointment is not scheduled"
    const val APPOINTMENT_INVALID_DATE = "Appointment date must be in the future."
    const val APPOINTMENT_INVALID_PATIENT = "The appointment has an invalid patient."
    const val APPOINTMENT_ID_NULL = "Appointment id is null."
    const val APPOINTMENT_ID_NOT_NULL = "Appointment id is not null."
    const val CONSULTS_INVALID_DATE = "Consult date must be in the future."
    const val CONSULT_INVALID_APPOINTMENT = "Invalid appointment."
    const val CONSULT_CANCELLED = "Consult date must be in the future."
    const val CONSULT_INVALID_APPOINTMENT_STATUS = "Invalid appointment status."
    const val CONSULT_CANCEL_ERROR_CONSULT_NOT_ATTENDED = "This consult is not in attended status."
    const val CONSULT_CANCEL_ERROR_APPOINTMENT_NOT_ATTENDED = "Appointment associated to this consult is not attended."
    const val INVALID_DATA = "The operation has invalid data"

}