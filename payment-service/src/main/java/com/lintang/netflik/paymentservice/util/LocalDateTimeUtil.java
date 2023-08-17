package com.lintang.netflik.paymentservice.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeUtil {

	public LocalDateTime fromMillis(long millis) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
	}

}
