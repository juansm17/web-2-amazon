package controllers;

import models.*;
import helpers.PropertiesReader;
import helpers.PoolManager;

import java.sql.*;
import java.io.IOException;

/**
 * @author
 */

@SuppressWarnings("Duplicates")
public class UserController {
	private static PoolManager poolManager = PoolManager.getPoolManager();
	private static PropertiesReader prop = PropertiesReader.getInstance();

	public static Response<User> login(User user) {
		Connection con = poolManager.getConn();
		Response<User> response = new Response<>();
		String query = prop.getValue("login");
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, user.getUsername().toLowerCase());
			pstmt.setString(2, user.getPassword());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				getUserData(rs, user);
				response.setStatus(200);
				response.setMessage("Acceso concedido");
				response.setData(user);
			}
			else {
				response.setStatus(401);
				response.setMessage("Credenciales no validas, intente de nuevo");
			}
		} catch (SQLException e) {
			response.setStatus(500);
			response.setMessage("Error en DB");
			e.printStackTrace();
		}
		poolManager.returnConn(con);
		return response;
	}

	public static Response<User> register(User user) throws IOException {
		Connection con = poolManager.getConn();
		Response<User> response = new Response<>();
		String query = prop.getValue("registerUser");
		if(checkLowercaseUsername(user.getUsername().toLowerCase())) {
			response.setStatus(409);
			response.setMessage("Nombre de usuario en uso");
			poolManager.returnConn(con);
			return response;
		}
		if(checkEmail(user.getEmail().toLowerCase())) {
			response.setStatus(409);
			response.setMessage("Correo en uso");
			poolManager.returnConn(con);
			return response;
		}
		try {
			PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getLastName());
			pstmt.setString(5, user.getEmail().toLowerCase());
			pstmt.execute();
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next();
			user.setId(rs.getInt(1));
			response.setStatus(200);
			response.setMessage("Usuario registrado de manera exitosa");
			user.setPassword(null);
			response.setData(user);
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setMessage("Error en DB");
		}
		poolManager.returnConn(con);
		return response;
	}

	private static void getUserData(ResultSet rs, User user) throws SQLException {
	    user.setId(rs.getInt(1));
		user.setUsername(rs.getString(2));
		user.setName(rs.getString(4));
		user.setLastName(rs.getString(5));
		user.setEmail(rs.getString(6));
		user.setPassword(null);
	}

	public static boolean checkEmail(String email){
		Connection con = poolManager.getConn();
		String query = prop.getValue("checkEmail");
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, email.toLowerCase());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				poolManager.returnConn(con);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			poolManager.returnConn(con);
			return true;
		}
		finally {
			poolManager.returnConn(con);
		}
		return false;
	}

	public static boolean checkLowercaseUsername(String username) {
		Connection con = poolManager.getConn();
		String query = prop.getValue("checkLowercaseUsername");
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				poolManager.returnConn(con);
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			poolManager.returnConn(con);
			return true;
		}
		finally {
			poolManager.returnConn(con);
		}
		return false;
	}

	public static Response<User> modifyUser(User user) {
		Connection con = poolManager.getConn();
		Response<User> response = new Response<>();
		String query = prop.getValue("updateUser");
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, user.getName());
			ps.setString(2, user.getUsername());
			ps.setString(3, user.getLastName());
			ps.setString(4, user.getEmail());
			ps.setInt(5, user.getId());
			ps.setString(6, user.getPassword());
			int affectedRows = ps.executeUpdate();
			if(affectedRows == 1) {
				response.setStatus(200);
				response.setMessage("Usuario actualizado de manera exitosa");
				response.setData(user);
			} else {
				response.setStatus(401);
				response.setMessage("Credenciales invalidas");
				response.setData(null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setMessage("Error DB");
			response.setData(user);
		}
		poolManager.returnConn(con);
		return response;
	}
}
