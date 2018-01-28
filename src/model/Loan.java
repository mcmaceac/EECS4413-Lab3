package model;

public class Loan {
	public static final String startPage = "/UI.jspx";
    public static final String resultsPage = "/Result.jspx";
    
    private static final String MONTHLYPAY = "monthlyPayments";
    private static final String GRACEINTEREST = "graceInterest";
	
	public Loan() {
		
	}
	
	public double computePayment(String p, String a, String i, 
			String g, String gp, String fi) throws Exception {
		
		double principal = Double.parseDouble(p);
		double interest = Double.parseDouble(i);
		double period = Double.parseDouble(a);
		boolean graceEnabled = Boolean.parseBoolean(g);
		double gracePeriod = Double.parseDouble(gp);
		double fixedInterest = Double.parseDouble(fi);
		
		double monthlyInt = computeMonthlyInterest(interest, fixedInterest);
		double monthlyPayments = monthlyInt * principal / (1 - Math.pow(1+monthlyInt, -period));
		
		double graceInterest = 0.0;
		if (graceEnabled) {
			graceInterest = computeGraceInterest(p, gp, i, fi);
			monthlyPayments += (graceInterest / gracePeriod);
		}
		
		return monthlyPayments;
	}
	
	public double computeGraceInterest(String p, String gp, String i, 
			String fi) throws Exception {
		
		double principal = Double.parseDouble(p);
		double interest = Double.parseDouble(i);
		double gracePeriod = Double.parseDouble(gp);
		double fixedInterest = Double.parseDouble(fi);
		
		double graceInterest = principal * computeMonthlyInterest(interest, fixedInterest) * gracePeriod;
		
		return graceInterest;
	}
	
	//computes the monthly interest rate based on fixed and regular interest rates
	private double computeMonthlyInterest(double i, double fi) {
		return ((fi + i) / 12.0) * .01;
	}
	
}
