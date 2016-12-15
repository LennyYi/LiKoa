package com.aiait.eflow.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class EmailUtil {
	private String copyTo = "";
	private String to = "";       //the direction email address
	private String from = "";     //the send email address
	private String host = "";     //smtp host
	private String username ="";  //
	private String password = "";
	private String filename = ""; //attacth file name
	
	private String subject;       //email subject
	private String content = "";  //email content
	
	final static String SS = "tt"; 
	
	Vector file = new Vector();
	
    /**
     * default ????
     *
     */
	public EmailUtil(){
	}
	
	/**
	 * ???????
	 * @param to
	 * @param from
	 * @param smtpServer
	 * @param username
	 * @param password
	 * @param subject
	 * @param content
	 */
	public EmailUtil(String to,String from,String smtpServer,String username,String password,String subject,String content){
	  this.to = to;
	  this.from = from;
	  this.host = smtpServer;
	  this.username = username;
	  this.password = password;
	  this.subject = subject;
	  this.content = content;
	}

	/**
	 * set the smnt host
	 * @param host
	 */
	public void setHost(String host){
	  this.host = host;
	}

	/**
	 * set the email's password
	 * @param pwd
	 */
	public void setPassWord(String pwd){
	  this.password = pwd;
	}

	/**
	 * set the email's userName
	 * @param usn
	 */
	public void setUserName(String usn){
	  this.username = usn;
	}

	/**
	 * 
	 * @param to
	 */
	public void setTo(String to){
	  this.to = to;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getCopyTo() {
		return copyTo;
	}
	public void setCopyTo(String copyTo) {
		this.copyTo = copyTo;
	}

	/**
	 * 
	 * @param from
	 */
	public void setFrom(String from){
	  this.from = from;
	}

	/**
	 * 
	 * @param subject
	 */
	public void setSubject(String subject){
	  this.subject = subject;
	}

	/**
	 * 
	 * @param content
	 */
	public void setContent(String content){
	  this.content = content;
	}

	/**
	 * 
	 * @param strText
	 * @return
	 */
	public String transferChinese(String strText){
	  try{
	    strText = MimeUtility.encodeText(new String(strText.getBytes(), "GB2312"), "GB2312", "B");
	  }catch(Exception e){
	    e.printStackTrace();
	  }
	  return strText;
	}

	/**
	 * 
	 * @param fname
	 */
	public void attachfile(String fname){
	  file.addElement(fname);
	}

	/**
	 * Asynchronized send email method 
	 * @since 2013
	 * @return
	 */
	public void sendMailAsync(){
		final EmailUtil obj = this;
		new Thread(new Runnable() {  
            public void run() {  
            	obj.sendMail();
            }
        }).start();  
	}
	/**
	 * Main send email method
	 * @return
	 */
	public boolean sendMail(){
	  //??mail session
	  Properties props = System.getProperties();
	  System.out.println("host---:"+host);
	  props.put("mail.smtp.host",host);
	  props.put("mail.smtp.auth","false");
	 
	  //Session session=Session.getDefaultInstance(props, new Authenticator(){
	  // public PasswordAuthentication getPasswordAuthentication(){
	  //  return new PasswordAuthentication(username,password); 
	 //  }
	 // });
	  Session session = Session.getInstance(props,null);
	  try {
		  
	    MimeMessage msg = new MimeMessage(session);
	    
	    //msg.setHeader("Disposition-Notification-To",this.from);
	    
	    msg.setFrom(new InternetAddress(from));
	    InternetAddress[] address = parseAddressList(to);
	    msg.setRecipients(Message.RecipientType.TO,address);
	    subject = transferChinese(subject);
	    msg.setSubject(subject);
	    
	    //??Multipart
	    Multipart mp = new MimeMultipart();
	    
	    //?Multipart????
	    MimeBodyPart mbpContent = new MimeBodyPart();
	    mbpContent.setContent("<meta http-equiv=Content-Type content=text/html; charset=GBK>"+content,"text/html;charset=GBK");
	    //mbpContent.setText(content);
	    //?MimeMessage???Multipart?????
	    mp.addBodyPart(mbpContent);
	    
	    //?Multipart????
	    Enumeration efile=file.elements();
	    while(efile.hasMoreElements()){
	    
	      MimeBodyPart mbpFile = new MimeBodyPart();
	      filename=efile.nextElement().toString();
	      FileDataSource fds = new FileDataSource(filename);
	      mbpFile.setDataHandler(new DataHandler(fds));
	      mbpFile.setFileName(fds.getName());
	      //?MimeMessage???Multipart?????
	      mp.addBodyPart(mbpFile);
	    }
	    
	    file.removeAllElements();
	    //?Multipart??MimeMessage
	    msg.setContent(mp);
	    msg.setSentDate(new Date());
	    
	    //????
	    //Transport.send(msg);
	    Transport transport = session.getTransport("smtp");
	    transport.connect((String)props.get("mail.smtp.host"),username,password);
	    transport.sendMessage(msg,msg.getRecipients(Message.RecipientType.TO));
	    if(!"".equals(this.getCopyTo())){
	       msg.setRecipients(Message.RecipientType.CC,(Address[])InternetAddress.parse(this.getCopyTo()));
		   transport.sendMessage(msg,msg.getRecipients(Message.RecipientType.CC));
	    }
	    //transport.send(msg);

	    System.out.println("send over!");
	    transport.close();
	  
	  } catch (MessagingException mex) {
		System.out.println("----------happen error-----"+mex.getMessage());
	    mex.printStackTrace();
	    Exception ex = null;
	    if ((ex=mex.getNextException())!=null){
	      ex.printStackTrace();
	    }
	    return false;
	  }
	  return true;
	 }


	private InternetAddress[] parseAddressList(String list) throws AddressException {
        Vector v = new Vector();
        String token;
        for (StringTokenizer st = new StringTokenizer(list, ","); st.hasMoreTokens(); ) {
          token = st.nextToken().trim();

          v.addElement(new InternetAddress(token));
        }

        InternetAddress array[] = new InternetAddress[v.size()];
        v.copyInto(array);

        return array;
}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		// TODO Auto-generated method stub
        EmailUtil sendmail = new EmailUtil();
        sendmail.setHost("167.247.155.82");
        sendmail.setUserName("Robin-YZ.Hou");
        sendmail.setTo("Robin-YZ.Hou@aig.com");
        sendmail.setCopyTo("Ned-M.Zhang@aig.com");
        sendmail.setFrom("Robin-YZ.Hou@aig.com");
        sendmail.setSubject("Test Email");
        sendmail.setContent("This is a test email!????"); 
        //Mail sendmail = new Mail("http://www.jspcn.net/htmlnews/?mailto:dujiang@sricnet.com","du_jiang@sohu.com","smtp.sohu.com","du_jiang","31415926?","??","??????");
        //sendmail.attachfile("c:\test.txt");
        //sendmail.attachfile("DND.jar");
        sendmail.sendMail();
**/

        
	}
}
