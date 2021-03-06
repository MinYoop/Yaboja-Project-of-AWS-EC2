package com.yaboja.controller;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yaboja.biz.ChattingBiz;
import com.yaboja.biz.CinemaBiz;
import com.yaboja.biz.CoinBiz;
import com.yaboja.biz.MatchingBiz;
import com.yaboja.biz.MatchingboardBiz;
import com.yaboja.biz.MovieBiz;
import com.yaboja.biz.UserBiz;
import com.yaboja.dto.CinemaDto;
import com.yaboja.dto.Criteria;
import com.yaboja.dto.MatchingDto;
import com.yaboja.dto.MatchingboardDto;
import com.yaboja.dto.MovieDto;
import com.yaboja.dto.PageMaker;
import com.yaboja.dto.UserDto;

@Controller
public class MatchingboardController {

	@Autowired
	private MatchingboardBiz matchingboardBiz;
	@Autowired
	private UserBiz userBiz;
	@Autowired
	private MovieBiz movieBiz;
	@Autowired
	private CinemaBiz cinemaBiz;
	@Autowired
	private MatchingBiz matchingBiz;
	@Autowired
	private CoinBiz coinBiz;
	@Autowired
	private ChattingBiz chattingBiz;

	private static final Logger logger = LoggerFactory.getLogger(MatchingboardController.class);

	@RequestMapping(value = "/matchingboardlist.do", method = RequestMethod.GET)
	public String list(Criteria cri, Model model, HttpSession session) {
//		UserDto dto = (UserDto)session.getAttribute("dto"); //userdto???   ????????? ????????????
		// 1.List<MatchingboardDto> list = matchingboardBiz.selectAll();// selectAll()???
		// ?????? matchingdto list??? ??????
		logger.info("?????????????????? 1 ?????? ?????? controller");
		System.out.println("////" + cri);

		//////
		UserDto userdto = (UserDto) session.getAttribute("dto");

		int coin_charge = 0;
		int coin_use = 0;
		int coin_use1 = 0;
		int coin_val = 0;

		if(userdto == null) {
			
		}else {
		coin_charge = coinBiz.coin(userdto.getUserseq(), "??????");

		coin_use = coinBiz.coin(userdto.getUserseq(), "?????? ????????? ??????");
		coin_use1 = coinBiz.coin(userdto.getUserseq(), "??????????????? ?????? ??????");

		coin_val = ((coin_charge - (coin_use+coin_use1)) / 500);

		model.addAttribute("user_name", userdto.getUsername());
		model.addAttribute("coin", coin_val);
		}
		////

		List<MatchingboardDto> list = matchingboardBiz.listPage(cri);
		List<UserDto> userinfo = new ArrayList<UserDto>();
		List<MovieDto> movieinfo = new ArrayList<MovieDto>();
		List<CinemaDto> cinemainfo = new ArrayList<CinemaDto>();
		int listsize = (list.size()) - 1;
//		System.out.println("????????? ?????????:" +listsize);

		for (int i = 0; i < list.size(); i++) {
			userinfo.add((UserDto) userBiz.selectOne(list.get(i).getUserseq()));
			movieinfo.add((MovieDto) movieBiz.selectOne(list.get(i).getMovieseq()));
			cinemainfo.add((CinemaDto) cinemaBiz.selectOne(list.get(i).getCinemaseq()));
		}

		// UserDto userDto=userBiz.selectOne(list.get(0).getUserseq());
		// MovieDto movieDto =movieBiz.selectOne(list.get(0).getMovieseq());
		// CinemaDto cinemaDto =cinemaBiz.selectOne(list.get(0).getCinemaseq());
		// System.out.println(userDto.getUsername()+"//"+userDto.getUsersex()+"//"+userDto.getUserage());
		// System.out.println(movieDto.getMovieseq()+"?????????:"+movieDto.getMovietitle());
		// System.out.println(cinemaDto.getCinemaseq()+"????????????:"+cinemaDto.getCinema());

		model.addAttribute("matchingboardlist1", list);
		model.addAttribute("matchingboardlist2", userinfo);
		model.addAttribute("matchingboardlist3", movieinfo);
		model.addAttribute("matchingboardlist4", cinemainfo);
		model.addAttribute("listsize", listsize);

		// ????????? ?????? ??????
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(matchingboardBiz.listCount());
		model.addAttribute("pageMaker", pageMaker);
		// ????????? ?????? ??????
//		model.addAttribute("matchingboardlist4", cinemaBiz.selectOne(list.get(0).getCinemaseq()));
		return "match_list";

	}

