package com.darpa.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.darpa.code.DarpaCode;

/**
 * Servlet implementation class GetServlet
 */
@WebServlet("/darpa")
public class Darpa extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Darpa() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		switch (request.getParameter("type")) {
		case "ip":
			String idIP = request.getParameter("id");
			response.getOutputStream().print(new DarpaCode().readIPFile(getServletContext(), idIP));
			break;
		case "predicted":
			String idAttack = request.getParameter("id");
			response.getOutputStream().print(new DarpaCode().readPredictedFile(getServletContext(), idAttack));
			break;
		case "attack":
			response.getOutputStream().print(new DarpaCode().readAttackFile(getServletContext()));
			break;
		}
			
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
