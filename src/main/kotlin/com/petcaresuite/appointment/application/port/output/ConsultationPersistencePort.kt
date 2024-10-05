package com.petcaresuite.appointment.application.port.output

import com.petcaresuite.appointment.domain.model.Consultation

interface ConsultationPersistencePort {

     fun save(consultation: Consultation): Consultation?

     fun update(consultation: Consultation): Consultation?

     fun findAllByFilter(filter: Consultation): List<Consultation>

     fun findByConsultationId(consultationId: Long, companyId: Long): Consultation

}