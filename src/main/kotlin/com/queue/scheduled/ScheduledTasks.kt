package com.queue.scheduled

import com.queue.service.QueueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledTasks {

    @Autowired
    private val queueService: QueueService? = null

    @Scheduled(cron = "0 0 1 * * *")
    fun clearScoreboard() {
        queueService?.clearScoreboard(true)
    }
}