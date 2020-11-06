package wangtimo_CSCI201L_Assignment2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class HomePage
 */
@WebServlet("/HomePage")
public class HomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomePage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String firstSession = request.getParameter("firstSession");
		
		if (firstSession.contentEquals("true")) {
			//First time using program. No user logged in.
			HttpSession session = request.getSession();
			session.setAttribute("username","");
			PrintWriter out = response.getWriter();
    		out.println("");
		}
		else {
			//Not first time using program. User has already been using program.
			HttpSession session = request.getSession(false);
			String n=(String)session.getAttribute("username");
			if (n == null || n.contentEquals("")) {
				//There is no username.
				System.out.println("There is not a username");
				PrintWriter out = response.getWriter();
	    		out.println("");
	        }
			else {
	    		//There is a username.
				System.out.println("There is a username");
	        	PrintWriter out = response.getWriter();
	    		out.println(n);
			}
		}
		
        
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
