package guru.qa.niffler.db.dao;

import guru.qa.niffler.db.model.userdata.UserDataUserEntity;

import java.util.UUID;

public interface UserDataUserDAO {

	int createUserInUserData(UserDataUserEntity user);

	void deleteUserByIdInUserData(UUID userId);

	UserDataUserEntity updateUserInUserData(UserDataUserEntity user);

	UserDataUserEntity getUserData(String username);
}
