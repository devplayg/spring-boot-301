package com.devplayg.hippo.config

import org.springframework.context.annotation.Configuration
import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener

@Configuration
class HippoHttpSessionListener : HttpSessionListener {
    override fun sessionCreated(event: HttpSessionEvent) {
        event.session.maxInactiveInterval = 0
    }
}