	@RequestMapping(value = "/matchingboardselectone.do")
	public String detail(Model model, int matchingboard, HttpSession session, HttpServletRequest request) { // ?????? ??????
																											// model ???
																											// ????????? ?????? id																						// ??? ???????????????
																											// ?????????.
		System.out.println("//" + matchingboard);
		MatchingboardDto matchingboarddto = matchingboardBiz
				.selectOne(Integer.parseInt(request.getParameter("matchingboard")));
		UserDto userdto = userBiz.selectOne(matchingboarddto.getUserseq()); // ????????? ??????
		CinemaDto cinemadto = cinemaBiz.selectOne(matchingboarddto.getCinemaseq());
		MovieDto moviedto = movieBiz.selectOne(matchingboarddto.getMovieseq());
		
		

//		System.out.println("user??????:"+matchingboarddto.getUserseq());
		model.addAttribute("matchingboarddetail1", matchingboarddto);
		model.addAttribute("matchingboarddetail2", userdto);
		model.addAttribute("matchingboarddetail3", moviedto);
		model.addAttribute("matchingboarddetail4", cinemadto);
		model.addAttribute("matchingboarddto",matchingboarddto);
		
		
		
	
		return "match_detail";
	}

	@RequestMapping(value = "/matchingForm.do")
	public String getMatchingForm(Model model, HttpServletResponse response, HttpSession session) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		MatchingboardDto matchingboarddto = null;

		MatchingDto matchingdto = null;

		int userseq = ((UserDto) session.getAttribute("dto")).getUserseq();

		

		matchingboarddto = matchingboardBiz.userOne(userseq);

		matchingdto = matchingBiz.selectMaxSeq(userseq);

		if(matchingboarddto == null) {//????????? ???????????? ?????????
			if(matchingdto == null) { //????????? ?????? ???

				List<CinemaDto> cinemaList = cinemaBiz.selectList();
				List<MovieDto> movieList = movieBiz.selectList();
				model.addAttribute("cinemaList", cinemaList);
				model.addAttribute("movieList", movieList);

				return "match_insert";

			}else { //????????? ????????? ???????????? ??????
				if(matchingdto.getMatchingstate().equals("P")) {//????????? ???????????????

					PrintWriter out = response.getWriter();
					out.println("<script>alert('?????? ?????? ?????? ????????? ????????????.');history.back();</script>");
					out.close();
					return null;
				} else if (matchingdto.getMatchingstate().equals("S")) {
					PrintWriter out = response.getWriter();
					out.println("<script>alert('?????? ?????? ?????? ????????? ????????????.');history.back();</script>");
					out.close();
					return null;
				} else if (matchingdto.getMatchingstate().equals("E")) {
					List<CinemaDto> cinemaList = cinemaBiz.selectList();
					List<MovieDto> movieList = movieBiz.selectList();
					model.addAttribute("cinemaList", cinemaList);
					model.addAttribute("movieList", movieList);
					return "match_insert";
				}
			}

		} else if (matchingboarddto != null) {

			PrintWriter out = response.getWriter();
			out.println("<script>alert('?????? ????????? ????????? ????????????.');history.back();</script>");
			out.close();
			return null;

		}

