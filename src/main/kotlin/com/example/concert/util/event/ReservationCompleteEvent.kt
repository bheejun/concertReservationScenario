package com.example.concert.util.event

import org.springframework.context.ApplicationEvent
import java.util.UUID

class ReservationCompleteEvent(source : Any, val memberId: UUID, val scheduleId: UUID) :ApplicationEvent(source)
