package com.queue.service

import com.queue.utils.Settings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service
class QueueService {

    @Autowired
    private val environment: Environment? = null

    var queue = mutableListOf<HashMap<Int, Int>>()
    var queues = HashMap<Int, Int>()
    var lastQueues = HashMap<Int, Int>()

    var clients = mutableListOf<Int>()

    var reloadClient = false
    var reloadScoreboard = false

    var number = 0
    var service = ""

    var clearScoreboard = false

    var repeat: HashMap<Int, Int>? = null

    fun load() {
        if (this.queues.isEmpty()) {
            environment?.let { Settings(it).buttons }?.forEach { button ->
                if (button.queue > 0) {
                    this.queues[button.id] = button.queue
                    this.lastQueues[button.queue] = button.queue
                }
                if (button.child.isNotEmpty()) {
                    button.child.forEach { child ->
                        if (child.queue > 0) {
                            this.queues[child.id] = child.queue
                            this.lastQueues[child.queue] = child.queue
                        }
                    }
                }
            }
        }
    }

    fun add(window: Int): HashMap<String, Int> {
        val client : HashMap<String, Int> = HashMap()

        val range: HashMap<Int, Int> = getRange(window)

        if (this.queue.size == environment?.let { Settings(it).length }) {
            this.queue.removeAt(0)
        }

        var index: Int = -1

        if (this.clients.size > 0) {
            val hashMap : HashMap<Int, Int> = HashMap()

            for (i in 0 until this.clients.size) {
                for (j in 0 until range.size) {
                    if (this.clients[i] >= range[j]!! && this.clients[i] <= (range[j]!! + 99)) {
                        index = i
                        break
                    }
                }
                if (index != -1) {
                    break
                }
            }

            if (index != -1) {
                hashMap[window] = this.clients[index]

                this.queue.add(hashMap)
                this.clients.removeAt(index)

                client["client"] = this.clients[index]
            }
        }

        return client
    }

    fun getRange(window: Int): HashMap<Int, Int> {
        val range: HashMap<Int, Int> = HashMap()

        var j = 0

        environment?.let { Settings(it).buttons }?.forEach { button ->
            if (button.queue > 0) {
                for (i in button.windows.indices) {
                    if (button.windows[i] == window) {
                        range[j] = button.queue
                        j++
                    }
                }
            }
            if (button.child.isNotEmpty()) {
                button.child.forEach { child ->
                    for (i in child.windows.indices) {
                        if (child.windows[i] == window) {
                            range[j] = child.queue
                            j++
                        }
                    }
                }
            }
        }
        return range
    }

    fun set(window: Int, client: Int): HashMap<String, Int> {
        val result : HashMap<String, Int> = HashMap()

        if (this.clients.size > 0) {

            val hashMap : HashMap<Int, Int> = HashMap()

            var index: Int = -1

            for (i in 0 until this.clients.size) {
                if (this.clients[i] == client) {
                    index = i
                    break
                }
            }

            if (index != -1) {
                hashMap[window] = this.clients[index]

                this.queue.add(hashMap)
                this.clients.removeAt(index)

                result["client"] = client
            }
        }

        return result
    }

    fun getRangeName(range: Int): String {

        environment?.let { Settings(it).buttons }?.forEach { button ->
            if (button.queue > 0) {
                for (i in button.windows.indices) {
                    if (button.queue == range) {
                        return button.service["ru"].toString().replace("УСЛУГА:", "").trim()
                    }
                }
            }
            if (button.child.isNotEmpty()) {
                button.child.forEach { child ->
                    for (i in child.windows.indices) {
                        if (child.queue == range) {
                            return child.service["ru"].toString().replace("УСЛУГА:", "").trim()
                        }
                    }
                }
            }
        }
        return ""
    }

    fun setService(id: Int, lang: String) {
        environment?.let { Settings(it).buttons }?.forEach { button ->
            if (button.id == id) {
                this.service = button.service[lang].toString()
            }
            if (button.child.isNotEmpty()) {
                button.child.forEach { child ->
                    if (child.id == id) {
                        this.service = child.service[lang].toString()
                    }
                }
            }
        }
    }

    fun remove(window: Int, client: Int) {
        var hashMap : HashMap<Int, Int>? = null

        this.queue.forEach {
            for (item in it) {
                if (item.key == window && item.value == client) {
                    hashMap = it
                }
            }
        }

        if (hashMap != null){
            this.queue.remove(hashMap!!)
        }
    }

    fun addClient(client: Int) {
        this.number = client
        this.clients.add(client)
    }

    fun reloadClient() {
        this.reloadClient = !this.reloadClient
    }

    fun reloadScoreboard() {
        this.reloadScoreboard = !this.reloadScoreboard
    }

    fun repeat(window: Int, client: Int) {
        this.repeat?.put(window, client)
    }

    fun clearScoreboard(value: Boolean) {
        this.clearScoreboard = value
    }

    fun getClientList(window: Int) : HashMap<Int, String>{
        val hashMap : HashMap<Int, String> = HashMap()

        val range: HashMap<Int, Int> = getRange(window)

        if (this.clients.size > 0) {
            for (i in 0 until this.clients.size) {
                for (j in 0 until range.size) {
                    if (this.clients[i] >= range[j]!! && this.clients[i] <= (range[j]!! + 99)) {
                        hashMap[this.clients[i]] = getRangeName(range[j]!!)
                    }
                }
            }
        }

        return hashMap
    }
}
