package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public UserDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "root";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID=?";
		try {
			pstmt = conn.prepareStatement(SQL);//정해진 문장 DB에 삽입하는 형식으로 인스턴스 가져온다.
			pstmt.setString(1, userID);// ?에 해당하는 내용으로 USERID를 삽입해준다.(id가 실제로 존재하는지 확인. 존재하면 DB에서 가져오도록 셋팅)
			rs = pstmt.executeQuery(); // 결과를 받은 객체에 실행한 결과를 넣어준다.
			if (rs.next()) {// 실행한 결과가 존재한다면 패스워드 일치여부 확인 아니면 -1을 반환
				if (rs.getString(1).equals(userPassword)) {// 삽입한 아이디의 파라미터에 대한 패스워드가 동일한지 확인(결과로 나온 userPW를 받아서 동일한지
															// 확인)
					return 1; //로그인 성공
				}
				else {
					return 0;// 비밀번호 불일치
				}
			}
			return -1; //아이디가 없다.
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();// 해당 예외가 뭔지 출력
		}
		return -2; //데이터베이스 오류
	}

	public int join(User user) {
		String SQL = "INSERT INTO USER VALUES (?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());

			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;// DB오류
	}

}
