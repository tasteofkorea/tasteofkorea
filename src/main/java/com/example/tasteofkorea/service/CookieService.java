package com.example.tasteofkorea.service;

import org.springframework.stereotype.Service;
import jakarta.servlet.http.Cookie;

@Service
public class CookieService {

	private static final int DEFAULT_MAX_AGE = 60 * 60; // 1시간

	public Cookie createCookie(String name, String value, int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		return cookie;
	}

	public Cookie expireCookie(String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge(0); // 즉시 만료
		return cookie;
	}
}
