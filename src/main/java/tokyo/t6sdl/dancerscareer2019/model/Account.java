package tokyo.t6sdl.dancerscareer2019.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class Account implements UserDetails {
	private String email;
	private String password;
	private String authority;
	private boolean validEmail;
	private LocalDateTime lastLogin;
	private LocalDateTime updatedAt;
	private LocalDateTime createdAt;
	private String emailToken;
	private String passwordToken;
	private String lineAccessToken;
	private boolean esUpdateNotification;
	
	public boolean isAdmin() {
		return this.getAuthority().equals("ROLE_ADMIN");
	}
	
	public boolean needsEsUpdateNotification() {
		return this.esUpdateNotification;
	}
		
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(authority));
		return authorities;
	}

	@Override
	public String getUsername() {
		return getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}