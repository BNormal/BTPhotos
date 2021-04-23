package com.utils;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

public class Email {
	private String address = "";
	private String subject = "Thank you for using BT Camera Controller!";
	private String message = "Attached below are your photo(s)!";
	private ArrayList<String> attachments = new ArrayList<String>();

	public Email(String address) {
		this.address = address;
	}

	public Email(String address, String subject) {
		this.address = address;
		this.subject = subject;
	}

	public Email(String address, String subject, String message) {
		this.address = address;
		this.subject = subject;
		this.message = message;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ArrayList<String> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<String> attachments) {
		this.attachments = attachments;
	}

	public void addAttachment(String attachment) {
		this.attachments.add(attachment);
	}

	public void removeAttachment(String attachment) {
		this.attachments.remove(attachment);
	}

	public void clearAttachments() {
		this.attachments.clear();
	}

	public void send() {
		try {
			JavaMail.sendEmailWithAttachments(address, subject, message, attachments);
			//System.out.println("Your pictures have been sent!");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public boolean verifyEmail() {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (address == null)
			return false;
		return pat.matcher(address).matches();
	}

}
