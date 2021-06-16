package ar.edu.unq.grupoN.backenddesappapi.persistence

import ar.edu.unq.grupoN.backenddesappapi.service.PerformedContent
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PerformedContentRepository: CrudRepository<PerformedContent, String?>