		return null;

	}

	@RequestMapping(value = "/matchingboard_insert.do", method = RequestMethod.POST)

	public void matchingboardInsert(Model model, HttpSession session, HttpServletResponse response, HttpServletRequest request, String cinema,
			String title, String moviename, String matchingboardcontent) throws IOException {
		
		response.setContentType("text/html; charset=utf-8"); 

		String movieseq = String.valueOf(movieBiz.getMovieSeq(moviename));
		String cinemaseq = String.valueOf(cinemaBiz.getCinemaSeq(cinema));
		Map<String, String> map = new HashMap<String, String>();

		map.put("movieseq", movieseq);
		map.put("userseq", String.valueOf(((UserDto) session.getAttribute("dto")).getUserseq()));
		map.put("matchingboardtitle", title);
		map.put("matchingboardcontent", matchingboardcontent);
		map.put("cinemaseq", cinemaseq);
		

		///////////////////////////

		UserDto userdto = (UserDto) session.getAttribute("dto");

		int point_val_01 = 0;
	      
		int coin_charge = 0;
		int coin_use = 0;
		int coin_use1 = 0;
		int coin_val = 0;

		coin_charge = coinBiz.coin(userdto.getUserseq(), "??????");

		coin_use = coinBiz.coin(userdto.getUserseq(), "?????? ????????? ??????");
		coin_use1 = coinBiz.coin(userdto.getUserseq(), "??????????????? ?????? ??????");

		coin_val = ((coin_charge - (coin_use+coin_use1)) / 500);

	      
	      if(coin_val < 1) {
	         
	         PrintWriter out = response.getWriter();
	         out.println("<script>alert('?????? ??????????????????');location.href='coin_charge.do';</script>");
	         out.close();
	    
	         
	      }else{
	         if (request.getParameter("point_val_01") != null) {
	      
	         point_val_01 = Integer.parseInt(request.getParameter("point_val_01"));
	         coinBiz.coin_insert(userdto.getUserseq(), point_val_01, "?????? ????????? ??????");
	         System.out.println("?????? ????????? ?????? ??????");
	         
	         int res = matchingboardBiz.insert(map);
	      
	      }

	      model.addAttribute("point_val_01", point_val_01);
	
	      PrintWriter out = response.getWriter();
	         out.println("<script>alert('?????? ???????????? ?????????????????????.');location.href='matchingboardlist.do';</script>");
	         out.close();
	}
	}

	@RequestMapping(value = "/matching_insert.do")
	public void match(Model model, HttpServletResponse response, HttpSession session, HttpServletRequest request)
			throws IOException {

		int userseq = ((UserDto) session.getAttribute("dto")).getUserseq();
		
		
		int matchingwriter = Integer.parseInt(request.getParameter("userseq"));
		String cinema = request.getParameter("cinema");
		String movietitle = request.getParameter("movietitle");

		int cinemaseq = cinemaBiz.getCinemaSeq(cinema);
		int movieseq = movieBiz.getMovieSeq(movietitle);

		
//		MatchingboardDto matchingboardto = matchingboardBiz.userOne(userseq);
		
		
		MatchingDto matchingDto = new MatchingDto();
		matchingDto.setMatchingwriter(matchingwriter);
		matchingDto.setMatchingapplicant(userseq);
		matchingDto.setMovieseq(movieseq);
		matchingDto.setCinemaseq(cinemaseq);

		response.setContentType("text/html; charset=UTF-8");

		MatchingDto mymatchingdto = null;
		mymatchingdto = matchingBiz.insertCheck(userseq);
		MatchingboardDto mymatchingboarddto = matchingboardBiz.userOne(userseq);
		MatchingboardDto matchingboardDto = matchingboardBiz.userOne(matchingwriter);
		
		int coin_charge = 0;
		int coin_use = 0;
		int coin_use1 = 0;
		int coin_val = 0;

		coin_charge = coinBiz.coin(userseq, "??????");

		coin_use = coinBiz.coin(userseq, "?????? ????????? ??????");
		coin_use1 = coinBiz.coin(userseq, "??????????????? ?????? ??????");

		coin_val = ((coin_charge - (coin_use+coin_use1)) / 500);

	      if (coin_val < 1) {

	         PrintWriter out = response.getWriter();
	         out.println("<script>alert('?????? ??????????????????, ??????????????? ????????? ???????????????????????? ???????????????.');location.href='coin_charge.do';</script>");
	         out.close();

	      } else {

	         /////

//	      if(matchingdto == null || matchingdto.getMatchingstate().equals("E")) {//?????????????????? ????????? ??????????????? ???????????? 
	         if(matchingboardDto != null) {
	    	  if (mymatchingboarddto == null) { // ??????
	            if (mymatchingdto == null) {
	               int res = matchingBiz.insert(matchingDto);
	               if (res > 0) {
	                  PrintWriter out = response.getWriter();
	                  out.println("<script>alert('?????? ?????? ??????');location.href='mypage_match_to.do';</script>");
	                  out.close();
	               } else {
	                  PrintWriter out = response.getWriter();
	                  out.println("<script>alert('?????????????????? ??????');history.back();</script>");
	                  out.close();
	               }

	            } else {
	               if (mymatchingdto.getMatchingstate().equals("P")) {
	                  PrintWriter out = response.getWriter();
	                  out.println("<script>alert('?????? ?????? ????????? ????????????.');history.back();</script>");
	                  out.close();
	               } else if (mymatchingdto.getMatchingstate().equals("S")) {
	                  PrintWriter out = response.getWriter();
	                  out.println("<script>alert('?????? ?????? ????????? ????????????.');history.back();</script>");
	                  out.close();
	               } else if (mymatchingdto.getMatchingstate().equals("E")) {
	                  int res = matchingBiz.insert(matchingDto);
	                  if (res > 0) {
	                     PrintWriter out = response.getWriter();
	                     out.println("<script>alert('?????? ?????? ??????');location.href='mypage_match_to.do';</script>");
	                     out.close();
	                  } else {
	                     PrintWriter out = response.getWriter();
	                     out.println("<script>alert('?????????????????? ??????');history.back();</script>");
	                     out.close();
	                  }
	               }

	            }

	         } else {

	            PrintWriter out = response.getWriter();
	            out.println("<script>alert('????????? ????????? ????????????. ??????????????? ????????? ????????????????????? ??????????????????.');history.back();</script>");
	            out.close();

	         }
	      } else {
	    	  PrintWriter out = response.getWriter();
	           out.println("<script>alert('????????? ????????? ??? ????????????.');location.href='matchingboardlist.do';</script>");
	           out.close();
	      }
	      }

	   }

