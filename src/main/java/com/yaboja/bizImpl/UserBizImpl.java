package com.yaboja.bizImpl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.yaboja.biz.UserBiz;
import com.yaboja.controller.ReviewboardController;
import com.yaboja.daoImpl.UserDaoImpl;
import com.yaboja.dto.CinemaDto;
import com.yaboja.dto.ReviewboardDto;
import com.yaboja.dto.UserDto;

@Service
public class UserBizImpl implements UserBiz {

	@Autowired
	UserDaoImpl dao;

	@Autowired
	BCryptPasswordEncoder bcryptPasswordEncoder;

	private static final Logger logger = LoggerFactory.getLogger(ReviewboardController.class);

	@Override
	public UserDto selectOne(String id) {
		UserDto dto = dao.selectOne(id);
		return dto;
	}

	public int insert(UserDto userdto) throws Exception {

		// 비밀번호 암호화 spring-sequrity
		String encPassword = bcryptPasswordEncoder.encode(userdto.getUserpw());
		userdto.setUserpw(encPassword);
		logger.info("암호화된 비밀번호 : " + userdto.getUserpw());
		return dao.insert(userdto);
	}

	public UserDto login(String userid, String userpw) {

		UserDto dto = dao.selectOne(userid);

		String dbPw = dto.getUserpw();

		logger.info("암호화 비밀번호 : " + dbPw);
		logger.info("입력 비밀번호 : " + userpw);
		if (bcryptPasswordEncoder.matches((CharSequence)userpw,dbPw)) {
			logger.info("비밀번호 일치");
			return dto;
		} else {
			logger.info("비밀번호 불일치");
			return null;
		}
	}

	public List<UserDto> selectAll() {
		return dao.selectAll();
	}

	@Override
	public int updateUser(int userseq, String grade) {
		return dao.updateUser(userseq, grade);
	}

	public UserDto selectOne(int userseq) {
		// TODO Auto-generated method stub
		return dao.selectOne(userseq);

	}

	@Override
	public UserDto getLogin(String userid, String userpw) {
		return dao.getLogin(userid, userpw);
	}

	@Override
	public UserDto selectOne1(String userid) {
		return dao.selectOne1(userid);
	}

	@Override
	public int update(UserDto dto) {
		return dao.update(dto);
	}

	@Override
	public int delete(String userid) {
		return dao.delete(userid);
	}

	@Override
	public List<ReviewboardDto> myboardList(int userseq) {
		return dao.myboardList(userseq);
	}

	@Override
	public int gradeUpdate(UserDto dto) {
		return dao.gradeUpdate(dto);
	}
}
