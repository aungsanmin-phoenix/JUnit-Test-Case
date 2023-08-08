package com.jdc.statement.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.jdc.statement.DatabaseInitializar;
import com.jdc.statements.ConnectionManager;
import com.jdc.statements.dao.MessageDao;
import com.jdc.statements.dto.Message;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MesageDaoTest {
	MessageDao dao;
	Message message = new Message("asm", "Aung San Min");

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		DatabaseInitializar.truncate("message");
	}

	@BeforeEach
	void setUpBefore() {
		dao = new MessageDao(ConnectionManager.getInstance());
	}

	@Test
	@Order(1)
	void testCreateMessage() {
		var result = dao.createMessage(message);
		assertEquals(1, result.id());
	}

	@Test
	@Order(2)
	void testFindByIdFound() {
		var result = dao.findById(1);
		assertNotNull(result);

		assertEquals(message.title(), result.title());
		assertEquals(message.message(), result.message());
		assertNotNull(result.postAt());
	}

	@Test
	@Order(3)
	void testFindByIdNotnd() {
		var result = dao.findById(2);
		assertNull(result);
	}

	@Test
	@Order(4)
	void testUpdateFound() {
		var title = "New Title";
		var message = "New Message";

		var count = dao.update(1, title, message);
		assertEquals(1, count);

		var result = dao.findById(1);
		assertEquals(title, result.title());
		assertEquals(message, result.message());
		assertNotNull(result.postAt());
	}

	@Test
	@Order(5)
	void testUpdateNotFound() {
		var title = "New Title";
		var message = "New Message";

		var count = dao.update(2, title, message);
		assertEquals(0, count);

	}

	@Test
	@Order(6)
	void testDeleteFound() {
		int count = dao.deleteById(1);
		assertEquals(1, count);
	}

	@Test
	@Order(7)
	void testDeleteNotFound() {
		int count = dao.deleteById(1);
		assertEquals(0, count);
	}

	@Test
	@Order(8)
	void testCreateMessages() {
		var messages = List.of(
				new Message("Message 1", "Message 1 message"),
				new Message("Message 2", "Message 2 message"),
				new Message("Message 3", "Message 3 message"));

		var list = dao.createMessages(messages);
		assertEquals(2, list.get(0).id());
		assertEquals(3, list.get(1).id());
		assertEquals(4, list.get(2).id());
	}

	@Test
	@Order(9)
	void testCreateMessagesEmpty() {
		var messages = new ArrayList<Message>();
		var list = dao.createMessages(messages);
		assertTrue(list.isEmpty());
	}

	@Test
	@Order(10)
	void testCreateMessagesNull() {
		var list = dao.createMessages(null);
		assertTrue(list.isEmpty());
	}
}
