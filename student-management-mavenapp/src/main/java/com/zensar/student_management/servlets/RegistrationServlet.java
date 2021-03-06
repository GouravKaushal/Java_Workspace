package com.zensar.student_management.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.SimpleDateFormat;  
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//JDBC Related Attributes
	private String driver = null;
	private String url = null;
	private String username = null;
	private String password = null;
	private Connection connection = null;
	private ServletContext servletContext=null;
	
	//Registration Form Related Attributes
	private String firstName=null;
	private String lastName=null;
	private String email=null;
	private String contact =null;
	private Date date;
	private String gender=null;
	private String[] hobbiesArray=null;
	private String[] technologiesArray=null;
	private String address=null;
	private String registerPassword=null;
	private String registerCnfPassword=null;
	PrintWriter out=null;
	
	
	//DB Insert Related Attributes
	private PreparedStatement psmt=null,psmt2=null,psmt3=null;
	private ResultSet rs=null;
	private int count =0;

	

	@Override
	public void init(ServletConfig config) throws ServletException {
		servletContext = config.getServletContext();

		driver = servletContext.getInitParameter("driver");
		url = servletContext.getInitParameter("url");
		username = servletContext.getInitParameter("username");
		password = servletContext.getInitParameter("password");

		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connected");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.sendRedirect("registration.html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		firstName=request.getParameter("textFirstName");
		lastName=request.getParameter("textLastName");
		email=request.getParameter("email");
		contact=request.getParameter("mobile");
		String tempDate=request.getParameter("dob");
		gender=request.getParameter("gender");
		hobbiesArray=request.getParameterValues("hobbies");
		technologiesArray=request.getParameterValues("technologies");
		address=request.getParameter("address");
		registerPassword=request.getParameter("registerPassword");
		registerCnfPassword=request.getParameter("registerCnfPassword");
		
		if(!registerPassword.equals(registerCnfPassword))
		{
			
		
			out=response.getWriter();
		    out.println("<script type=\"text/javascript\">");
		                out.println("alert('Pass is not Same');");
		                out.println("location='registration.html';");
		                out.println("</script>");

		}
		
		else {
				
		try {
			psmt=connection.prepareStatement("insert into studentdetails (firstname,lastname,email,contact,dob,gender,address,password) values (?,?,?,?,?,?,?,?);");
			psmt.setString(1, firstName);
			psmt.setString(2, lastName);
			psmt.setString(3, email);
			psmt.setString(4, contact);
			psmt.setString(5, tempDate);
			psmt.setString(6, gender);
			psmt.setString(7, address);
			psmt.setString(8, registerPassword);
		
		    count = psmt.executeUpdate();
		    
		    
			psmt=connection.prepareStatement("select id from studentdetails where email=?");
			psmt.setString(1, email);
			int id=0;
			rs=psmt.executeQuery();
			if(rs.next())
			{
				id=rs.getInt(1);//2
				
				psmt2=connection.prepareStatement("insert into hobbies (hobby,student_id) values (?,?);");
				for(int i=0;i<hobbiesArray.length;i++)
				{
				
					psmt2.setString(1, hobbiesArray[i]);
					psmt2.setInt(2, id);
					count=psmt2.executeUpdate();
				}
				count=0;
				psmt3=connection.prepareStatement("insert into technologies (technology,student_id) values (?,?);");
				for(int i=0;i<technologiesArray.length;i++)
				{
				
					psmt3.setString(1, technologiesArray[i]);
					psmt3.setInt(2, id);
					count=psmt3.executeUpdate();
				}
				if(count!=0)
				{
					response.sendRedirect("success.html");
				}
				else
				{
					response.sendRedirect("error.html");
				}
			}

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	}

}




