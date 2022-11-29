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
			pstmt = conn.prepareStatement(SQL);//������ ���� DB�� �����ϴ� �������� �ν��Ͻ� �����´�.
			pstmt.setString(1, userID);// ?�� �ش��ϴ� �������� USERID�� �������ش�.(id�� ������ �����ϴ��� Ȯ��. �����ϸ� DB���� ���������� ����)
			rs = pstmt.executeQuery(); // ����� ���� ��ü�� ������ ����� �־��ش�.
			if (rs.next()) {// ������ ����� �����Ѵٸ� �н����� ��ġ���� Ȯ�� �ƴϸ� -1�� ��ȯ
				if (rs.getString(1).equals(userPassword)) {// ������ ���̵��� �Ķ���Ϳ� ���� �н����尡 �������� Ȯ��(����� ���� userPW�� �޾Ƽ� ��������
															// Ȯ��)
					return 1; //�α��� ����
				}
				else {
					return 0;// ��й�ȣ ����ġ
				}
			}
			return -1; //���̵� ����.
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();// �ش� ���ܰ� ���� ���
		}
		return -2; //�����ͺ��̽� ����
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
		return -1;// DB����
	}

}
