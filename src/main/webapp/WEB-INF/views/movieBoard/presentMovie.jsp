<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>야보자</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Bootstrap Core CSS -->
<link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

 <!-- Theme CSS -->
<!-- <link href="css/clean-blog.min.css" rel="stylesheet"> -->

<!-- Custom Fonts -->
<link href="vendor/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">
<link
	href='https://fonts.googleapis.com/css?family=Lora:400,700,400italic,700italic'
	rel='stylesheet' type='text/css'>
<link
	href='https://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800'
	rel='stylesheet' type='text/css'>



<!-- Menu Bar -->
<link href="css/menubar.css" rel="stylesheet">
<!-- Content -->
<link href="css/index_content.css" rel="stylesheet">
<!-- jQuery -->
	<script src="vendor/jquery/jquery.min.js"></script>
	
	

	
	




</head>

<body>


	<!-- Navigation -->
	<%@ include file="../inc/topbar.jsp"%>


	<!-- Menu Bar -->
	<c:choose>
		<c:when test="${dto.usergrade eq 'admin'}">
			<nav class="nav2" style="margin-top: 60px; margin-bottom: 20px;">
			<!-- 메뉴바 -->
			<a href="movieBoard.do"><strong>Movie Board</strong></a> 
			<a href="matchingboardlist.do"><strong>Matching Board</strong></a> 
			<a href="reviewBoard.do"><strong>Review Board</strong></a> 
			<a href="qnaboard.do"><strong>Q&A Board</strong></a> 
			<a href="adminPreferences.do"><strong>preferences</strong></a>
			<div class="nav-underline"></div>
			</nav>		
		</c:when>
		<c:otherwise>
			<nav class="nav2" style="margin-top: 60px; margin-bottom: 20px;">
			<!-- 메뉴바 -->
			<a href="movieBoard.do"><strong>Movie Board</strong></a> 
			<a href="matchingboardlist.do"><strong>Matching Board</strong></a> 
			<a href="reviewboard.do"><strong>Review Board</strong></a> 
			<a href="qnaboard.do"><strong>Q&A Board</strong></a> 
			<a href="mypage.do"><strong>My Page</strong></a>
			<div class="nav-underline"></div>
			</nav>		
		</c:otherwise>
	</c:choose>
	
	<!-- moviesidebar -->
	<%@ include file="../inc/moviesidebar2.jsp"%>
	
	
	<!-- 영화매칭관련 페이지들 소스는 여기부터 작성!! -->
	
	<!-- Movie List -->
	<br/>
	<br/>
	<h3 class="text-center">현재상영작</h3>
	<p style="padding-left:200px"> * 이 페이지는 데이터베이스 영화 테이블에 '상영작'인 조건으로  불러오는 데이터입니다. 돋보기나 영화 제목을 클릭하면 영화 상세 정보를 볼 수 있습니다.</p>
	<p style="padding-left:200px"> 이 때 출연 배우나 상세 데이터는 실시간으로 크롤링한 데이터입니다. '예매하기' 버튼을 누르면 네이버 예매페이지로 링크를 걸어두었습니다. </p>
	<p style="padding-left:200px"> '매칭하러가기' 버튼을 누르면 해당 영화에 대한 매칭 게시글만 검색되는 페이지로 넘겨줍니다. </p>
	<%@ include file="../inc/movielist.jsp"%>	

	<!-- Footer -->
	<%@ include file="../inc/footer.jsp"%>

	

</body>

</html>
