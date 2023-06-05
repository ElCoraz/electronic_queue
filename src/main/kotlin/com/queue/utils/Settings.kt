package com.queue.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.queue.model.BackStyle
import com.queue.model.Buttons
import com.queue.model.CheckStyle
import org.springframework.core.env.Environment
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class Settings(
    private val environment: Environment) {

    private val settings: com.queue.model.Settings

    val table: String?
        get() = settings.table

    val length: Int?
        get() = settings.length

    val slider: String?
        get() = settings.slider

    val interval: Int?
        get() = settings.interval

    val back: BackStyle?
        get() = settings.backStyle

    val check: CheckStyle?
        get() = settings.checkStyle

    val buttons: Array<Buttons>?
        get() = settings.buttons

    val enterprise: HashMap<String, String>?
        get() = settings.enterprise

    val version: String?
        get() = environment.getProperty("settings.version")

    init {
        val objectMapper = ObjectMapper()
        settings = objectMapper.readValue(String(Files.readAllBytes(Paths.get(Objects.requireNonNull(environment.getProperty("settings.file.name")!!))), StandardCharsets.UTF_8), objectMapper.typeFactory.constructType(com.queue.model.Settings::class.java))
    }
}

