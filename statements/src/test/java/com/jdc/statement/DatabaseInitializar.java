package com.jdc.statement;

import java.sql.SQLException;

import com.jdc.statements.ConnectionManager;

public class DatabaseInitializar {
	public static void truncate(String ...tables) {
		try(var connection = ConnectionManager.getInstance().getConnection()){
			for(var table :tables) {
				var stmt = connection.createStatement();
				stmt.execute("truncate table %s".formatted(table));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
