package com.jdc.statements.dto;

import java.time.LocalDateTime;

public record Message(
		int id,
		String title,
		String message,
		LocalDateTime postAt
		) {

	public Message(String title, String message) {
		this(0, title, message,null);
	}
	
	public Message cloneWithId(int id) {
		return new Message(id, this.title,this.message,this.postAt);
	}
}