//			MatchingDto matchingDto = new MatchingDto();
//			matchingDto.setMatchingwriter(matchingwriter);
//			matchingDto.setMatchingapplicant(userseq);
//			matchingDto.setMovieseq(movieseq);
//			matchingDto.setCinemaseq(cinemaseq);
//			int res = matchingBiz.insert(matchingDto);
//			if(res>0) { //????????? ????????? ????????? ?????????
//				if(matchingdto.getMatchingstate().equals("E")) { //???????????? ?????? ??????-> ????????????
//					PrintWriter out = response.getWriter();
//					out.println("<script>alert('?????? ?????? ??????');location.href='mypage_match_to.do';</script>");
//					out.close();
//				}else if(matchingdto.getMatchingstate().equals("P")) { //?????? ???????????? ??????
//					PrintWriter out = response.getWriter();
//					out.println("<script>alert('?????? ?????? ????????? ????????????.');history.back();</script>");
//					out.close();
//				}else if(matchingdto.getMatchingstate().equals("S")) { // ?????? ???????????????
//					PrintWriter out = response.getWriter();
//					out.println("<script>alert('?????? ?????? ????????? ????????????.');history.back();</script>");
//					out.close();
//				}
//			}else { //????????? ????????? ????????? ????????? -> ????????????
//				PrintWriter out = response.getWriter();
//				out.println("<script>alert('?????? ?????? ??????');location.href='mypage_match_to.do';</script>");
//				out.close();
//			}
//		}
			
//			if(mymatchingboarddto == null) { //???????????? ?????????
//			if (res > 0) {
//				PrintWriter out = response.getWriter();
//				out.println("<script>alert('?????? ?????? ??????');location.href='mypage_match_to.do';</script>");
//				out.close();
//
//			} else {
//				PrintWriter out = response.getWriter();
//				out.println("<script>alert('?????? ?????? ??????');history.back();</script>");
//				out.close();
//			}
//			}else { //???????????? ?????? ???
//				PrintWriter out = response.getWriter();
//				out.println("<script>alert('????????? ????????? ????????????. ??????????????? ????????? ????????????????????? ??????????????????.');history.back();</script>");
//				out.close();
//			}
//			
//		}
			
