<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>야보자</title>

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
</head>

<body>


	<!-- Navigation -->
	<%@ include file="inc/topbar.jsp"%>

	<!-- Page Header -->
	<!-- Set your background image for this header on the line below. -->
	<header class="intro-header"
		style="background-image: url('img/home-bg.jpg')">
		<div class="container">
			<div class="row">
				<div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
					<div class="site-heading">
						<h1>Movie Matching</h1>
						<hr class="small">
						<span class="subheading">Matching with YABOJA </span>
					</div>
				</div>
			</div>
		</div>
	</header>

	<!-- 관리자 / 사용자,탈퇴회원들 로 매뉴바 구성 -->
	<!-- Menu Bar -->

	<c:choose>
		<c:when test="${dto.usergrade eq 'admin'}">
			<nav class="nav2" style="margin-top: 60px; margin-bottom: 20px;">
			<!-- 메뉴바 -->
			<a href="movieBoard.do"><strong>Movie Board</strong></a> 
			<a href="matchingboardlist.do"><strong>Matching Board</strong></a> 
			<a href="reviewboard.do"><strong>Review Board</strong></a> 
			<a href="qnaboard.do"><strong>Q&A Board</strong></a> 
			<a href="adminPreferences.do"><strong>AdminManagement</strong></a>
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
	
	

	<!-- Movie List -->
	
	<%@ include file="inc/movielist.jsp"%>
	<!-- Slider -->
	<%@ include file="inc/slide.jsp"%>

<br><br>
	<!-- Main Content -->
	<div class="container">

<div class="container mt-5 mb-5">
	<div class="row">
		<div class="col-md-6 offset-md-3" style="margin-left: 25%;">
			<h4>What is Yaboja?</h4>
			<ul class="timeline">
				<li>
					<a target="_blank" href="https://www.totoprayogo.com/#">실시간 영화 커뮤니티 야! 보자!</a>
					<a href="#" class="float-right">21 March, 2022</a>
					<p>현재 상영작/개봉예정작/종영작 정보 확인, 혼자 영화 보지 마세요! 같이 봐요 야보자! 내 주변 영화관을 3개까지 등록할 수 있어요!</p>
				</li>
				<li>
					<a href="#">코인을 결제(카카오 결제)하시면 매칭을 등록할 수 있어요!</a>
					<a href="#" class="float-right">4 March, 2022</a>
					<p>영화와 영화관을 고르고 매칭이 성사되면 상대방과 채팅으로 약속을 잡을 수 있어요!</p>
				</li>
				<li>
					<a href="#">다양한 영화들의 후기를 남길 수 있어요!</a>
					<a href="#" class="float-right">1 April, 2022</a>
					<p>개개인의 영화 후기를 보고 댓글을 남길 수 있어요 ! </p>
				</li>
			</ul>
		</div>
	</div>
</div>

<!-- <div class="text-muted mt-5 mb-5 text-center small">by : <a class="text-muted" target="_blank" href="http://totoprayogo.com">totoprayogo.com</a></div> -->

	</div>

	<hr>

	<!-- Footer -->
	<%@ include file="inc/footer.jsp"%>

	<!-- jQuery -->
	<script src="vendor/jquery/jquery.min.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="vendor/bootstrap/js/bootstrap.min.js"></script>

	<!-- Contact Form JavaScript -->
	<script src="js/jqBootstrapValidation.js"></script>
	<script src="js/contact_me.js"></script>

	<!-- Theme JavaScript -->
	<script src="js/clean-blog.min.js"></script>

</body>

</html>

