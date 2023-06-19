package com.queue.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.queue.service.QueueService
import com.queue.utils.AudioConcatenator
import com.queue.utils.Settings
import javassist.bytecode.ByteArray
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.*
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.Base64.getEncoder


@ApiIgnore
@RestController
@RequestMapping("/api/")
class ApiController {

    @Autowired
    private val environment: Environment? = null

    @Autowired
    private val queueService: QueueService? = null

    @RequestMapping(value = ["/list"], method = [RequestMethod.GET])
    fun list(): HashMap<Int, Int> {
        val hashMap: HashMap<Int, Int> = HashMap()

        for (item: HashMap<Int, Int> in queueService?.queue!!) {
            hashMap[item.keys.first()] = item.getValue(item.keys.first())
        }

        return hashMap
    }

    @RequestMapping(value = ["/list/clear"], method = [RequestMethod.GET])
    fun clearList(): ResponseEntity<String> {
        if (queueService != null) {
            if (queueService.clearScoreboard) {
                queueService.clearScoreboard(false)
                return ResponseEntity.ok().build()
            }
        }
        return ResponseEntity.noContent().build()
    }

    @RequestMapping(value = ["/show"], method = [RequestMethod.GET])
    fun show(@RequestParam(value = "window", required = false, defaultValue = "0") window: Int): ResponseEntity<String> {
        if (queueService != null) {
            if (queueService.clients.size > 0) {
                val headers = HttpHeaders()
                headers.add("Content-Type", "application/json; charset=utf-8")
                return ResponseEntity((ObjectMapper()).writeValueAsString(queueService.getClientList(window)), headers, HttpStatus.OK)
            } else {
                return ResponseEntity.noContent().build()
            }
        }
        return ResponseEntity.badRequest().build()
    }

    @RequestMapping(value = ["/reload/get/scoreboard"], method = [RequestMethod.GET])
    fun getReloadScoreboard(): ResponseEntity<String> {
        if (queueService != null) {
            if (queueService.reloadScoreboard) {
                queueService.reloadScoreboard()
                return ResponseEntity.ok().build()
            } else {
                return ResponseEntity.noContent().build()
            }
        }
        return ResponseEntity.badRequest().build()
    }

    @RequestMapping(value = ["/reload/get/client"], method = [RequestMethod.GET])
    fun getReloadClient(): ResponseEntity<String> {
        if (queueService != null) {
            if (queueService.reloadClient) {
                queueService.reloadClient()
                return ResponseEntity.ok().build()
            } else {
                return ResponseEntity.noContent().build()
            }
        }
        return ResponseEntity.badRequest().build()
    }

