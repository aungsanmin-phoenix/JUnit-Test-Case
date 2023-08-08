package com.jdc.statements.dao;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jdc.statements.ConnectionManager;
import com.jdc.statements.dto.Message;

public class MessageDao {
	private ConnectionManager manager;

	public MessageDao(ConnectionManager manager) {
		super();
		this.manager = manager;
	}
	
	public List<Message> createMessages(List<Message> messages) {
		List<Message> list = new ArrayList<Message>();
		
		if(null == messages) {
			return list;
		}
		
		var sql = "insert into message(title,message)values(?,?)";
		try(var conn = manager.getConnection();
				var stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
				
				for(var m:messages) {
					stmt.setString(1, m.title());
					stmt.setString(2, m.message());
					stmt.addBatch();
				}
				
				stmt.executeBatch();
				var keys = stmt.getGeneratedKeys();
				var index = 0;
				
				while(keys.next()) {
					System.out.println();
					list.add(messages.get(index).cloneWithId(keys.getInt(1)));
					index++;
				}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public Message createMessage(Message data) {
		var sql ="insert into message(title,message)values(?,?)";
		
		try(var conn = manager.getConnection()){
			var stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, data.title());
			stmt.setString(2, data.message());
			
			stmt.executeUpdate();
			var result = stmt.getGeneratedKeys();
			
			if(result.next()) {
				var id = result.getInt(1);
				return data.cloneWithId(id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Message findById(int id) {
		var sql = "select * from message where id= ?";
		try(var conn = manager.getConnection();
				var stmt = conn.prepareStatement(sql)){
			
			stmt.setInt(1, id);
			
			var resultSet = stmt.executeQuery();
			if(resultSet.next()) {
				return new Message(
						resultSet.getInt("id"),
						resultSet.getString("title"),
						resultSet.getString("message"),
						resultSet.getTimestamp("post_at").toLocalDateTime());
						
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int update(int id,String title,String message) {
		var sql = "update message set title = ?, message = ? where id=?";
				
		try(var conn = manager.getConnection();
				var stmt = conn.prepareStatement(sql)){
				stmt.setString(1, title);
				stmt.setString(2, message);
				stmt.setInt(3, id);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int deleteById(int id) {
		var sql = "delete from message where id=?";
		try(var conn = manager.getConnection();
				var stmt = conn.prepareStatement(sql)){
			
				stmt.setInt(1, id);
				
			return stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
