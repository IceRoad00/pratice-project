package com.estate.back.service.implementation;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.estate.back.common.object.CustomOAuth2User;
import com.estate.back.entitiy.EmailAuthNumberEntity;
import com.estate.back.entitiy.UserEntity;
import com.estate.back.repository.EmailAuthNumberRepository;
import com.estate.back.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Oauth2UserServiceImplementaion extends DefaultOAuth2UserService{

    private final UserRepository userRepository;
    private final EmailAuthNumberRepository emailAuthNumberRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 부모에 있는 결과를 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 어떤 요청이 들어온건지 판단을 해줌
        String oauthClientName = userRequest.getClientRegistration().getClientName().toUpperCase();

        // System.out.println(oauthClientName);

        // try {

        //     System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes())
        //     );

        // } catch(Exception exception) {
        //     exception.printStackTrace();
        // }

        // 네이버와 카카오를 만든 유저에 넣음
        String id = getId(oAuth2User, oauthClientName);
        
        //KAKAO_
        //NAVER_
        String userId = oauthClientName + '_' + id.substring(0, 10);

        boolean isExistUser = userRepository.existsByUserId(userId);
        if(!isExistUser) {
            String password = passwordEncoder.encode(id);
            
            String email = id + "@" + oauthClientName.toLowerCase() + ".com";
            EmailAuthNumberEntity emailAuthNumberEntity = new EmailAuthNumberEntity(email, "0000");
            emailAuthNumberRepository.save(emailAuthNumberEntity);

            UserEntity userEntity = new UserEntity(userId, password, email, "ROLE_USER", oauthClientName);
            userRepository.save((userEntity));
        }

        return new CustomOAuth2User(userId, oAuth2User.getAttributes());
    }
    
    // 카카오인지 네이버인지 비교하여 가져오게 만들어줌
    private String getId(OAuth2User oAuth2User, String oauthClientName) {
        String id = null;

        if(oauthClientName.equals("KAKAO")) {
            Long LongId = (Long) oAuth2User.getAttributes().get("id");
            id = LongId.toString();
        }

        if(oauthClientName.equals("NAVER")) {
            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
            id = responseMap.get("id");
        }

        return id;

    }
}
