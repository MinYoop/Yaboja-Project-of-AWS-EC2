package com.yaboja.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Gmail extends Authenticator{
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication("myk430s@gmail.com","phwulktkreelvyjo");//앱용 구글 계정 2차 비번임
	}
}
