package com.queue.controller

import com.queue.service.QueueService
import com.queue.utils.Settings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import springfox.documentation.annotations.ApiIgnore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ApiIgnore
@Controller
class ClientController {

    @Autowired
    private val environment: Environment? = null

    @Autowired
    private val queueService: QueueService? = null

    @RequestMapping(value = ["/client"], method = [RequestMethod.GET])
    fun client(model: Model, @RequestParam(value = "lang", required = false, defaultValue = "ru") lang : String, @RequestParam(value = "client", required = false, defaultValue = "0") client : String): String {
        queueService?.load()

        model.addAttribute("lang", lang)
        model.addAttribute("back", environment?.let { Settings(it).back })
        model.addAttribute("client", client)
        model.addAttribute("buttons", environment?.let { Settings(it).buttons })

        return "client/index"
    }

    @RequestMapping(value = ["/button"], method = [RequestMethod.POST])
    fun button(model: Model, @RequestParam(value = "value") value : String, @RequestParam(value = "lang", required = false, defaultValue = "ru") lang : String): String {

        if (queueService?.queues?.isEmpty()!!) {
            queueService.load()
        }

        val index: Int? = queueService.queues[value.toInt()]

        var client: Int? = queueService.lastQueues[index]?.plus(1)

        if (index != null) {
            if (client != null) {
                if (client > (index + 99)) {
                    client = index
                }
            }
        }

        if (index != null) {
            if (client != null) {
                queueService.lastQueues[index] = client
            }
        }

        if (client != null) {
            queueService.addClient(client)
        }

        queueService.setService(value.toInt(), lang)

        return "redirect:/client/check?lang=$lang&client=$client"
    }

    @RequestMapping(value = ["/client/check"], method = [RequestMethod.GET])
    fun check(model: Model, @RequestParam(value = "lang", required = false, defaultValue = "ru") lang : String, @RequestParam(value = "client", required = false, defaultValue = "0") client : String): String {
        model.addAttribute("delay", environment?.let { Settings(it).check?.delay }?.times(1000))
        model.addAttribute("style", environment?.let { Settings(it).check?.style })
        model.addAttribute("check", environment?.let { Settings(it).check?.title?.get(lang) }?.replace("__NUMBER__", client))

        return "client/check"
    }

    @RequestMapping(value = ["/client/print"], method = [RequestMethod.GET])
    fun index(model: Model, @RequestParam(value = "lang", required = false, defaultValue = "ru") lang : String): String {
        model.addAttribute("lang", lang)
        model.addAttribute("number", queueService?.number)
        model.addAttribute("service", queueService?.service)
        model.addAttribute("datetime", (if (lang == "ru") "Время: " else "Уақыт: ") + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(LocalDateTime.now()))

        queueService?.number = 0
        queueService?.service = ""

        return "client/print"
    }

}
