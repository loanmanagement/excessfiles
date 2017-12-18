package com.lms.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.lms.entity.CustomerDetails;
import com.lms.entity.LoanApplication;
import com.lms.entity.LoanProgramsOffered;
import com.lms.exception.LmsException;
import com.lms.service.AdminServiceImpl;
import com.lms.service.CustomerServiceImpl;
import com.lms.service.IAdminService;
import com.lms.service.ICustomerService;

public class CustomerMain 
{
	public static void main(String[] args) 
	{
		LoanApplication loanApplication = new LoanApplication();
		ICustomerService customerService= new CustomerServiceImpl();
		CustomerDetails customer = new CustomerDetails();
		IAdminService adminService = new AdminServiceImpl();
		@SuppressWarnings("resource")
		Scanner sc= new Scanner(System.in);
		int choice = 0;
		
		do
		{
			System.out.println("-------------------------------------------------------------------------");
			System.out.println("                             Welcome Customer     ");
			System.out.println("-------------------------------------------------------------------------");
			
			System.out.println("1. Register as a New Customer");
			System.out.println("2. View All Loan Programs.");
			System.out.println("3. Apply for a Loan Program.");
			System.out.println("4. Check Loan Application Status.");
			System.out.println("0. Exit");
			System.out.println("Enter a Choice");
			
			choice= sc.nextInt();
		
			switch(choice)
			{
			case 0:System.out.println("Application has been successfully Terminated.");	
					break;
			
			case 1:System.out.println("-------------------------------------------------------------------------");
					System.out.println("                             Register Customer    ");
					System.out.println("-------------------------------------------------------------------------");
					System.out.println("Enter Customer Name");
					customer.setApplicantName(sc.next());
					System.out.println("Enter Date of Birth in format (dd/MM/yyyy) ");
					Date dob=null;
					try 
					{
						dob = new SimpleDateFormat("dd/MM/yyyy").parse(sc.next());
					}
					catch (ParseException e1) 
					{
						System.out.println("Unable to Parse Date");
					}
					
					customer.setDob(dob);
					System.out.println("Enter number of Dependents");
					customer.setCountOfDependents(sc.nextInt());
					System.out.println("Enter email ID");
					customer.setEmailId(sc.next());
					System.out.println("Enter Marital Status");
					customer.setMaritalStatus(sc.next());
					System.out.println("Enter Personal Mobile Number (Eg: 9988776655)");
					customer.setMobileNumber(sc.nextLong());
					System.out.println("Enter Landline Number (Eg: 02511234567)");
					customer.setPhoneNumber(sc.nextLong());
					
					try 
					{
						customerService.registerCustomer(customer);
						
					}
					catch (LmsException e1) 
					{
						System.out.println("Unable to register Customer:" +e1.getMessage());
					}
					break;
				
				
			case 2:System.out.println("-------------------------------------------------------------------------");
					System.out.println("                           List of Customers     ");
					System.out.println("-------------------------------------------------------------------------");
					System.out.println("Program Name | Program Description  | Time Duration(Years) | Minimum Amount | Maximum Amount | ROI | Proofs Required ");
				try 
				{
					List<LoanProgramsOffered> list = adminService.viewAll();
					if(list.isEmpty())
					{
						System.out.println("No Loan Programs Found");
					}
					else
					{
						for (LoanProgramsOffered loan : list)
						{
							System.out.printf("%5s %25s %15d %20d %15d %12d %15s \n",loan.getProgramName(),loan.getDescription(),loan.getDurationInYears(),loan.getMinLoanAmount(),loan.getMaxLoanAmount(),loan.getRateOfInterest(),loan.getProofsRequired());
							
						}
						
					} 
				}
				catch (LmsException e) 
				{
					System.out.println("Unable to retrieve Program List: "+e.getMessage());
				}
				break;
				
			
			case 3: System.out.println("------------------------------------------------------------------------");
					System.out.println("                       Loan Application     ");
					System.out.println("------------------------------------------------------------------------");
					
					LocalDate appDate = LocalDate.now();
					Date date = java.sql.Date.valueOf(appDate);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					date = cal.getTime();
					loanApplication.setApplicationDate(date);
					System.out.println("Enter Loan Program");
					loanApplication.setLoanProgram(sc.next());
					System.out.println("Enter Loan Amount");
					loanApplication.setAmountOfLoan(sc.nextLong());
					System.out.println("Enter Address of Property");
					loanApplication.setAddressOfProperty(sc.next());
					System.out.println("Enter Family Income");
					loanApplication.setAnnualFamilyIncome(sc.nextInt());
					System.out.println("Enter Proof Name");
					loanApplication.setDocumentProofsAvailable(sc.next());
					System.out.println("Enter Guarantee Cover");
					loanApplication.setGuranteeCover(sc.next());
					System.out.println("Enter Market Value of Guarantee Cover");
					loanApplication.setMarketValueOfGuranteeCover(sc.nextInt());
					loanApplication.setStatus("Unapproved");
					loanApplication.setDateOfInterview(null);
					customer.setApplicationId(loanApplication);
					try
					{
						int id = customerService.applyLoan(loanApplication);
						System.out.println("Please note youd ID for future Reference :"+id);
						
					}
					catch (LmsException e) 
					{
						System.out.println("Error in Registration: "+e.getMessage());
					}
					break;
					
			case 4:System.out.println("------------------------------------------------------------------------");
					System.out.println("                       Check Application Status     ");
					System.out.println("------------------------------------------------------------------------");
					System.out.println("Enter Application ID");
					int applicationID = sc.nextInt();
					try 
					{
						LoanApplication resultApplication = customerService.viewApplicationStatus(applicationID);
						if(resultApplication==null)
						{
							System.out.println("No Application Found with given ID.\nPlease try Again.");
						}
						else
						{
							System.out.println("Details of Loan Application");
							System.out.println("Application ID: "+resultApplication.getApplicationId());
							System.out.println("Application Date: "+resultApplication.getApplicationDate());
							System.out.println("Loan Program: "+resultApplication.getLoanProgram());
							System.out.println("Loan Amount: "+resultApplication.getAmountOfLoan());
							System.out.println("Property Address: "+resultApplication.getAddressOfProperty());
							System.out.println("Family Income: "+resultApplication.getAnnualFamilyIncome());
							System.out.println("Proof Document: "+resultApplication.getDocumentProofsAvailable());
							System.out.println("Guarantee Cover: "+resultApplication.getGuranteeCover());
							System.out.println("Market Value of Guarantee Cover: "+resultApplication.getMarketValueOfGuranteeCover());
							System.out.println("------------------------------------------------------------------------");
							System.out.println("Status: "+resultApplication.getStatus());
							if (resultApplication.getDateOfInterview()!=null)
							{
								System.out.println("Date of Interview: "+resultApplication.getDateOfInterview());
							}
							else
							{
								System.out.println("Your application is under review and hasn't been assigned a Date of Interview.");
							}
						}
					}
					catch (LmsException e)
					{
						System.out.println("Unable to Get Details: "+e.getMessage());
					}
					break;
			
			
			default: System.out.println("Enter a valid choice");	
					break;
			
			
			}
		}while(choice!=0);
	}
}
