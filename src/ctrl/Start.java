package ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Start
 */
@WebServlet({"/Start", "/Startup", "/Startup/*"})
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public String startPage = "/UI.jspx";
    public String resultsPage = "/Result.jspx";
    
    private static final String MONTHLYPAY = "monthlyPayments";
    private static final String GRACEINTEREST = "graceInterest";
	
    /**
     * It is awkward to intermix html and java code because it is hard to see the
     * structure of the html when you are outputting it in java as well as violation
     * of the separation principal.
     */
    
    /**
     * Mixing validation, payment computation and presentation violates the separation
     * of concern principal since the calculation of values should be separated from
     * the validation of user input and presentation of input as seen in the MVC design
     * pattern. When mixing these three it not only creates uglier code but opens the 
     * code up to bugs that can often be hard to fix when the code is not modularized.
     */
    
    /**
     * The content-length measures the size of the data in the body of the request/response
     */
    
    /**
     * GET should be used when only information from the server needs to be retrieved
     * with no side effects while POST should be used when information needs to be 
     * preserved and side effects are present.
     */
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Start() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//submit was not pressed, so we forward to the start page
		if (request.getParameter("submit") == null) {
			if (request.getParameter("restart") == null) {
				request.getRequestDispatcher(startPage).forward(request, response);
			}
			else { //restart has been pressed, return to start page with variables blank
				response.sendRedirect(this.getServletContext().getContextPath() + "/Start");
			}
		}
		else {
			double principal, period, interest, fixedInterest, graceInterest, gracePeriod;
			
			boolean gracePeriodEnabled = request.getParameter("gracePeriod") != null;
			
			principal = Double.parseDouble(getServletContext().getInitParameter("principal"));
			period = Double.parseDouble(getServletContext().getInitParameter("period"));
			interest = Double.parseDouble(getServletContext().getInitParameter("interest"));
			fixedInterest = Double.parseDouble(getServletContext().getInitParameter("fixedInterest"));
			gracePeriod = Double.parseDouble(getServletContext().getInitParameter("gracePeriod"));
			
			
			principal = (request.getParameter("principal").isEmpty()) ? principal : Double.parseDouble(request.getParameter("principal"));
			period = (request.getParameter("period").isEmpty()) ? period : Double.parseDouble(request.getParameter("period"));
			interest = (request.getParameter("interest").isEmpty()) ? interest : Double.parseDouble(request.getParameter("interest"));
			
			
			//Writer p = response.getWriter();
			
			double monthlyInt = ((fixedInterest + interest) / 12.0) * .01;
			double monthlyPayments = monthlyInt * principal / (1 - Math.pow(1+monthlyInt, -period));
			
			graceInterest = 0.0;	 //grace interest is 0 by default
			if (gracePeriodEnabled) {
				graceInterest = principal * monthlyInt * gracePeriod;
				monthlyPayments += (graceInterest / gracePeriod);
			}
			
			DecimalFormat d = new DecimalFormat("##.0");
			//System.out.println(graceInterest);
			request.setAttribute(GRACEINTEREST, graceInterest);
			request.setAttribute(MONTHLYPAY, d.format(monthlyPayments));
			//p.flush();
			//p.write("Monthly payments: " + d.format(monthlyPayments));
			
			
			request.getRequestDispatcher(resultsPage).forward(request, response);
		}
		//The below code is to generate an exception to test the exception page
		//int[] ex = {1, 2, 3};
		//int genException = ex[5];
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
