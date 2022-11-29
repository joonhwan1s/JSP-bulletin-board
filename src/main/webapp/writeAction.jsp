	<%@ page language="java" contentType="text/html; charset=UTF-8"
    	pageEncoding="UTF-8"%>
    <%@ page import="bbs.BbsDAO" %>
    <%@ page import="java.io.PrintWriter" %>
    <% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page" />
<jsp:setProperty name="bbs" property="bbsTitle" />
<jsp:setProperty name="bbs" property="bbsContent" />


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%
		String userID = null; //로그인이 되지 않은 유저는 회원가입 페이지에 들어갈 수 없도록 설정
		if(session.getAttribute("userID") != null){//userID로 세션이 존재하는 회원들은 
			userID = (String) session.getAttribute("userID");//String로 바꿔 할당된 변수가 정상적으로 userID가 자신에게 할당된 세션값을 넣어줄 수 있도록 설정
		}//userID에 해당 세션의 값을 넣어준다.
		if(userID == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("history.back()");
			script.println("</script>");
		} else{
			if(bbs.getBbsTitle() == null || bbs.getBbsContent() == null){
					PrintWriter script = response.getWriter();//하나라도 null값이 들어갔을때 실행
					script.println("<script>");
					script.println("alert('입력이 안 된 사항이 있습니다.')");
					script.println("history.back()");
					script.println("</script>");
				}else{
					BbsDAO bbsDAO = new BbsDAO();
					int result = bbsDAO.write(bbs.getBbsTitle(), userID, bbs.getBbsContent());
					if(result == -1){//DB로 보내도 동일한 ID가 있다면 실행
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("alert('글쓰기에 실패하였습니다.')");
						script.println("history.back()");
						script.println("</script>");
					}else {//정상입력시 입력한 데이터가 DB로 이동
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("location.href = 'bbs.jsp'");
						script.println("</script>");
					}
		}
		
			
		}
	%>
	
</body>
</html>