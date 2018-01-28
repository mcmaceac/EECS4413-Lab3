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

import model.Loan;

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
    private static final String FIXED_INTEREST = "fixedInterest";
    private static final String GRACE_PERIOD = "gracePeriod";
    private static final String MODEL = "model";
    
    private Loan loan;
	
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
        try {
			init();
		} catch (ServletException e) {
			e.printStackTrace();
		}
    }
    
    public void init() throws ServletException {
    		loan = new Loan();
    		//getServletContext().setAttribute(MODEL, loan);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().setAttribute("legendName", getServletContext().getInitParameter("legendName"));
		
		//submit was not pressed, so we forward to the start page
		if (request.getParameter("submit") == null) {
			if (request.getParameter("restart") == null) {
				request.getRequestDispatcher(startPage).forward(request, response);
			}
			else { //restart has been pressed
				response.sendRedirect(this.getServletContext().getContextPath() + "/Start");
			}
		}
		else {
			String principal, period, interest, gracePeriodEnabled, fixedInterest, gracePeriod;
			Double monthlyPayments, graceInterest = 0.0;
			
			principal = request.getParameter("principal");
			period = request.getParameter("period");
			interest = request.getParameter("interest");
			gracePeriodEnabled = request.getParameter("gracePeriod");
			
			//System.out.println("gracePeriodEnabled = " + gracePeriodEnabled);
			
			fixedInterest = getServletContext().getInitParameter(FIXED_INTEREST);
			gracePeriod = getServletContext().getInitParameter(GRACE_PERIOD);
			
			try {
				monthlyPayments = loan.computePayment(principal, period, interest, gracePeriodEnabled, gracePeriod, fixedInterest);
				graceInterest = loan.computeGraceInterest(principal, gracePeriod, interest, fixedInterest, gracePeriodEnabled);
			
				DecimalFormat d = new DecimalFormat("##.0");
				//System.out.println(graceInterest);
				getServletContext().setAttribute(GRACEINTEREST, graceInterest);
				getServletContext().setAttribute(MONTHLYPAY, d.format(monthlyPayments));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			//save the session attributes in case the user presses reset
			request.getSession().setAttribute("principal", principal);
			request.getSession().setAttribute("interest", interest);
			request.getSession().setAttribute("period", period);
			request.getSession().setAttribute("graceEnabled", gracePeriodEnabled);
			
			
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
