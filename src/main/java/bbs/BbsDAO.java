package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
//			String dbURL = "jdbc:mysql://localhost/joonhwan1s?serverTimezone=UTC";
//	String dbID = "joonhwan1s";
//	String dbPassword = "wnsghks12!s";
	private Connection conn;
	private ResultSet rs;

	public BbsDAO() {// String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";
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

	public String getDate() {
		String SQL = "SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public int getNext() {
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int write(String bbsTitle, String userID, String bbsContent) {
		String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			// rs = pstmt.executeQuery(); INSERT 문구 같은 경우 excuteUpdate로 작동하기때문에 조회는 필요없다.
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public ArrayList<Bbs> getList(int pageNumber) {// 특정한 페이지에 따른 총 10개의 게시글를 가져올 수 있는 함수글
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10); // 물음표에 들어갈 내용, 다음으로 작성될 글의 번호 getNext(6) - 5 -> 결과적으로
																// 6이란 값이 들어감
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);// 해당 인스턴스를 리스트에 담아서 반환
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean nextPage(int pageNumber) {// 특정한 페이지가 존재하는지 물어보는 함수
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10); // 물음표에 들어갈 내용, 다음으로 작성될 글의 번호 getNext(6) - 5 -> 결과적으로
																// 6이란 값이 들어감
			rs = pstmt.executeQuery();
			if (rs.next()) {// 만약 결과가 하나라도 존재한다면 return true를 함으로 다음 페이지에 넘어 갈 수 있도록 한다.
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Bbs getBbs(int bbsID) {
		String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID); // 물음표에 들어갈 내용, 다음으로 작성될 글의 번호 getNext(6) - 5 -> 결과적으로
									// 6이란 값이 들어감
			rs = pstmt.executeQuery();
			if (rs.next()) {// 만약 결과가 하나라도 존재한다면 return true를 함으로 다음 페이지에 넘어 갈 수 있도록 한다.
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public int update(int bbsID, String bbsTitle, String bbsContent) {// 위 write 인스턴스와 비슷한 성격을 가지고 있다.
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ? ";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			// rs = pstmt.executeQuery(); INSERT 문구 같은 경우 excuteUpdate로 작동하기때문에 조회는 필요없다.
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int delete(int bbsID) {
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ? ";// 글을 삭제하더라도 글에 대한 정보가 남아있을 수 있도록 0으로 바꾼다.
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);// 성공적으로 ID값의 글 available을 0으로 바꿈으로써 삭제처리
			// rs = pstmt.executeQuery(); INSERT 문구 같은 경우 excuteUpdate로 작동하기때문에 조회는 필요없다.
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

}