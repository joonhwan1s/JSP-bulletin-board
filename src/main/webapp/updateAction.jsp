	<%@ page language="java" contentType="text/html; charset=UTF-8"
    	pageEncoding="UTF-8"%>
    <%@ page import="bbs.BbsDAO" %>
    <%@ page import="bbs.Bbs" %>
    <%@ page import="java.io.PrintWriter" %>
    <% request.setCharacterEncoding("UTF-8"); %>



<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%
		String userID = null; //로그인이 된 유저는 회원가입 페이지에 들어갈 수 없도록 설정
		if(session.getAttribute("userID") != null){//userID로 세션이 존재하는 회원들은 
			userID = (String) session.getAttribute("userID");//String로 바꿔 할당된 변수가 정상적으로 userID가 자신에게 할당된 세션값을 넣어줄 수 있도록 설정
		}//userID에 해당 세션의 값을 넣어준다.
		if(userID == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("history.back()");
			script.println("</script>");
		} 
		int bbsID = 0;//현재 수정하고자하는 글의 ID값 글의 번호가 들어오지 않았다면 유효하지 않은 글이라 출력
		if(request.getParameter("bbsID") != null){
			bbsID = Integer.parseInt(request.getParameter("bbsID"));
		}
		if(bbsID == 0){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>");
		}
		Bbs bbs = new BbsDAO().getBbs(bbsID);//현재 작성한 글이 작성한 사람 본인인지 확인(현재 넘어온 bbsID 값을 가지고 해당 글을 가져와서)
		if(!userID.equals(bbs.getUserID())){//실제로 이 글을 작성한 사람이 맞는지 확인하는 과정(세션의 값가 가져온 값이 일치하지 않는다면)
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('권한이 없습니다.')");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>");
		}else{//권한이 있는 사람이라면 실행 모든 내용이 다 성공적으로 입력되었는지 확인, javabeans을 더 이상 사용하지 않기 때문에 bbstitle 외 content로 넘어온 값들을 request를 통해 비교
			if(request.getParameter("bbsTitle") == null || request.getParameter("bbsContent") == null//bbstitle이란 이름으로 매개변수 넘어온 값들을 분석한다.(update페이지에서 bbstitle,content가 매개변수로 넘어온다.)
					|| request.getParameter("bbsTitle").equals("") || request.getParameter("bbsContent").equals("")){//글의 제목과 내용이 모두 빈값인지 혹은 빈칸이 하나라도 있는 경우 사용자가 다시 입력할 수 있도록 확인 
					PrintWriter script = response.getWriter();//하나라도 null값이 들어갔을때 실행
					script.println("<script>");
					script.println("alert('입력이 안 된 사항이 있습니다.')");
					script.println("history.back()");
					script.println("</script>");
				}else{
					BbsDAO bbsDAO = new BbsDAO();
					int result = bbsDAO.update(bbsID, request.getParameter("bbsTitle"), request.getParameter("bbsContent"));
					if(result == -1){//DB로 보내도 동일한 ID가 있다면 실행
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("alert('글 수정에 실패하였습니다.')");
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