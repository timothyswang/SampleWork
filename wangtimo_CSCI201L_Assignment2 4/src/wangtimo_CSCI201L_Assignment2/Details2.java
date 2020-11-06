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
 * Servlet implementation class Details2
 */
@WebServlet("/Details2")
public class Details2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Details2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		JDBC_Driver connection = new JDBC_Driver();
		
		String BookISBN = request.getParameter("BookISBN");
		
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
			String bookResult = connection.BookResult(BookISBN, n);
        	PrintWriter out = response.getWriter();
        	System.out.println("bookResult: " + bookResult);
    		out.println(bookResult);
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
