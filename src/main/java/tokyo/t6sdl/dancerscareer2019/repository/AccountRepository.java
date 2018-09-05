package tokyo.t6sdl.dancerscareer2019.repository;

import java.util.List;

import tokyo.t6sdl.dancerscareer2019.model.Account;

public interface AccountRepository {
	List<Account> find();
	Account findOneByEmail(String email);
	Account findOneByEmailToken(String emailToken);
	Account findOneByPasswordToken(String passwordToken);
	void insert(Account newAccount);
	void delete(String email);
	void updateEmail(String loggedInEmail, String newEmail);
	void updatePassword(String loggedInEmail, String newPassword);
	void updateValidEmail(String loggedInEmail, boolean validEmail);
	void recordEmailToken(String loggedEmail, String emailToken);
	void refreshEmailToken(String loggedInEmail);
	void recordPasswordToken(String loggedInEmail, String passwordToken);
	void refreshPasswordToken(String loggedInEmail);
}