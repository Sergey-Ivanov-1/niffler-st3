package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.model.userdata.UserDataUserEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositorySpringJdbc;
import guru.qa.niffler.jupiter.annotation.Friend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.IncomeInvitation;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitation;
import guru.qa.niffler.model.UserJson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static guru.qa.niffler.model.UserJson.fromEntity;
import static guru.qa.niffler.util.FakerUtils.generateRandomUsername;

public class DbCreateUserExtension extends CreateUserExtension {

    @Override
    protected UserJson createUserForTest(GenerateUser annotation) {
        UserRepository userRepository = new UserRepositorySpringJdbc();
        AuthUserEntity authUser = getAuthUserEntity();
        userRepository.createUserForTest(authUser);
        UserJson result = fromEntity(authUser);
        result.setPassword(DEFAULT_PASSWORD);
        return result;
    }

    @Override
    protected List<UserJson> createFriendsIfPresent(GenerateUser annotation, UserJson currentUser) {
        UserRepository userRepository = new UserRepositorySpringJdbc();
        Friend friends = annotation.friends();
        List<UserJson> userJsonList = new ArrayList<>();
        if (friends.handleAnnotation()) {
            UserDataUserEntity currentUserEntity = userRepository
                    .getUserData(AuthUserEntity.authUserEntityFromUserJson(currentUser));
            for (int i = 0; i < friends.count(); i++) {
                AuthUserEntity authFriend = getAuthUserEntity();
                userRepository.createUserForTest(authFriend);
                UserDataUserEntity userDataFriendEntity = userRepository.getUserData(authFriend);
                currentUserEntity.addFriends(false, userDataFriendEntity);
                userDataFriendEntity.addFriends(false, currentUserEntity);
                userRepository.updateUserForTest(userDataFriendEntity);
                userRepository.updateUserForTest(currentUserEntity);
                userRepository.addFriendForUser(currentUserEntity, userDataFriendEntity);
                userJsonList.add(fromEntity(userDataFriendEntity));
            }
            userRepository.updateUserForTest(currentUserEntity);
        }
        return userJsonList;
    }

    @Override
    protected List<UserJson> createIncomeInvitationsIfPresent(GenerateUser annotation, UserJson currentUser) {
        UserRepository userRepository = new UserRepositorySpringJdbc();
        IncomeInvitation incomeInvitation = annotation.incomeInvitations();
        List<UserJson> userJsonList = new ArrayList<>();
        if (incomeInvitation.handleAnnotation()) {
            UserDataUserEntity currentUserEntity = userRepository
                    .getUserData(AuthUserEntity.authUserEntityFromUserJson(currentUser));
            for (int i = 0; i < incomeInvitation.count(); i++) {
                AuthUserEntity authFriend = getAuthUserEntity();
                userRepository.createUserForTest(authFriend);
                UserDataUserEntity userDataFriendEntity = userRepository.getUserData(authFriend);
                userDataFriendEntity.addFriends(true, currentUserEntity);
                userRepository.updateUserForTest(userDataFriendEntity);
                userRepository.updateUserForTest(currentUserEntity);
                userRepository.addInvitation(userDataFriendEntity, currentUserEntity);
                userJsonList.add(fromEntity(userDataFriendEntity));
            }
            userRepository.updateUserForTest(currentUserEntity);
        }
        return userJsonList;
    }

    @Override
    protected List<UserJson> createOutcomeInvitationsIfPresent(GenerateUser annotation, UserJson currentUser) {
        UserRepository userRepository = new UserRepositorySpringJdbc();
        OutcomeInvitation outcomeInvitation = annotation.outcomeInvitations();
        List<UserJson> userJsonList = new ArrayList<>();
        if (outcomeInvitation.handleAnnotation()) {
            UserDataUserEntity currentUserEntity = userRepository
                    .getUserData(AuthUserEntity.authUserEntityFromUserJson(currentUser));
            for (int i = 0; i < outcomeInvitation.count(); i++) {
                AuthUserEntity authFriend = getAuthUserEntity();
                userRepository.createUserForTest(authFriend);
                UserDataUserEntity userDataFriendEntity = userRepository.getUserData(authFriend);
                currentUserEntity.addFriends(true, userDataFriendEntity);
                userRepository.updateUserForTest(userDataFriendEntity);
                userRepository.updateUserForTest(currentUserEntity);
                userRepository.addInvitation(currentUserEntity, userDataFriendEntity);
                userJsonList.add(fromEntity(userDataFriendEntity));
            }
        }
        return userJsonList;
    }


    private AuthUserEntity getAuthUserEntity() {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(generateRandomUsername());
        authUser.setPassword(DEFAULT_PASSWORD);
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(new ArrayList<>(Arrays.stream(Authority.values())
                .map(a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    ae.setUser(authUser);
                    return ae;
                }).toList()));
        return authUser;
    }
}