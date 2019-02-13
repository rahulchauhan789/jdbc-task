import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Question {

	public static void main(String[] args) throws SQLException, IOException {

		Scanner sc = new Scanner(System.in);
		System.out.println("Please Enter Choise");
		System.out.println("1.Login as Admin");
		System.out.println("2.Login as Student");
		int choice = sc.nextInt();
		Task t = new Task();

		switch (choice) {
		case 1:
			t.adminLogin();
			break;

		case 2:
			t.studentLogin();
			break;

		default:
			System.out.println("You Have Invalid Choice");
			break;
		}
	}
}

class Task {

	PreparedStatement ps = null;
	static Connection con;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbcQuestionTask", "root", "");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	Scanner sc = new Scanner(System.in);

	void adminLogin() throws SQLException, IOException {
		System.out.println("Please Enter UserName");
		String username = sc.nextLine();
		System.out.println("Please Enter Password");
		String upass = sc.nextLine();

		String query1 = "select *from admin where username=? and password=?";
		ps = con.prepareStatement(query1);

		ps.setString(1, username);
		ps.setString(2, upass);

		ResultSet rs = ps.executeQuery();

		boolean fnd = rs.next();

		if (fnd) {
			setquestion();
		} else {
			System.out.println("username and paasword does not match");
		}
	}

	void studentLogin() throws SQLException {

		System.out.println("Please Enter UserName");
		String username = sc.nextLine();
		System.out.println("Please Enter Password");
		String upass = sc.nextLine();

		String query = "Select *from Student where username=? and password=?";
		ps = con.prepareStatement(query);
		ps.setString(1, username);
		ps.setString(2, upass);
		ResultSet rs = ps.executeQuery();

		boolean fnd = rs.next();

		if (fnd) {
			getAnswer();
		}
	}

	void setquestion() throws SQLException, IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("How Many Question You Want To Submit....Please Enter");
		int n = sc.nextInt();

		for (int i = 1; i <= n; i++) {
			System.out.println("Please Enter Question");
			String que = br.readLine();
			System.out.println("Please Enter Option A");
			String a = br.readLine();
			System.out.println("Please Enter Option B");
			String b = br.readLine();
			System.out.println("Please Enter Option C");
			String c = br.readLine();
			System.out.println("Please Enter Option D");
			String d = br.readLine();
			System.out.println("Please Enter Answer");
			String ans = br.readLine();

			String query = "insert into question values(?,?,?,?,?,?,?)";
			ps = con.prepareStatement(query);
			ps.setInt(1, 0);
			ps.setString(2, que);
			ps.setString(3, a);
			ps.setString(4, b);
			ps.setString(5, c);
			ps.setString(6, d);
			ps.setString(7, ans);

			ps.executeUpdate();
			System.out.println("Question " + i + " And Their Answer Submitted Successfully");
		}
	}

	void getAnswer() throws SQLException {

		System.out.println("Java Test");
		int rowcount = 0;
		int countnum = 0;
		int correctanswer = 0, wronganswer = 0;
			
		String q = "Select *from question";
		ps = con.prepareStatement(q);
		ResultSet rs = ps.executeQuery();
		
		ps = con.prepareStatement("select count(*) from question");
		ResultSet rs1 = ps.executeQuery();
		while (rs1.next()) {
			rowcount = rs1.getInt(1);
		}

		ArrayList<Integer> al = new ArrayList<>(rowcount);
		for (int i = 1; i <= rowcount; i++) {
			al.add(i);
		}
		Collections.shuffle(al);

		int x = 0;
		for (int i = 1; i <= rowcount; i++) {
			x = al.get(i);
			rs.absolute(x);
		
			while (rs.next()) {
				System.out.print(rs.getInt(1) + ". ");
				System.out.println(rs.getString(2) + "  ");
				System.out.print("a)" + rs.getString(3) + "  ");
				System.out.print("b)" + rs.getString(4) + "  ");
				System.out.print("c)" + rs.getString(5) + "  ");
				System.out.println("d)" + rs.getString(6) + "  ");

				System.out.print("Answer: ");
				String answer = sc.nextLine();
				String s = rs.getString(7);
				if (s.equals(answer)) {
					countnum++;
					correctanswer++;
				} else {
					wronganswer++;
				}
			}	
		}
		System.out.println("\nTotal Marks: " + rowcount);
		System.out.println("Total Correct Answer: " + correctanswer);
		System.out.println("Total Wrong Answer: " + wronganswer);
		System.out.println("Total Get Marks: " + countnum);
	}
}