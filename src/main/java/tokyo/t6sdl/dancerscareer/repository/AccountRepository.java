package tokyo.t6sdl.dancerscareer.repository;

import java.util.List;

import tokyo.t6sdl.dancerscareer.model.Account;

public interface AccountRepository {
	List<Account> find();
	Account findOneByEmail(String email);
	Account findOneByEmailToken(String emailToken);
	Account findOneByPasswordToken(String passwordToken);
	String findEmailTokenByEmail(String email);
	String findPasswordTokenByEmail(String email);
	String findLineAccessTokenByEmail(String email);
	List<Account> findForMassMailBy(int by);
	void insert(Account newAccount);
	void delete(String email);
	void updateEmail(String loggedInEmail, String newEmail);
	void updatePassword(String loggedInEmail, String newPassword);
	void updateValidEmail(String loggedInEmail, boolean validEmail);
	void updateLastLogin(String loggedInEmail);
	void updateLineAccessToken(String loggedInEmail, String lineAccessToken);
	void updateNewEsMail(String loggedInEmail, boolean newEsMail);
	void recordEmailToken(String loggedEmail, String emailToken);
	void refreshEmailToken(String loggedInEmail);
	void recordPasswordToken(String loggedInEmail, String passwordToken);
	void refreshPasswordToken(String loggedInEmail);
}
