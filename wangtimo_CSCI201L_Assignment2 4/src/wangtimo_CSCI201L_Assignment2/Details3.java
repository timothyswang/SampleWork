package wangtimo_CSCI201L_Assignment2;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Details3
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/Details3" })
public class Details3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Details3() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		JDBC_Driver connection = new JDBC_Driver();
		
		String BookCover = request.getParameter("BookCover");
		String Title = request.getParameter("Title");
		String Author = request.getParameter("Author");
		String Summary = request.getParameter("Summary");
		String Url = request.getParameter("Url");
		String BookISBN = request.getParameter("BookISBN");
		
		System.out.println("Url2: " + Url);
		System.out.println("BookISBN2: " + BookISBN);
		
		/*
		HttpSession session = request.getSession(false);
		String n=(String)session.getAttribute("username");
		if (n == null || n.contentEquals("")) {
			//There is no username.
			System.out.println("There is not a username");
			PrintWriter out = response.getWriter();
			String errormessage = "asdfadfasasdasafas";
			System.out.println(errormessage);
    		out.println(errormessage);
        }
        */
		//else {
    		//There is a username.
		
		
		System.out.println("There is a username");
		HttpSession session = request.getSession(false);
		String n=(String)session.getAttribute("username");
		connection.AddFavoriteBook(BookCover, Title, Author, Summary, Url, BookISBN, n, LocalDateTime.now().toString());
		PrintWriter out = response.getWriter();
		out.println("");
		//}
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
