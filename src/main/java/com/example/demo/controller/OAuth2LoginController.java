package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;

import com.example.demo.service.Oauth2TokenService;

@Controller
public class OAuth2LoginController {
	
	private boolean invalidateHttpSession = true;
	private boolean clearAuthentication = true;

	@Autowired
	Oauth2TokenService tokenService;

	@GetMapping("/")
	public String index(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal OAuth2User oauth2User) {
		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		if(sessionId!=null)
		{
			model.addAttribute("userName", oauth2User.getName());
			model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
			model.addAttribute("userAttributes", oauth2User.getAttributes());
			return "index";
		}
		return "redirect:/login";
	}
	
	@GetMapping("/success")
	public String sucess(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal OAuth2User oauth2User) {

		model.addAttribute("userName", oauth2User.getName());
		model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
		model.addAttribute("userAttributes", oauth2User.getAttributes());
		return "index";
	}

	@PostMapping("/logout")
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		
		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

		System.out.println(sessionId);
		
		if (invalidateHttpSession) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
		}

		if (clearAuthentication) {
			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(null);
		}

		return "redirect:/login";
	}
	public boolean isInvalidateHttpSession() {
		return invalidateHttpSession;
	}

	/**
	 * Causes the {@link HttpSession} to be invalidated when this {@link LogoutHandler} is
	 * invoked. Defaults to true.
	 *
	 * @param invalidateHttpSession true if you wish the session to be invalidated
	 * (default) or false if it should not be.
	 */
	public void setInvalidateHttpSession(boolean invalidateHttpSession) {
		this.invalidateHttpSession = invalidateHttpSession;
	}

	/**
	 * If true, removes the {@link Authentication} from the {@link SecurityContext} to
	 * prevent issues with concurrent requests.
	 *
	 * @param clearAuthentication true if you wish to clear the {@link Authentication}
	 * from the {@link SecurityContext} (default) or false if the {@link Authentication}
	 * should not be removed.
	 */
	public void setClearAuthentication(boolean clearAuthentication) {
		this.clearAuthentication = clearAuthentication;
	}
}