//		}else if(dto.getMatchingstate().equals("P")){//?????????
//			PrintWriter out = response.getWriter();
//			out.println("<script>alert('?????? ?????? ????????? ????????????.');history.back();</script>");
//			out.close();
//		}else if(dto.getMatchingstate().equals("S")) {//????????????
//			PrintWriter out = response.getWriter();
//			out.println("<script>alert('?????? ?????? ????????? ????????????.');history.back();</script>");
//			out.close();
//		}
	
	@RequestMapping(value="/mypage_match_to.do", method=RequestMethod.GET)
	public String getMypage_match_to(Model model,HttpSession session) {
		int userseq = ((UserDto)session.getAttribute("dto")).getUserseq();
		MatchingDto matchingdto = matchingBiz.userOne(userseq);
		//System.out.println("/////"+matchingdto.getMatchingwriter());

		UserDto writerUser = null;
		MatchingboardDto matchingboarddto = null;
		if (matchingdto != null) {
			writerUser = userBiz.selectOne(matchingdto.getMatchingwriter());
			matchingboarddto = matchingboardBiz.userOne(matchingdto.getMatchingwriter());
		}

		// System.out.println("////"+writerUser.getUsername());

		// System.out.println("////"+writerUser.getUsername());
		matchingBiz.readMatchRequested(userseq);
		MatchingboardDto boarddto = matchingboardBiz.userOne(userseq);
		List<MatchingDto> matchingList = matchingBiz.getMatchingApplicant(userseq);
		List<UserDto> userList = new ArrayList<UserDto>();
		for (int i = 0; i < matchingList.size(); i++) {
			userList.add(userBiz.selectOne(matchingList.get(i).getMatchingapplicant()));
		}
		model.addAttribute("writerUser", writerUser);
		model.addAttribute("matchingboarddto", matchingboarddto);
		model.addAttribute("matchingdto", matchingdto);

		model.addAttribute("boarddto", boarddto);
		model.addAttribute("matchingList", matchingList);
		model.addAttribute("userList", userList);

		return "mypage_match_to";
	}
	@RequestMapping(value = "/matchingdelete.do", method = RequestMethod.GET)
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=utf-8");
		int matchingseq = Integer.parseInt(request.getParameter("matchingseq"));
		int res = matchingBiz.delete(matchingseq);
		
		if(res > 0) {
			response.sendRedirect("mypage_match_to.do");
		}else {
			PrintWriter out = response.getWriter();
			out.println("<script>alert('?????????????????? ??????');history.back();</script>");
			out.close();
		}
	}
	
	
	@RequestMapping(value = "/matchingcut.do", method = RequestMethod.GET)
	public void matchingdelete(Model model, HttpSession session,  HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		System.out.println("???????????? ????????????");
		response.setContentType("text/html; charset=utf-8");
		UserDto dto=(UserDto)session.getAttribute("dto");
		String matchingseq = request.getParameter("matchingseq");
//		MatchingDto matchingdto = matchingBiz.matchSuccess(dto.getUserseq());
//		matchingseq = matchingdto.getMatchingseq();
		System.out.println(matchingseq);
		int res = matchingBiz.matchingcut(Integer.parseInt(matchingseq));
		System.out.println("///");
		System.out.println("res : "+res);
		if(res>0) {
			chattingBiz.delete(dto.getUserseq());
			PrintWriter out = response.getWriter();
			out.println("<script>alert('????????? ??????????????????.');location.href='mypage.do';</script>");
			out.close();
		}else {
			PrintWriter out = response.getWriter();
			out.println("<script>alert('?????? ??????');location.href='mypage.do';</script>");
			out.close();
		}
//
//		response.sendRedirect("mypage_match_to.do");
	}

	@RequestMapping(value = "/match_update.do", method = RequestMethod.GET)
	public String getMatchingBoardUpdate(Model model, HttpServletRequest request) {
		String matchingboard = request.getParameter("matchingboard");
		MatchingboardDto matchingboarddto = matchingboardBiz.selectOne(Integer.parseInt(matchingboard));
		UserDto userdto = userBiz.selectOne(matchingboarddto.getUserseq());
		CinemaDto cinemadto = cinemaBiz.selectOne(matchingboarddto.getCinemaseq());
		MovieDto moviedto = movieBiz.selectOne(matchingboarddto.getMovieseq());
		List<CinemaDto> cinemaList = cinemaBiz.selectList();
		List<MovieDto> movieList = movieBiz.selectList();

		model.addAttribute("matchingboarddto", matchingboarddto);
		model.addAttribute("userdto", userdto);
		model.addAttribute("cinemadto", cinemadto);
		model.addAttribute("movieddto", moviedto);
		model.addAttribute("cinemaList", cinemaList);
		model.addAttribute("movieList", movieList);

		return "match_update";
	}

	@RequestMapping(value = "/matchingboard_update.do", method = RequestMethod.POST)
	public void matchingBoardUpdate(HttpServletResponse response, String cinema, String title, String moviename,
			String matchingboardcontent, String matchingboard) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		String matchingboardseq = String.valueOf(matchingboard);
		String movieseq = String.valueOf(movieBiz.getMovieSeq(moviename));
		String cinemaseq = String.valueOf(cinemaBiz.getCinemaSeq(cinema));
		Map<String, String> map = new HashMap<String, String>();
		map.put("matchingboard", matchingboardseq);
		map.put("movieseq", movieseq);
		map.put("matchingboardtitle", title);
		map.put("matchingboardcontent", matchingboardcontent);
		map.put("cinemaseq", cinemaseq);

		int res = matchingboardBiz.update(map);
		if (res > 0) {
			PrintWriter out = response.getWriter();
			out.println("<script>alert('??? ?????? ??????');location.href='matchingboardlist.do'</script>");
			out.close();
		} else {
			PrintWriter out = response.getWriter();
			out.println("<script>alert('??? ?????? ??????');history.back();</script>");
			out.close();
		}

	}

	// ??? ?????? + ?????????
	@RequestMapping(value = "/listPage.do", method = RequestMethod.GET)
	public void listPage(@ModelAttribute("cri") Criteria cri, Model model) {
		System.out.println("????????? ??????");
		List<MatchingboardDto> list = matchingboardBiz.listPage(cri);
		model.addAttribute("list", list);
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(matchingboardBiz.listCount());
		model.addAttribute("pageMaker", pageMaker);
	}

	@RequestMapping(value = "/acceptance.do", method = RequestMethod.GET)
	public void acceptance(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		int userseq = ((UserDto) session.getAttribute("dto")).getUserseq();
		int matchingapplicant = Integer.parseInt(request.getParameter("matchingapplicant"));
		int matchingwriter = ((UserDto) session.getAttribute("dto")).getUserseq();
		Map<String, String> map = new HashMap<String, String>();
		map.put("matchingwriter", String.valueOf(matchingwriter));
		map.put("matchingapplicant", String.valueOf(matchingapplicant));

		int res1 = matchingBiz.acceptance(map);
		//int res2 = matchingBiz.rejection(map);
		List<MatchingDto> list = matchingBiz.rejectionAll(map);
		if(list.size() != 0) {
			for(int i = 0 ; i < list.size() ; i++) {
				map.put("matchingapplicant", String.valueOf(list.get(i).getMatchingapplicant()));
				matchingBiz.rejectionOne(map);
			}
		}
		
	///// ?????? ?????? ?????? /////////////////

	      int point_val_01 = 0;

	      point_val_01 = 500;
	      coinBiz.coin_insert(matchingapplicant, point_val_01, "??????????????? ?????? ??????");
	      System.out.println("?????? ?????? ??????");

	      int coin_charge = 0;
			int coin_use = 0;
			int coin_use1 = 0;
			int coin_val = 0;

			coin_charge = coinBiz.coin(userseq, "??????");

			coin_use = coinBiz.coin(userseq, "?????? ????????? ??????");
			coin_use1 = coinBiz.coin(userseq, "??????????????? ?????? ??????");

			coin_val = ((coin_charge - (coin_use+coin_use1)) / 500);

		if (res1 > 0) {
			matchingboardBiz.delete(userseq);
			PrintWriter out = response.getWriter();
			out.println("<script>alert('?????? ?????? ??????');location.href='mypage_match_success.do';</script>");
			out.close();
		} else {
			PrintWriter out = response.getWriter();
			out.println("<script>alert('?????? ?????? ??????');history.back();</script>");
			out.close();
		}
	}

	@RequestMapping(value = "rejectionOne.do", method = RequestMethod.GET)
	public void rejectionOne(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		int matchingapplicant = Integer.parseInt(request.getParameter("matchingapplicant"));
		int matchingwriter = ((UserDto) session.getAttribute("dto")).getUserseq();
		Map<String, String> map = new HashMap<String, String>();
		map.put("matchingwriter", String.valueOf(matchingwriter));
		map.put("matchingapplicant", String.valueOf(matchingapplicant));

		int res = matchingBiz.rejectionOne(map);
		if (res > 0) {
			PrintWriter out = response.getWriter();
			out.println("<script>alert('?????????????????????.');location.href='mypage_match_to.do';</script>");
			out.close();
		} else {
			PrintWriter out = response.getWriter();
			out.println("<script>alert('?????????????????? ??????');history.back();</script>");
			out.close();
		}

	}

	@RequestMapping(value = "mypage_match_success.do", method = RequestMethod.GET)
	public String mypageMatchSuccess(Model model, HttpSession session) {
		int userseq = ((UserDto) session.getAttribute("dto")).getUserseq();
		MatchingDto matchingDto = null;
		matchingDto = matchingBiz.matchSuccess(userseq);
		if (matchingDto != null) { 
			Map<String, String> map = new HashMap<String, String>();
			map.put("matchingwriter", String.valueOf(matchingDto.getMatchingwriter()));
			map.put("matchingapplicant", String.valueOf(matchingDto.getMatchingapplicant()));
			MatchingboardDto matchingboardDto = matchingboardBiz.getCinemaSeq(map);
			
			matchingBiz.readMatchSuccess(userseq);
			
			String cinema = cinemaBiz.getCinema(matchingDto.getCinemaseq());
			MovieDto movieDto = movieBiz.selectOne(matchingDto.getMovieseq());
			UserDto userDto = null;
			if (userseq == matchingDto.getMatchingwriter()) {
				userDto = userBiz.selectOne(matchingDto.getMatchingapplicant());
			} else {
				userDto = userBiz.selectOne(matchingDto.getMatchingwriter());
			}

			model.addAttribute("userDto", userDto);
			model.addAttribute("matchingDto", matchingDto);
			model.addAttribute("cinema", cinema);
			model.addAttribute("imgurl", movieDto.getImgurl());
		}

		return "mypage_match_Success";
	}
	
	@RequestMapping(value="/match_history.do",method = RequestMethod.GET)
	public String match_history(HttpSession session, Model model) {
		int userseq = ((UserDto)session.getAttribute("dto")).getUserseq();
		List<MatchingDto> matchingList = matchingBiz.history(userseq);
		List<UserDto> userList = new ArrayList<UserDto>();
		if(matchingList.size() != 0) {
			for(int i = 0 ; i < matchingList.size() ; i++) {
				if(userseq == matchingList.get(i).getMatchingwriter()) {
					userList.add(userBiz.selectOne(matchingList.get(i).getMatchingapplicant()));
				}else {
					userList.add(userBiz.selectOne(matchingList.get(i).getMatchingwriter()));
				}
			}
		}
		matchingBiz.readMatchRejection(userseq);
		model.addAttribute("matchingList",matchingList);
		model.addAttribute("userList",userList);
		
		return "match_history";
	}

	@RequestMapping(value = "/chat.do", method = RequestMethod.GET)
	public String chat(HttpServletRequest request, Model model) {
		String userseq2 = request.getParameter("userseq2");

		UserDto userDto = userBiz.selectOne(Integer.parseInt(userseq2));
		
		model.addAttribute("userDto",userDto);
		model.addAttribute("userseq2",userseq2);
		

		return "chat";
	}

	// ?????? ?????? ??????

	@RequestMapping(value = "/map.do", method = RequestMethod.GET)
	public String getSelectCinema(Model model) {

		System.out.println("?????????????????? ???????????????");

		return "matchingBoard/map";
	}

	@RequestMapping(value = "/getJson.do", method = RequestMethod.GET)
	@ResponseBody
	public String getCinemaLoc(Model model, HttpServletResponse response) throws IOException {

		System.out.println("json?????? ???????????????");
		JSONObject jsonObject = new JSONObject();
		JSONArray markerArray = new JSONArray();

		// marker??? JSON ????????? ?????? Array ??????
		List<CinemaDto> list = cinemaBiz.selectList();
		for (int i = 0; i < list.size(); i++) {

			CinemaDto dto = list.get(i);
			String imgSrc = null;
			if (dto.getCinema().contains("CGV")) {
				imgSrc = "img/cgv.png";
			} else if (dto.getCinema().contains("??????")) {
				imgSrc = "img/lotte.png";
			}

			JSONObject markerInfo = new JSONObject();

			markerInfo.put("html", "<a href=\"matchingboardlistbycinema.do?cinemaseq="+dto.getCinemaseq()+"\" style='font-size:20px;'><img src=\"" + imgSrc
					+ "\" width=\"50px\" height=\"50px\"/>" + dto.getCinema() + "</a>");
			markerInfo.put("lat", list.get(i).getLatitude());
			markerInfo.put("lng", list.get(i).getLongitude());

			markerArray.add(markerInfo);
		}
		jsonObject.put("positions", markerArray);

		response.setContentType("text/xml; charset=utf-8");

		PrintWriter out = response.getWriter();
		out.print(jsonObject.toJSONString());

		return null;
	}

	@RequestMapping(value = "/matchingboardlistbycinema.do", method = RequestMethod.GET)
	public String listByCinema(Model model, HttpSession session, String cinemaseq) {
		
		

		//////
		UserDto userdto = (UserDto) session.getAttribute("dto");

		int coin_charge = 0;
		int coin_use = 0;
		int coin_use1 = 0;
		int coin_val = 0;

		if(userdto == null) {
			
		}else {
		coin_charge = coinBiz.coin(userdto.getUserseq(), "??????");

		coin_use = coinBiz.coin(userdto.getUserseq(), "?????? ????????? ??????");
		coin_use1 = coinBiz.coin(userdto.getUserseq(), "??????????????? ?????? ??????");

		coin_val = ((coin_charge - (coin_use+coin_use1)) / 500);

		model.addAttribute("user_name", userdto.getUsername());
		model.addAttribute("coin", coin_val);
		}
		////
		System.out.println("---------------????????? ?????? " + cinemaseq);

		List<MatchingboardDto> list = matchingboardBiz.selectListByCinema(Integer.parseInt(cinemaseq));
		List<UserDto> userinfo = new ArrayList<UserDto>();
		List<MovieDto> movieinfo = new ArrayList<MovieDto>();
		List<CinemaDto> cinemainfo = new ArrayList<CinemaDto>();
		int listsize = (list.size()) - 1;


		for (int i = 0; i < list.size(); i++) {
			userinfo.add((UserDto) userBiz.selectOne(list.get(i).getUserseq()));
			movieinfo.add((MovieDto) movieBiz.selectOne(list.get(i).getMovieseq()));
			cinemainfo.add((CinemaDto) cinemaBiz.selectOne(list.get(i).getCinemaseq()));
		}

		

		model.addAttribute("matchingboardlist1", list);
		model.addAttribute("matchingboardlist2", userinfo);
		model.addAttribute("matchingboardlist3", movieinfo);
		model.addAttribute("matchingboardlist4", cinemainfo);
		model.addAttribute("listsize", listsize);

		
//		
		return "matchingBoard/match_list2";


	}
	
	@RequestMapping(value = "/mycinema.do", method = RequestMethod.GET)
	public String listByCinema(Model model, HttpSession session, HttpServletResponse response) throws IOException {
		
		UserDto userdto = (UserDto) session.getAttribute("dto");
		response.setContentType("text/html; charset=UTF-8");
		if (userdto == null) {
	         PrintWriter out = response.getWriter();
	         out.println("<script>alert('????????? ????????????.');history.back();</script>");
	         out.close();
	         return null;
		}else {	
			
			
			int coin_charge = 0;
			int coin_use = 0;
			int coin_val = 0;

			coin_charge = coinBiz.coin(userdto.getUserseq(), "??????");

			coin_use = coinBiz.coin(userdto.getUserseq(), "??????");

			coin_val = ((coin_charge - coin_use) / 500);

			model.addAttribute("user_name", userdto.getUsername());
			model.addAttribute("coin", coin_val);
			
			int usercinema1 = userdto.getUsercinema1();
			int usercinema2 = userdto.getUsercinema2();
			int usercinema3 = userdto.getUsercinema3();	

			List<MatchingboardDto> list = matchingboardBiz.selectListByCinema(usercinema1);
			list.addAll(matchingboardBiz.selectListByCinema(usercinema2));
			list.addAll(matchingboardBiz.selectListByCinema(usercinema3));
			
			List<UserDto> userinfo = new ArrayList<UserDto>();
			List<MovieDto> movieinfo = new ArrayList<MovieDto>();
			List<CinemaDto> cinemainfo = new ArrayList<CinemaDto>();
			int listsize = (list.size()) - 1;


			for (int i = 0; i < list.size(); i++) {
				userinfo.add((UserDto) userBiz.selectOne(list.get(i).getUserseq()));
				movieinfo.add((MovieDto) movieBiz.selectOne(list.get(i).getMovieseq()));
				cinemainfo.add((CinemaDto) cinemaBiz.selectOne(list.get(i).getCinemaseq()));
			}

			

			model.addAttribute("matchingboardlist1", list);
			model.addAttribute("matchingboardlist2", userinfo);
			model.addAttribute("matchingboardlist3", movieinfo);
			model.addAttribute("matchingboardlist4", cinemainfo);
			model.addAttribute("listsize", listsize);

			
//			
			return "matchingBoard/match_list2";
		}
		

	}

		


	// ?????? ?????? ???
	
	
	
	@RequestMapping("/movie_matching.do")
	public String listBymovietitle(Model model, HttpSession session, String movieseq) {
		
		

		//////
		UserDto userdto = (UserDto) session.getAttribute("dto");

		int coin_charge = 0;
		int coin_use = 0;
		int coin_use1 = 0;
		int coin_val = 0;

		if(userdto == null) {
			
		}else {
		coin_charge = coinBiz.coin(userdto.getUserseq(), "??????");

		coin_use = coinBiz.coin(userdto.getUserseq(), "?????? ????????? ??????");
		coin_use1 = coinBiz.coin(userdto.getUserseq(), "??????????????? ?????? ??????");

		coin_val = ((coin_charge - (coin_use+coin_use1)) / 500);

		model.addAttribute("user_name", userdto.getUsername());
		model.addAttribute("coin", coin_val);
		}
		////


		List<MatchingboardDto> list = matchingboardBiz.selectListByMovieseq((Integer.parseInt(movieseq)));
		List<UserDto> userinfo = new ArrayList<UserDto>();
		List<MovieDto> movieinfo = new ArrayList<MovieDto>();
		List<CinemaDto> cinemainfo = new ArrayList<CinemaDto>();
		int listsize = (list.size()) - 1;


		for (int i = 0; i < list.size(); i++) {
			userinfo.add((UserDto) userBiz.selectOne(list.get(i).getUserseq()));
			movieinfo.add((MovieDto) movieBiz.selectOne(list.get(i).getMovieseq()));
			cinemainfo.add((CinemaDto) cinemaBiz.selectOne(list.get(i).getCinemaseq()));
		}

		

		model.addAttribute("matchingboardlist1", list);
		model.addAttribute("matchingboardlist2", userinfo);
		model.addAttribute("matchingboardlist3", movieinfo);
		model.addAttribute("matchingboardlist4", cinemainfo);
		model.addAttribute("listsize", listsize);

		
//		
		return "matchingBoard/match_movie";

		}
		

}
