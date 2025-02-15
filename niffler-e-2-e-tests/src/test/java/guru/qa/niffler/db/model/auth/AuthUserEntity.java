package guru.qa.niffler.db.model.auth;

import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.model.UserJson;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static guru.qa.niffler.db.model.CurrencyValues.RUB;
import static jakarta.persistence.FetchType.EAGER;

@Entity
@Table(name = "\"user\"")
public class AuthUserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
	private UUID id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Boolean enabled;

	@Column(name = "account_non_expired", nullable = false)
	private Boolean accountNonExpired;

	@Column(name = "account_non_locked", nullable = false)
	private Boolean accountNonLocked;

	@Column(name = "credentials_non_expired", nullable = false)
	private Boolean credentialsNonExpired;

	@OneToMany(fetch = EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	private List<AuthorityEntity> authorities = new ArrayList<>();

	public AuthUserEntity() {
	}

	public AuthUserEntity(AuthUserEntity user) {
		this.id = user.id;
		this.username = user.username;
		this.password = user.password;
		this.enabled = user.enabled;
		this.accountNonExpired = user.accountNonExpired;
		this.accountNonLocked = user.accountNonLocked;
		this.credentialsNonExpired = user.credentialsNonExpired;
		this.authorities = new ArrayList<>();
		for (AuthorityEntity authority : user.getAuthorities()) {
			AuthorityEntity newAuthority = new AuthorityEntity(authority);
			newAuthority.setUser(this); // set the back-reference
			this.authorities.add(newAuthority);
		}
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Boolean getCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public List<AuthorityEntity> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<AuthorityEntity> authorities) {
		this.authorities = authorities;
	}

	public void addAuthorities(AuthorityEntity... authorities) {
		this.authorities.addAll(List.of(authorities));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AuthUserEntity that = (AuthUserEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(enabled, that.enabled) && Objects.equals(accountNonExpired, that.accountNonExpired) && Objects.equals(accountNonLocked, that.accountNonLocked) && Objects.equals(credentialsNonExpired, that.credentialsNonExpired) && Objects.equals(authorities, that.authorities);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username, password, enabled, accountNonExpired, accountNonLocked, credentialsNonExpired, authorities);
	}

	public static AuthUserEntity authUserEntityFromUserJson(UserJson user) {
		AuthUserEntity entity = new AuthUserEntity();
		entity.setId(user.getId());
		entity.setUsername(user.getUsername());
		return entity;
	}

	public static UserDataUserEntity toUserDataUserEntity (AuthUserEntity authUserEntity) {
		UserDataUserEntity entity = new UserDataUserEntity();
		entity.setUsername(authUserEntity.getUsername());
		entity.setCurrency(RUB);
		return entity;
	}
}
