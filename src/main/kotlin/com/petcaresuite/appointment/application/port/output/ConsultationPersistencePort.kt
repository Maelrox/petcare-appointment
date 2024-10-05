package com.petcaresuite.appointment.application.port.output

import com.petcaresuite.appointment.domain.model.Consultation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ConsultationPersistencePort {

     fun save(consultation: Consultation): Consultation?

     fun update(consultation: Consultation): Consultation?

     fun findAllByFilterPageable(filter: Consultation, pageable: Pageable): Page<Consultation>

     fun findByConsultationId(consultationId: Long, companyId: Long): Consultation

}