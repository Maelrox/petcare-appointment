package com.petcaresuite.appointment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling

@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
class AppointmentApplication

fun main(args: Array<String>) {
	runApplication<AppointmentApplication>(*args)
}
