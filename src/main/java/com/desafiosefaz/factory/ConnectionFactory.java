package com.desafiosefaz.factory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

	private static final String USERNAME = "root";

	private static final String PASSWORD = "";

	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/desafio_sefaz?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	public static Connection createConnectionToMySQL() throws Exception {
		Class.forName("com.mysql.jdbc.Driver"); // Faz com que a classe seja carregada pela JVM
	
		Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
	
		return connection;
	}

	public static void main(String[] args) throws Exception {

		Connection con = createConnectionToMySQL();

		if (con != null) {
			System.out.println("Conexão obtida com sucesso! " + con);
			con.close();
		}
	}
}
