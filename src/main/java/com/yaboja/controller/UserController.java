package com.yaboja.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import com.yaboja.bizImpl.CinemaBizImpl;
import com.yaboja.bizImpl.UserBizImpl;
import com.yaboja.dto.CinemaDto;
import com.yaboja.dto.UserDto;
import com.yaboja.util.Gmail;
import com.yaboja.util.SHA256;

@Controller
public class UserController {

	@Autowired
	UserBizImpl userbiz;
	@Autowired
	CinemaBizImpl cinemabiz;

	//???????????? ???????????? 
	@RequestMapping(value = "joincheck.do", method = RequestMethod.GET)
	public String joincheck() {

		return "joincheck";
	}
	//???????????????
	@RequestMapping(value = "joinform.do", method = RequestMethod.GET)
	public String joinform(Model model) {
		List<CinemaDto> cinemaList=cinemabiz.selectAll();

		model.addAttribute("cinemaList",cinemaList);
		return "joinform";
	}
	//???????????? ??????
	@RequestMapping(value="join.do", method=RequestMethod.POST)
	public String join(UserDto userdto,HttpServletResponse response) throws Exception {
		int res;
		try {
			res = userbiz.insert(userdto);
			if(res>0) {
				response.setContentType("text/html; charset=UTF-8");
	            PrintWriter out = response.getWriter();
	            out.println("<script>alert('??????????????? ?????????????????????.');</script>"); // history.go(-1);</script>
	            out.flush();
				return "main";
			}else {
				response.setContentType("text/html; charset=UTF-8");
	            PrintWriter out = response.getWriter();
	            out.println("<script>alert('??????????????? ?????????????????????.');</script>"); // history.go(-1);</script>
	            out.flush();
				return "joinform";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	//????????? ???
	@RequestMapping(value="loginform.do",method = RequestMethod.GET)
	public String loginform() {
		
		return "loginform";
	}
	//????????????
	@RequestMapping(value="logout.do",method = RequestMethod.GET)
	public String logout(String userpw, HttpSession session,Model model,HttpServletRequest request) {
		
		//??????????????? "kakao"?????? ????????? ??????????????????
		System.out.println(userpw);
		if(userpw.equals("kakao")) {
			//??????????????? (???????????????)
			System.out.println("?????? ???????????? :"+request.getSession().getServletContext().getAttribute("code"));
			JsonNode jsonToken = (JsonNode) request.getSession().getServletContext().getAttribute("access_token");
//			System.out.println("//"+jsonToken);
			//			JsonNode jsonToken = getAccessToken(code);		
//			System.out.println("access_token : " + jsonToken.get("access_token"));
			System.out.println("????????? ?????? : "+jsonToken.get("access_token"));
			JsonNode userInfo = kakaoLogout(jsonToken.get("access_token"));
			System.out.println("???????????????(?????????)"+userInfo);
			
			System.out.println();
		}
		session.invalidate();
		System.out.println("????????????!");
			
		model.addAttribute("msg", "???????????? ???????????????.");
		model.addAttribute("view","main");
		
		return "inc/msg";
	}
	//????????? ??????
	@RequestMapping(value="login.do", method=RequestMethod.POST)
	public String login(String userid, String userpw, HttpSession session,HttpServletResponse response,Model model) throws IOException {

		UserDto dto=userbiz.login(userid,userpw);
		
		if(dto ==null) {
			System.out.println("????????? ??????!");
			response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('????????? ????????? ??????????????????.');</script>"); // history.go(-1);</script>
            out.flush();
			return "main";
		}else if(dto.getUsergrade().equals("drop")) {
			System.out.println("????????? ??????!");
			response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('????????? ???????????????.');</script>"); // history.go(-1);</script>
            out.flush();
			return "main";
		}
		
		else {
			session.setAttribute("dto", dto);
			
			System.out.println("????????? ??????!");
			response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
//            out.println("<script>alert('????????? ?????????????????????.');</script>"); // history.go(-1);</script>
            out.flush();
    		model.addAttribute("msg", "????????? ?????????????????????.");
    		model.addAttribute("view","main");
			return "inc/msg";
		}
		
		
	}	
	@RequestMapping(value="kakaoLoginForm.do",method = { RequestMethod.GET, RequestMethod.POST } )
	public String kakaoLoginForm() {
		return "kakaoLoginForm";
	}
	
	//ID ????????????
	@RequestMapping(value = "checkId.do", method = RequestMethod.POST)
	@ResponseBody
	public int idCheck(String id) {
		UserDto dto = userbiz.selectOne(id);
		
		if(dto == null) {
			return 0;
		}else {
			return 1;
		}

	}

	
	//?????? ????????? ??????(ajax)
	//1.dependency 2??? ?????? (?????? ????????????) 2.servlet-context?????? multipartResolver ??????
    @RequestMapping(value = "fileUpload.do", method=RequestMethod.POST)
    @ResponseBody
    public String fileUp(String userid,MultipartHttpServletRequest multi, HttpServletRequest request) throws FileNotFoundException {

        // ?????? ?????? ??????	
    	String path="";
//    	File file = new File("C:\\Users\\");
//    	File[] fileList = file.listFiles(); 
//    	String tmp=""; 
//
//    	
//    	if(fileList.length > 0){
//    	    for(int i=0; i < fileList.length; i++){
//    	    	System.out.println("------------------------"+fileList[i]);
//    	    	tmp=fileList[i]+"\\git\\yaboja\\FinalProject\\src\\main\\webapp\\profile";
//    	    	File file_exe= new File(tmp);
//    	    	if(file_exe.isDirectory()) {
//    	    		path=tmp;
//    	    	}
//    	    	
//    	    }
//    	}
    	
    	//path="C:\\Users\\MinYeop\\Desktop\\KH\\Workspace_Spring\\Yaboja\\src\\main\\webapp\\profile"; //?????? ????????? ?????????!
    	path="/var/lib/tomcat8/webapps/profile";
    	System.out.println("!!!!"+path);
        String newFileName = ""; // ????????? ?????? ?????????
         
        File dir = new File(path);
        if(!dir.isDirectory()){
            dir.mkdir();
        }
         
        Iterator<String> files = multi.getFileNames();
     
        String fileName=null;
        while(files.hasNext()){  
            String uploadFile = files.next();         
            MultipartFile mFile = multi.getFile(uploadFile);
             fileName = mFile.getOriginalFilename();
             
            
            //???????????? ?????? ????????? ??????
            int index=fileName.indexOf(".");
            fileName=userid+"."+fileName.substring(index+1);
            System.out.println("?????? ?????? ?????? : " +fileName);
            try {
                mFile.transferTo(new File(path+"/"+fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }
    

    
    //????????? ??????
    @RequestMapping(value="emailChk.do", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String emailChk(String email) {
    	String emailHash=SHA256.getSHA256(email);
    	
    	//host??????!!!!!!!!!!!!!!
    	String host = "http://minyeop.site/controller/";
    	String from = "myk430s@gmail.com";
    	String to = email;
    	String subject = "??????????????? ?????? ????????? ?????? ???????????????.";
    	String content ="?????? ?????? : "+emailHash;
    		
    	Properties p = new Properties();
    	p.put("mail.smtp.user", from);
    	p.put("mail.smtp.host", "smtp.googlemail.com");
    	p.put("mail.smtp.port", "465");
    	p.put("mail.smtp.starttls.enable","true");
    	p.put("mail.smtp.auth","true");
    	p.put("mail.smtp.debug","true");
    	p.put("mail.smtp.socketFactory.port","465");
    	p.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
    	p.put("mail.smtp.socketFactory.fallback","false");
    	
    	System.out.println("from:" +from);
    	try{
    		Authenticator auth = new Gmail();
    		Session ses = Session.getInstance(p, auth);
    		ses.setDebug(true);
    		MimeMessage msg = new MimeMessage(ses);
    		msg.setSubject(subject);
    		Address fromAddr = new InternetAddress(from);
    		msg.setFrom(fromAddr);
    		Address toAddr = new InternetAddress(to);
    		msg.addRecipient(Message.RecipientType.TO,toAddr);
    		msg.setContent(content,"text/html;charset=UTF8");
    		Transport.send(msg);
    	}catch(Exception e){
    		e.printStackTrace();
    
    		
    	}
    	
    	
    	return emailHash;
    }

    //????????? ????????? ??????
	@RequestMapping(value ="kakaologin.do",produces="application/json", method = {RequestMethod.GET,RequestMethod.POST})
	public String kakaologin(@RequestParam("code") String code,Model model, HttpServletRequest request,HttpServletResponse response, HttpSession session) throws IOException {
		System.out.println("!!!!");
		
		//????????? ???  ?????? get		
		System.out.println("code : "+code);
		request.getSession().getServletContext().setAttribute("code", code);
		System.out.println("application scope : "+request.getSession().getServletContext().getAttribute("code"));
		System.out.println("---------");
		
		//???????????????
		JsonNode jsonToken = getAccessToken(code);
		request.getSession().getServletContext().setAttribute("access_token", jsonToken);
		System.out.println("access_token : " + jsonToken.get("access_token"));
		System.out.println("----------");
			
		
		//??????????????? (???????????????)
		JsonNode userInfo = getKakaoUserInfo1(jsonToken.get("access_token"));
		System.out.println("???????????????(?????????)"+userInfo);
        // Get id
       

 
        // ???????????? ??????????????? ???????????? Get properties
		String id = userInfo.path("id").asText();
        JsonNode properties = userInfo.path("properties");
        JsonNode kakao_account = userInfo.path("kakao_account");
 
        //id?????????
        String name = properties.path("nickname").asText();
        String email = kakao_account.path("email").asText();
        String age="";
        if(kakao_account.path("has_age_range").asText()=="false") {
        	age="";
        }else {
        	age=kakao_account.path("age_range").asText();
        	age=age.substring(0, 2);
        }
        String sex="";
        if(kakao_account.path("has_gender").asText()=="false") {
        	sex="";
        }else {
        	sex=kakao_account.path("gender").asText();
        }
        
  
        
        System.out.println("id : " + id);
        System.out.println("name : " + name);
        System.out.println("email : " + email);
        System.out.println("age : "+age);
        System.out.println("sex : "+sex);
        //???
        
        //???????????????????????? ?????? ???????????? ???????????? ????????????
        UserDto dto=userbiz.selectOne(id);
       
        if(dto==null) {
        	//???????????????????????????
        	Map<String,String> map=new HashMap<String,String>();
        	map.put("userid", id);
        	map.put("username", name);
        	map.put("useremail", email);
        	map.put("userage", age);
        	map.put("usersex", sex);
        	
        	model.addAttribute("map",map);
        	List<CinemaDto> cinemaList=cinemabiz.selectAll();

    		model.addAttribute("cinemaList",cinemaList);
        	return "kakaoLoginForm";
        }else {
        	//????????? ????????????
        	if(dto.getUsergrade().equals("drop")) {
    			response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('????????? ???????????????.');</script>"); // history.go(-1);</script>
                out.flush();
                return "redirect:main.do";
        	}else {
        		session.setAttribute("dto", dto);
        		return "redirect:main.do";
        	}
        }
       
              
        
			
		
	}
	//????????????????????? ??????
	@RequestMapping(value=" kakaoJoin.do", method=RequestMethod.POST)
	public String kakaoJoin(UserDto userdto,HttpSession session,HttpServletResponse response) throws IOException {
		
		userdto.setUserpw("kakao");
		int res;
		try {
			res = userbiz.insert(userdto);
			if(res>=0) {
				//??????
				session.setAttribute("dto",userdto);
				response.setContentType("text/html; charset=UTF-8");
	            PrintWriter out = response.getWriter();
	            out.println("<script>alert('????????? ?????????????????????.');</script>"); // history.go(-1);</script>
	            out.flush();
				return "main";
			}else {
				//??????
				return "main";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String id=userdto.getUserid();
		userdto=userbiz.selectOne(id);
		
		return null;
	} 
	
	//access token??????
	public static JsonNode getAccessToken(String autorize_code){ 

	    final String RequestUrl = "https://kauth.kakao.com/oauth/token";
	   
	    final List<NameValuePair> postParams = new ArrayList<NameValuePair>();

	    postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
	    postParams.add(new BasicNameValuePair("client_id", "cacd5231c6e9a148742b217941190c01"));    // REST API KEY
	    postParams.add(new BasicNameValuePair("redirect_uri", "http://3.210.71.242:8080/controller/kakaologin.do"));    // ??????????????? URI
	    postParams.add(new BasicNameValuePair("code", autorize_code));    // ????????? ????????? ?????? code ???

	    final HttpClient client = HttpClientBuilder.create().build();
	    final HttpPost post = new HttpPost(RequestUrl);
	    JsonNode returnNode = null;

	    try {
	      post.setEntity(new UrlEncodedFormEntity(postParams));
	      final HttpResponse response = client.execute(post);
	      final int responseCode = response.getStatusLine().getStatusCode();

	      System.out.println("\nSending 'POST' request to URL : " + RequestUrl);
	      System.out.println("Post parameters : " + postParams);
	      System.out.println("Response Code : " + responseCode);
	     

	      //JSON ?????? ????????? ??????
	      ObjectMapper mapper = new ObjectMapper();
	      returnNode = mapper.readTree(response.getEntity().getContent());
	    } catch (UnsupportedEncodingException e) {
	      e.printStackTrace();
	    } catch (ClientProtocolException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } finally {
	        // clear resources
	    }

	    

	    return returnNode;

	}
	//????????? ????????????
	 public static JsonNode kakaoLogout(JsonNode accessToken) {
		 
	        final String RequestUrl = "https://kapi.kakao.com/v1/user/logout";
	        

	        final HttpClient client = HttpClientBuilder.create().build();
	        final HttpPost post = new HttpPost(RequestUrl);
	 
	        // add header
	        post.addHeader("Authorization", "Bearer " + accessToken);
//	        post.addHeader("Authorization", "KakaoAK " + "8c89186a433ca2d33527288541e96fef");
	        JsonNode returnNode = null;
	 
	        try {
	            final HttpResponse response = client.execute(post);
	            final int responseCode = response.getStatusLine().getStatusCode();
	 
	            System.out.println("\nSending 'POST' request to URL : " + RequestUrl);
	            System.out.println("Response Code : " + responseCode);
	 
	            // JSON ?????? ????????? ??????
	            ObjectMapper mapper = new ObjectMapper();
	            returnNode = mapper.readTree(response.getEntity().getContent());
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            // clear resources
	        }
	 
	        return returnNode;
	    }

	 //????????? ????????? ->???????????????????????????
	 public static JsonNode getKakaoUserInfo1(JsonNode accessToken) {
		 
	        final String RequestUrl = "https://kapi.kakao.com/v2/user/me";
	       
	        final HttpClient client = HttpClientBuilder.create().build();
	        final HttpPost post = new HttpPost(RequestUrl);
	 
	        // add header
	        post.addHeader("Authorization", "Bearer " + accessToken);
//	        post.addHeader("Authorization", "KakaoAK " + "8c89186a433ca2d33527288541e96fef");
	        JsonNode returnNode = null;
	 
	        try {
	            final HttpResponse response = client.execute(post);
	            final int responseCode = response.getStatusLine().getStatusCode();
	 
	            System.out.println("\nSending 'POST' request to URL : " + RequestUrl);
	            System.out.println("Response Code : " + responseCode);
	 
	            // JSON ?????? ????????? ??????
	            ObjectMapper mapper = new ObjectMapper();
	            returnNode = mapper.readTree(response.getEntity().getContent());
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            // clear resources
	        }
	 
	        return returnNode;
	    }

	

}
