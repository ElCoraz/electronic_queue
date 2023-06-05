package com.queue.controller

import com.queue.utils.Settings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.annotations.ApiIgnore
import java.io.File
import java.nio.file.FileSystems
import javax.servlet.ServletContext


@ApiIgnore
@Controller
class ScoreboardController {

    @Autowired
    private val environment: Environment? = null

    @Autowired
    var context: ServletContext? = null

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun index(model: Model): String {
        model.addAttribute("table", environment?.let { Settings(it).table })
        model.addAttribute("slider", environment?.let { Settings(it).slider })
        model.addAttribute("length", environment?.let { Settings(it).length })
        model.addAttribute("interval", environment?.let { Settings(it).interval })

        val directory = FileSystems.getDefault()
                .getPath("")
                .toAbsolutePath()
                .toString() + "/src/main/resources/static/images/slider"

        var images: Array<String> = emptyArray()

        File(directory).walk().forEach {
            if (it.isFile) {
                if (it.extension == "png" || it.extension == "gif" || it.extension == "jpg" || it.extension == "jpeg") {
                    images += it.name
                }
            }
        }

        model.addAttribute("images", images)

        return "scoreboard/index"
    }
}