    @RequestMapping(value = ["/reload/set/scoreboard"], method = [RequestMethod.GET])
    fun setReloadScoreboard(): ResponseEntity<String> {
        if (queueService != null) {
            queueService.reloadScoreboard()
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.badRequest().build()
    }

    @RequestMapping(value = ["/reload/set/client"], method = [RequestMethod.GET])
    fun setReloadClient(): ResponseEntity<String> {
        if (queueService != null) {
            queueService.reloadClient()
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.badRequest().build()
    }

    @RequestMapping(value = ["/get"], method = [RequestMethod.GET])
    fun get(@RequestParam(value = "window", required = false, defaultValue = "0") window: Int): ResponseEntity<String> {
        if (queueService != null) {
            if (queueService.clients.size > 0) {
                val headers = HttpHeaders()
                headers.add("Content-Type", "application/json; charset=utf-8")
                return ResponseEntity((ObjectMapper()).writeValueAsString(queueService.add(window)), headers, HttpStatus.OK)
            } else {
                return ResponseEntity.noContent().build()
            }
        }
        return ResponseEntity.badRequest().build()
    }

    @RequestMapping(value = ["/set"], method = [RequestMethod.GET])
    fun set(@RequestParam(value = "window", required = false, defaultValue = "0") window: Int, @RequestParam(value = "client", required = false, defaultValue = "0") client: Int): ResponseEntity<String> {
        if (queueService != null) {
            if (queueService.clients.size > 0) {
                val headers = HttpHeaders()
                headers.add("Content-Type", "application/json; charset=utf-8")
                return ResponseEntity((ObjectMapper()).writeValueAsString(queueService.set(window, client)), headers, HttpStatus.OK)
            } else {
                return ResponseEntity.noContent().build()
            }
        }
        return ResponseEntity.badRequest().build()
    }

    @RequestMapping(value = ["/setRepeat"], method = [RequestMethod.GET])
    fun setRepeat(@RequestParam(value = "window", required = false, defaultValue = "0") window: Int, @RequestParam(value = "client", required = false, defaultValue = "0") client: Int): ResponseEntity<String> {
        if (queueService != null) {
            queueService.repeat(window, client)
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.badRequest().build()
    }

    @RequestMapping(value = ["/play"], method = [RequestMethod.GET])
    fun play(@RequestParam(value = "window", required = false, defaultValue = "0") window: Int, @RequestParam(value = "client", required = false, defaultValue = "0") client: Int): ResponseEntity<kotlin.ByteArray> {
        val byteArray = AudioConcatenator().combined(window, client)

        if (byteArray.size.toLong() > 0) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(byteArray.size.toLong())
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment()
                                    .filename("audio.ogg")
                                    .build().toString())
                    .body(byteArray)
        }

        return ResponseEntity.noContent().build()
    }

    @RequestMapping(value = ["/repeat"], method = [RequestMethod.GET])
    fun repeat(): ResponseEntity<kotlin.ByteArray> {
        if (queueService != null) {
            if (queueService.repeat != null) {
                val byteArray = queueService.repeat!![queueService.repeat?.keys?.first()]?.let { queueService.repeat?.keys?.first()?.let { it1 -> AudioConcatenator().combined(it1, it) } }

                if (byteArray != null) {
                    if (byteArray.size.toLong() > 0) {
                        return ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .contentLength(byteArray.size.toLong())
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                        ContentDisposition.attachment()
                                                .filename("audio.ogg")
                                                .build().toString())
                                .body(byteArray)
                    }
                }
            }
        }

        return ResponseEntity.noContent().build()
    }

    @RequestMapping(value = ["/remove"], method = [RequestMethod.GET])
    fun remove(@RequestParam(value = "window", required = false, defaultValue = "0") window: Int, @RequestParam(value = "client", required = false, defaultValue = "0") client: Int): ResponseEntity<String> {
        if (queueService != null) {
            if (queueService.queue.size > 0) {
                queueService.remove(window, client)
                return ResponseEntity.ok().build()
            } else {
                return ResponseEntity.noContent().build()
            }
        }
        return ResponseEntity.badRequest().build()
    }

    @RequestMapping(value = ["/print"], method = [RequestMethod.GET])
    fun print(): ResponseEntity<String> {
        if (queueService?.number!! > 0) {
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.noContent().build()
    }

    @RequestMapping(value = ["/productDay"], method = [RequestMethod.GET], produces = ["application/json"])
    fun productDay(): String {
        val json = StringBuilder()

        val connection: HttpURLConnection = (URL(environment?.let { Settings(it).enterprise?.get("url") })).openConnection() as HttpURLConnection

        connection.requestMethod = "GET"

        connection.setRequestProperty("Authorization", "Basic " + getEncoder().encodeToString((environment?.let { Settings(it).enterprise?.get("username") } + ":" + environment?.let { Settings(it).enterprise?.get("password") }).toByteArray(StandardCharsets.UTF_8)))

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val input = BufferedReader(InputStreamReader(connection.inputStream))
            var inputLine: String?
            while (input.readLine().also { inputLine = it } != null) {
                json.append(inputLine.toString())
            }
            input.close()
        }

        return json.toString()
    }
}