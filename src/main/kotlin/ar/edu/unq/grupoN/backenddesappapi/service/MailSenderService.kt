package ar.edu.unq.grupoN.backenddesappapi.service

import ar.edu.unq.grupoN.backenddesappapi.model.PlatformAdministrator
import ar.edu.unq.grupoN.backenddesappapi.model.imdb.CinematographicContent
import ar.edu.unq.grupoN.backenddesappapi.model.review.Review
import ar.edu.unq.grupoN.backenddesappapi.persistence.CinematographicContentRepository
import ar.edu.unq.grupoN.backenddesappapi.persistence.PlatformAdministratorRepository
import ar.edu.unq.grupoN.backenddesappapi.service.dto.ReviewDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.mail.MessagingException
import javax.mail.internet.MimeMessage

@Service
class MailSenderService {

    @Autowired
    lateinit var platformAdminRepository: PlatformAdministratorRepository

    @Autowired
    private lateinit var contentRepository: CinematographicContentRepository

    @Autowired
    private lateinit var javaMailSender: JavaMailSender

    private val PREFIX = "Api key "

    @Transactional
    fun activeNotificationsTo(apiKey: String, titleId: String): String {
        val platformAdmin = platformAdminRepository.findByUuid(apiKey.replace(PREFIX, ""))
        val content = contentRepository.findById(titleId).get()
        val response: String = if (content.subscribers.any { it.username == platformAdmin.username }){
            "You're already subscribed to $titleId"
        } else {
            content.addToSubscribers(platformAdmin)
            "Subscribed to $titleId succesfully"
        }

        contentRepository.save(content)

        return response
    }

    @Transactional
    fun notifyToAllSubscribersOf(titleId: String, createdReview: ReviewDTO) {
        val content = contentRepository.findById(titleId).get()

        content.subscribers.map { sendMailTo(it, titleId, createdReview.toModel()) }
    }

    private fun sendMailTo(platformAdmin: PlatformAdministrator, titleId: String, review: Review) {
        val mailMessage: MimeMessage = javaMailSender.createMimeMessage()
        lateinit var helper: MimeMessageHelper

        try {
            helper = MimeMessageHelper(mailMessage, true)
            helper.setTo(platformAdmin.email)
            helper.setSubject("New review on $titleId")
            helper.setText(
                """
                     Hello ${platformAdmin.username}, we have registered a new review on a 
                     content who you are subscribed.
                        User platform: ${review.platform}
                        Review valoration: ${review.rating}
                        Review text: ${review.text}
                     
                     Have a nice day,
                     Regards,
                     Re-senia Team.
                """
            )
            javaMailSender.send(mailMessage)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}