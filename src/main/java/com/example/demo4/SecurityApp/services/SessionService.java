package com.example.demo4.SecurityApp.services;

import com.example.demo4.SecurityApp.entities.Session;
import com.example.demo4.SecurityApp.entities.User;
import com.example.demo4.SecurityApp.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

//    private final SessionRepository sessionRepository;
//    private final int SESSION_LIMIT = 2;
//
//    public void generateNewSession(User user, String refreshToken) {
//        List<Session> userSessions = sessionRepository.findByUser(user);
//        if (userSessions.size() == SESSION_LIMIT) {
//            userSessions.sort(Comparator.comparing(Session::getLastUsedAt));
//
//            Session leastRecentlyUsedSession = userSessions.getFirst();
//            sessionRepository.delete(leastRecentlyUsedSession);
//        }
//
//        Session newSession = Session.builder()
//                .user(user)
//                .refreshToken(refreshToken)
//                .build();
//        sessionRepository.save(newSession);
//    }
//
//    public void validateSession(String refreshToken) {
//        Session session = sessionRepository.findByRefreshToken(refreshToken)
//                .orElseThrow(() -> new SessionAuthenticationException("Session not found for refreshToken: "+refreshToken));
//        session.setLastUsedAt(LocalDateTime.now());
//        sessionRepository.save(session);
//    }
    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT=2;
    public void generateNewSession(User user,String refreshToken){
        List<Session>  userSessions=sessionRepository.findByUser(user);
        //above will return all the session related to the passed user
        if(userSessions.size()==SESSION_LIMIT){
            userSessions.sort(Comparator.comparing(Session::getLastUsedAt));
//            according to the last used we are getting sorted usersession so when we pick the first session it will be the recently used
            Session leastRecentlyUsedSession = userSessions.getFirst();
            sessionRepository.delete(leastRecentlyUsedSession);
        }
        Session newSession=Session.builder().user(user).refreshToken(refreshToken).build();
        sessionRepository.save(newSession);

    }


    public void validateSession(String refreshToken){
//        I am going to check the if there is a session according to this refreshToken is present in my database or not
//        if we do not check like this then if any hacker hack our refresh toke from browser cookie the they can easily
//        get access token and hack our system without login in.

      Session session=  sessionRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new SessionAuthenticationException("Session not found for refreshToken: "+refreshToken));
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);


    }

    public void logOut(Long userId,String refreshToken) {
        Session  userSessions=sessionRepository.findByUserIdAndRefreshToken(userId,refreshToken).get();
        sessionRepository.delete(userSessions);
    }
}
