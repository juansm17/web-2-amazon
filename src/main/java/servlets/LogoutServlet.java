package servlets;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import models.Response;

@WebServlet(urlPatterns = "/logout", name = "Logout Servlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getSession().invalidate();
		Response<?> response = new Response<>();
		ObjectMapper mapper = new ObjectMapper();
		response.setStatus(200);
		response.setMessage("Sesion finalizada");
		res.getWriter().print(mapper.writeValueAsString(response));
	}
}