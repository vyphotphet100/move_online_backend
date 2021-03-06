package com.move_up.service.management.impl;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.move_up.utils.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import com.move_up.dto.UserDTO;
import com.move_up.entity.MissionEntity;
import com.move_up.entity.UserEntity;
import com.move_up.repository.MissionRepository;
import com.move_up.repository.UserRepository;
import com.move_up.service.management.IUserService;
import com.move_up.utils.JwtUtil;

@Service
public class UserService extends BaseService implements IUserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MissionRepository missionRepo;

    @Override
    public UserDTO findAll() {
        UserDTO userDto = new UserDTO();
        List<UserEntity> userEntities = userRepo.findAll();

        if (!userEntities.isEmpty()) {
            for (UserEntity userEntity : userEntities)
                userDto.getListResult().add(this.converter.toDTO(userEntity, UserDTO.class));
            userDto.setMessage("Load user list successfully.");
            return userDto;
        }

        return (UserDTO) this.ExceptionObject(userDto, "There is no user.");
    }

    @Override
    public UserDTO findOne(String username, HttpServletRequest request, String userStr) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng t???n t???i ng?????i d??ng n??y.");

        UserDTO userDto = new UserDTO();
        UserEntity userEntity = userRepo.findById(username).get();

        if (userEntity != null) {
            if (userStr.contains("requestedRole=user")) {
                if (requestedUserEntity == null ||
                        !userEntity.getUsername().equals(requestedUserEntity.getUsername()))
                    return (UserDTO) this.ExceptionObject(userDto, "Access Denied");
            }

            userDto = this.converter.toDTO(userEntity, UserDTO.class);
            userDto.setMessage("Get user having username = " + username + " successfully.");
            return userDto;
        }

        return (UserDTO) this.ExceptionObject(userDto, "User does not exist.");
    }

    @Override
    public UserDTO save(UserDTO userDto) {
        UserEntity userEntityTmp = userRepo.findById(userDto.getUsername()).orElse(null);
        if (userEntityTmp != null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "T??n ????ng nh???p n??y ???? t???n t???i.");

        if (userDto.getUsername() == null || userDto.getUsername().trim() == "")
            return (UserDTO) this.ExceptionObject(new UserDTO(), "T??n ????ng nh???p kh??ng ???????c b??? tr???ng.");

        if (userDto.getPassword() == null || userDto.getPassword().trim() == "")
            return (UserDTO) this.ExceptionObject(new UserDTO(), "M???t kh???u kh??ng ???????c b??? tr???ng.");

        if (userDto.getFullname() == null || userDto.getFullname().trim() == "")
            return (UserDTO) this.ExceptionObject(new UserDTO(), "M???t kh???u kh??ng ???????c b??? tr???ng.");

        if (userDto.getUsername().trim().length() < 4)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "T??n ????ng nh???p ph???i c?? ??t nh???t 4 k?? t???.");

        if (userDto.getPassword().trim().length() < 6)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "M???t kh???u ph???i c?? ??t nh???t 6 k?? t???.");

        if (userDto.getReferrerUsername() != null && userDto.getReferrerUsername().trim() != "") {
            if (userRepo.findById(userDto.getReferrerUsername()) == null)
                return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng t???n t???i m?? ng?????i gi???i thi???u n??y.");
        }

        userDto.getRoleCodes().add("USER");
        UserEntity userEntity = this.converter.toEntity(userDto, UserEntity.class);
        userEntity.setTokenCode(JwtUtil.generateToken(userDto));
        if (userEntity.getReferrerUser() != null)
            userEntity.setAccountBalance(1000);
        else
            userEntity.setAccountBalance(0);
        userEntity.setCommission(0);
        userEntity.setNumOfCoinGiftBox(0);
        userEntity.setNumOfDefaultTime(0);
        userEntity.setNumOfStar(0);
        userEntity.setNumOfTimeGiftBox(0);
        userEntity.setNumOfTravelledTime(0);
        userEntity = userRepo.save(userEntity);

        userDto = this.converter.toDTO(userEntity, UserDTO.class);
        userDto.setPassword(null);
        userDto.setMessage("????ng k?? th??nh c??ng.");
        return userDto;
    }

    @Override
    public UserDTO update(UserDTO userDto, HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng t???n t???i ng?????i d??ng n??y.");

        // check if phone number is in true format
        if (userDto.getPhoneNumber() != null && userDto.getPhoneNumber().trim() != "") {
            if (userDto.getPhoneNumber().length() != 10)
                return (UserDTO) this.ExceptionObject(new UserDTO(), "S??? ??i???n tho???i kh??ng h???p l???.");
            for (char ch : userDto.getPhoneNumber().toCharArray())
                if (!Character.isDigit(ch))
                    return (UserDTO) this.ExceptionObject(new UserDTO(), "S??? ??i???n tho???i kh??ng h???p l???.");
        }

        // check if email is in true format
        if (userDto.getEmail() != null && userDto.getEmail().trim() != "")
            if (!MyUtil.isEmailValid(userDto.getEmail()))
                return (UserDTO) this.ExceptionObject(new UserDTO(), "Email kh??ng h???p l??.");

        // check email and phone number exist
        if (userDto.getEmail() != null && userDto.getPhoneNumber() != null &&
                userDto.getEmail().trim() != "" && userDto.getPhoneNumber().trim() != "") {
            List<UserEntity> userEntities = userRepo.findAll();
            for (UserEntity userEntity : userEntities) {
                if (!userEntity.getUsername().equals(requestedUserEntity.getUsername()) &&
                        userDto.getEmail().equals(userEntity.getEmail()))
                    return (UserDTO) this.ExceptionObject(new UserDTO(), "Email n??y ???? t???n t???i tr??n h??? th???ng.");

                if (!userEntity.getUsername().equals(requestedUserEntity.getUsername()) &&
                        userDto.getPhoneNumber().equals(userEntity.getPhoneNumber()))
                    return (UserDTO) this.ExceptionObject(new UserDTO(), "S??? ??i???n tho???i n??y ???? t???n t???i tr??n h??? th???ng.");
            }
        }

        requestedUserEntity.setFullname(userDto.getFullname());
        requestedUserEntity.setEmail(userDto.getEmail());
        requestedUserEntity.setPhoneNumber(userDto.getPhoneNumber());
        requestedUserEntity.setAddress(userDto.getAddress());
        UserEntity userEntity = userRepo.save(requestedUserEntity);
        userDto = this.converter.toDTO(userEntity, UserDTO.class);
        userDto.setMessage("C???p nh???t th??ng tin ng?????i d??ng th??nh c??ng.");
        return userDto;
    }

    @Override
    public UserDTO delete(String username) {
        UserDTO userDto = new UserDTO();
        if (userRepo.findById(username) != null) {
            userRepo.deleteById(username);
            userDto.setMessage("Delete user successfully.");
            return userDto;
        }

        return (UserDTO) this.ExceptionObject(userDto, "This username does not exist.");
    }

    @Override
    public UserDTO logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDTO userDto = new UserDTO();
        try {
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            userDto.setMessage("Logout successfully.");
            return userDto;
        } catch (Exception ex) {
            return (UserDTO) this.ExceptionObject(userDto, "Something went wrong.");
        }
    }

    @Override
    public UserDTO changePassword(UserDTO userDto, HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng t???n t???i ng?????i d??ng n??y.");

        // check old password
        // listRequest[0] = old pass
        // listRequest[1] = new pass
        if (userDto.getListRequest().size() != 2)
            return (UserDTO) this.ExceptionObject(userDto, "C?? l???i x???y ra.");

        UserEntity userEntity = userRepo.findById(userDto.getUsername()).get();
        if (!userEntity.getUsername().equals(userDto.getUsername()))
            return (UserDTO) this.ExceptionObject(userDto, "C?? l???i x???y ra.");

        if (!userEntity.getPassword().equals(userDto.getListRequest().get(0)))
            return (UserDTO) this.ExceptionObject(userDto, "M???t kh???u hi???n t???i ch??a ch??nh x??c. H??y ki???m tra l???i.");

        if (((String) userDto.getListRequest().get(1)).trim().equals("") || userDto.getListRequest().get(1) == null)
            return (UserDTO) this.ExceptionObject(userDto, "M???t kh???u m???i kh??ng ???????c ch???p nh???n. Vui l??ng ch???n m???t kh???u kh??c.");

        if (((String) userDto.getListRequest().get(1)).length() < 6)
            return (UserDTO) this.ExceptionObject(userDto, "M???t kh???u m???i ph???i c?? t??? 6 k?? t??? tr??? l??n.");

        if (userEntity.getPassword().equals(userDto.getListRequest().get(1)))
            return (UserDTO) this.ExceptionObject(userDto, "M???t kh???u m???i kh??ng ???????c gi???ng v???i m???t kh???u hi???n t???i.");

        // change password
        userEntity.setPassword((String) userDto.getListRequest().get(1));
        userEntity = userRepo.save(userEntity);
        userDto = this.converter.toDTO(userEntity, UserDTO.class);
        userDto.setMessage("Thay ?????i m???t kh???u th??nh c??ng.");
        return userDto;
    }

    @Override
    public UserDTO checkFacebookAccByPostLink(UserDTO userDto, HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng t???n t???i ng?????i d??ng n??y.");

        // GET CONTENT OF POST
        if (userDto.getListRequest().size() != 1)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "C?? l???i x???y ra.");
        String postLink = (String) userDto.getListRequest().get(0);
        postLink = postLink.replace("www", "m");
        postLink = postLink.replace("mbasic", "m");
        String contentOfPost = MyUtil.getDocumentFromUrl(postLink);
        if (contentOfPost == null)
            return (UserDTO) this.ExceptionObject(userDto, "C?? l???i x???y ra.");

        // get facebook link
        if (!contentOfPost.contains("<link rel=\"canonical\" href=\""))
            return (UserDTO) this.ExceptionObject(userDto, "Kh??ng t??m th???y th??ng tin t??i kho???n trong b??i vi???t. H??y ki???m tra l???i.");
        StringBuilder facebookLink = new StringBuilder();
        int _countSlash = 0;
        for (int i = contentOfPost.indexOf("<link rel=\"canonical\" href=\"") + 28; _countSlash < 4; i++) {
            if (contentOfPost.charAt(i) == '/')
                _countSlash++;
            facebookLink.append(contentOfPost.charAt(i));
        }
        System.out.println(facebookLink.toString());

        // get facebook name
        if (!contentOfPost.contains("<title>"))
            return (UserDTO) this.ExceptionObject(userDto, "Kh??ng t??m th???y th??ng tin t??i kho???n trong b??i vi???t. H??y ki???m tra l???i.");
        StringBuilder facebookName = new StringBuilder();
        for (int i = contentOfPost.indexOf("<title>") + 7; true; i++) {
            if (contentOfPost.charAt(i) == '-')
                break;
            facebookName.append(contentOfPost.charAt(i));
        }
        System.out.println(facebookName.toString());


        // get link of avatar
        int _countQuotes = 0;
        StringBuilder avatarLink = new StringBuilder();
        for (int i = contentOfPost.indexOf("og:image"); _countQuotes < 3; i++) {
            if (_countQuotes == 2 && contentOfPost.charAt(i) != '\"')
                avatarLink.append(contentOfPost.charAt(i));

            if (contentOfPost.charAt(i) == '\"')
                _countQuotes++;
        }
        String avatarLinkStr = avatarLink.toString();
        while (avatarLinkStr.contains("amp;"))
            avatarLinkStr = avatarLinkStr.replace("&amp;", "&");

        System.out.println(avatarLinkStr);

        UserDTO resDto = new UserDTO();
        resDto.getListResult().add(facebookLink.toString().replace("mbasic", "www"));
        resDto.getListResult().add(facebookName.toString());
        resDto.getListResult().add(avatarLinkStr.replace("mbasic", "www"));
        resDto.setMessage("X??c nh???n th??ng tin t??i kho???n th??nh c??ng.");
        return resDto;
    }

    @Override
    public UserDTO saveFacebookAccByPostLink(UserDTO userDto, HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng t???n t???i ng?????i d??ng n??y.");

        // listRequest[0] = post link
        // listRequest[1] = facebook link
        // listRequest[2] = avatar link
        // listRequest[3] = facebook name

        // check time
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        if (requestedUserEntity.getFacebookLink() != null) {
            if (calendar.get(Calendar.DAY_OF_WEEK) != 1 || now.getHours() != 8)
                return (UserDTO) this.ExceptionObject(new UserDTO(), "H??? th???ng ch??? cho ph??p thay ?????i t??i kho???n Facebook l??m vi???c v??o kho???ng th???i gian 8AM - 9AM ng??y Ch??? nh???t.");
        }

        if (userDto.getListRequest().size() != 4)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "C?? l???i x???y ra.");

        // get facebook id
        int _countSlash = 0;
        StringBuilder facebookId = new StringBuilder();
        for (int i = 0; i < ((String) userDto.getListRequest().get(1)).length(); i++) {
            if (_countSlash == 3 && ((String) userDto.getListRequest().get(1)).charAt(i) != '/')
                facebookId.append(((String) userDto.getListRequest().get(1)).charAt(i));

            if (((String) userDto.getListRequest().get(1)).charAt(i) == '/')
                _countSlash++;
        }

        List<UserEntity> userEntities = userRepo.findAll();
        for (UserEntity userEntity : userEntities) {
            if (userEntity.getFacebookLink() != null && userEntity.getFacebookLink().contains(facebookId))
                return (UserDTO) this.ExceptionObject(userDto, "Facebook n??y ???? t???n t???i tr??n h??? th???ng.");
        }


        String postLink = (String) userDto.getListRequest().get(0);
        postLink = postLink.replace("www", "mbasic");
        String contentOfPost = MyUtil.getDocumentFromUrl(postLink);
        if (contentOfPost == null)
            return (UserDTO) this.ExceptionObject(userDto, "C?? l???i x???y ra.");

        if (!contentOfPost.contains("X??c nh???n tham gia Move online: " + requestedUserEntity.getUsername()))
            return (UserDTO) this.ExceptionObject(userDto, "X??c nh???n n???i dung b??i vi???t kh??ng th??nh c??ng. H??y ki???m tra l???i.");

        UserEntity userEntity = userRepo.findById(requestedUserEntity.getUsername()).get();
        // download picture of facebook account
        try(InputStream in = new URL((String) userDto.getListRequest().get(2)).openStream()){
            Files.deleteIfExists(Paths.get("src/main/resources/static/picture/" + userEntity.getUsername() + ".jpg"));
            Files.copy(in, Paths.get("src/main/resources/static/picture/" + userEntity.getUsername() + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        userEntity.setFacebookLink((String) userDto.getListRequest().get(1));
        userEntity.setPicture("/api/user/picture/" + userEntity.getUsername());
        userEntity.setFacebookName((String) userDto.getListRequest().get(3));
        userDto = this.converter.toDTO(userRepo.save(userEntity), UserDTO.class);
        userDto.setPassword(null);
        userDto.setMessage("Th??m t??i kho???n Facebook l??m vi???c th??nh c??ng.");
        return userDto;
    }

    @Override
    public UserDTO getCurrentUser(HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng t???n t???i ng?????i d??ng n??y.");

        UserDTO userDto = this.converter.toDTO(requestedUserEntity, UserDTO.class);
        userDto.setPassword(null);
        return userDto;
    }

    @Override
    public UserDTO exchangeTimeGiftBoxByStar(HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng t???n t???i ng?????i d??ng n??y.");

        if (requestedUserEntity.getNumOfStar() < 10)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng ????? s??? sao ????? ?????i H???p qu?? th???i gian.");

        increaseTimeGiftBox(1, requestedUserEntity);
        requestedUserEntity.setNumOfStar(requestedUserEntity.getNumOfStar() - 10);
        requestedUserEntity = userRepo.save(requestedUserEntity);
        UserDTO userDto = this.converter.toDTO(requestedUserEntity, UserDTO.class);
        userDto.setMessage("?????i th??nh c??ng 1 h???p qu?? th???i gian.");
        return userDto;
    }

    @Override
    public UserDTO exchangeCoinGiftBoxByStar(HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng t???n t???i ng?????i d??ng n??y.");

        if (requestedUserEntity.getNumOfStar() < 20)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng ????? s??? sao ????? ?????i H???p qu?? xu.");

        increaseCoinGiftBox(1, requestedUserEntity);
        requestedUserEntity.setNumOfStar(requestedUserEntity.getNumOfStar() - 20);
        requestedUserEntity = userRepo.save(requestedUserEntity);
        UserDTO userDto = this.converter.toDTO(requestedUserEntity, UserDTO.class);
        userDto.setMessage("?????i th??nh c??ng 1 h???p qu?? xu.");
        return userDto;
    }

    private void increaseTimeGiftBox(int numOfTimeGiftBox, UserEntity userEntity) {
        userEntity.setNumOfTimeGiftBox(userEntity.getNumOfTimeGiftBox() + numOfTimeGiftBox);
        userRepo.save(userEntity);
    }

    private void increaseCoinGiftBox(int numOfCoinGiftBox, UserEntity userEntity) {
        userEntity.setNumOfCoinGiftBox(userEntity.getNumOfCoinGiftBox() + numOfCoinGiftBox);
        userRepo.save(userEntity);
    }

    @Override
    public UserDTO exchangeShirtByStar(HttpServletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserDTO getReferredUser(HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng t???n t???i ng?????i d??ng n??y.");

        UserDTO resDto = new UserDTO();
        List<UserEntity> referredUsers = userRepo.findAllByReferrerUserUsername(requestedUserEntity.getUsername());
        for (UserEntity userEntity : referredUsers) {
            UserDTO userDto = new UserDTO();
            userDto.setUsername(userEntity.getUsername());
            userDto.setCommission(userEntity.getCommission());
            resDto.getListResult().add(userDto);
        }

        resDto.setMessage("Load referred-user successfully.");
        return resDto;
    }

    @Override
    public UserDTO saveReferrerUser(HttpServletRequest request, UserDTO userDto) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng t???n t???i ng?????i d??ng n??y.");

        if (requestedUserEntity.getReferrerUser() != null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Kh??ng th??? thay ?????i m?? ng?????i gi???i thi???u.");

        // listRequest[0] = referrer code
        if (userDto.getListRequest().size() != 1)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "C?? l???i x???y ra.");
        String referrerCode = (String) userDto.getListRequest().get(0);

        if (referrerCode.trim().equals(""))
            return (UserDTO) this.ExceptionObject(new UserDTO(), "C?? l???i x???y ra.");

        if (requestedUserEntity.getUsername().equals(referrerCode))
            return (UserDTO) this.ExceptionObject(new UserDTO(), "B???n kh??ng th??? t??? th??m m?? ng?????i gi???i thi???u c???a ch??nh m??nh.");

        UserEntity referrerUserEntity = userRepo.findById(referrerCode).get();
        if (referrerUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "M?? ng?????i gi???i thi???u kh??ng t???n t???i.");

        requestedUserEntity.setReferrerUser(referrerUserEntity);
        requestedUserEntity.setAccountBalance(requestedUserEntity.getAccountBalance() + 1000);
        userRepo.save(requestedUserEntity);
        UserDTO resDto = new UserDTO();
        resDto.setMessage("Th??m m?? ng?????i gi???i thi???u th??nh c??ng. B???n ???? nh???n ???????c 1000 xu.");
        return resDto;
    }

    @Override
    public UserDTO checkReferrerExist(UserDTO userDto) {
        if (userRepo.findById(userDto.getUsername()) == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Ng?????i gi???i thi???u kh??ng t???n t???i.");

        UserDTO resDto = new UserDTO();
        resDto.setMessage("Ng?????i gi???i thi???u t???n t???i.");
        return resDto;
    }

}
