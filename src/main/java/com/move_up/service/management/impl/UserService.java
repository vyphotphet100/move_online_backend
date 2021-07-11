package com.move_up.service.management.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.move_up.utils.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");

        UserDTO userDto = new UserDTO();
        UserEntity userEntity = userRepo.findOne(username);

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
        UserEntity userEntity = userRepo.findOne(userDto.getUsername());
        if (userEntity == null) {
            userEntity = this.converter.toEntity(userDto, UserEntity.class);
            userEntity.setTokenCode(JwtUtil.generateToken(userEntity));
            userEntity.setAccountBalance(0);
            userEntity.setCommission(0);
            userEntity.setNumOfCoinGiftBox(0);
            userEntity.setNumOfDefaultTime(0);
            userEntity.setNumOfStar(0);
            userEntity.setNumOfTimeGiftBox(0);
            userEntity.setNumOfTravelledTime(0);
            userEntity = userRepo.save(userEntity);

            userDto = this.converter.toDTO(userEntity, UserDTO.class);
            userDto.setMessage("Đăng ký thành công.");
            return userDto;
        }

        return (UserDTO) this.ExceptionObject(userDto, "Tên đăng nhập này đã tồn tại.");
    }

    @Override
    public UserDTO update(UserDTO userDto, HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");

        requestedUserEntity.setFullname(userDto.getFullname());
        requestedUserEntity.setEmail(userDto.getEmail());
        requestedUserEntity.setPhoneNumber(userDto.getPhoneNumber());
        requestedUserEntity.setAddress(userDto.getAddress());
        UserEntity userEntity = userRepo.save(requestedUserEntity);
        userDto = this.converter.toDTO(userEntity, UserDTO.class);
        userDto.setMessage("Cập nhật thông tin người dùng thành công.");
        return userDto;
    }

    @Override
    public UserDTO delete(String username) {
        UserDTO userDto = new UserDTO();
        if (userRepo.findOne(username) != null) {
            userRepo.delete(username);
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
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");

        // check old password
        // listRequest[0] = old pass
        // listRequest[1] = new pass
        if (userDto.getListRequest().size() != 2)
            return (UserDTO) this.ExceptionObject(userDto, "Có lỗi xảy ra.");

        UserEntity userEntity = userRepo.findOne(userDto.getUsername());
        if (!userEntity.getUsername().equals(userDto.getUsername()))
            return (UserDTO) this.ExceptionObject(userDto, "Có lỗi xảy ra.");

        if (!userEntity.getPassword().equals(userDto.getListRequest().get(0)))
            return (UserDTO) this.ExceptionObject(userDto, "Mật khẩu hiện tại chưa chính xác. Hãy kiểm tra lại.");

        if (((String) userDto.getListRequest().get(1)).trim().equals("") || userDto.getListRequest().get(1) == null)
            return (UserDTO) this.ExceptionObject(userDto, "Mật khẩu mới không được chấp nhận. Vui lòng chọn mật khẩu khác.");

        if (((String) userDto.getListRequest().get(1)).length() < 6)
            return (UserDTO) this.ExceptionObject(userDto, "Mật khẩu mới phải có từ 6 ký tự trở lên.");

        if (userEntity.getPassword().equals(userDto.getListRequest().get(1)))
            return (UserDTO) this.ExceptionObject(userDto, "Mật khẩu mới không được giống với mật khẩu hiện tại.");

        // change password
        userEntity.setPassword((String) userDto.getListRequest().get(1));
        userEntity = userRepo.save(userEntity);
        userDto = this.converter.toDTO(userEntity, UserDTO.class);
        userDto.setMessage("Thay đổi mật khẩu thành công.");
        return userDto;
    }

    @Override
    public UserDTO checkFacebookAccByPostLink(UserDTO userDto, HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");

        // GET CONTENT OF POST
        if (userDto.getListRequest().size() != 1)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Có lỗi xảy ra.");
        String postLink = (String) userDto.getListRequest().get(0);
        postLink = postLink.replace("www", "m");
        postLink = postLink.replace("mbasic", "m");
        String contentOfPost = MyUtil.getDocumentFromUrl(postLink);
        if (contentOfPost == null)
            return (UserDTO) this.ExceptionObject(userDto, "Có lỗi xảy ra.");

        // get facebook link
        if (!contentOfPost.contains("<link rel=\"canonical\" href=\""))
            return (UserDTO) this.ExceptionObject(userDto, "Không tìm thấy thông tin tài khoản trong bài viết. Hãy kiểm tra lại.");
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
            return (UserDTO) this.ExceptionObject(userDto, "Không tìm thấy thông tin tài khoản trong bài viết. Hãy kiểm tra lại.");
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
        resDto.setMessage("Xác nhận thông tin tài khoản thành công.");
        return resDto;
    }

    @Override
    public UserDTO saveFacebookAccByPostLink(UserDTO userDto, HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");

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
                return (UserDTO) this.ExceptionObject(new UserDTO(), "Hệ thống chỉ cho phép thay đổi tài khoản Facebook làm việc vào khoảng thời gian 8AM - 9AM ngày Chủ nhật.");
        }

        if (userDto.getListRequest().size() != 4)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Có lỗi xảy ra.");

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
                return (UserDTO) this.ExceptionObject(userDto, "Facebook này đã tồn tại trên hệ thống.");
        }


        String postLink = (String) userDto.getListRequest().get(0);
        postLink = postLink.replace("www", "mbasic");
        String contentOfPost = MyUtil.getDocumentFromUrl(postLink);
        if (contentOfPost == null)
            return (UserDTO) this.ExceptionObject(userDto, "Có lỗi xảy ra.");

        if (!contentOfPost.contains("Xác nhận tham gia Move online: " + requestedUserEntity.getUsername()))
            return (UserDTO) this.ExceptionObject(userDto, "Xác nhận nội dung bài viết không thành công. Hãy kiểm tra lại.");

        UserEntity userEntity = userRepo.findOne(requestedUserEntity.getUsername());
        userEntity.setFacebookLink((String) userDto.getListRequest().get(1));
        userEntity.setPicture((String) userDto.getListRequest().get(2));
        userEntity.setFacebookName((String) userDto.getListRequest().get(3));
        userDto = this.converter.toDTO(userRepo.save(userEntity), UserDTO.class);
        userDto.setPassword(null);
        userDto.setMessage("Thêm tài khoản Facebook làm việc thành công.");
        return userDto;
    }

    @Override
    public UserDTO getCurrentUser(HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");

        UserDTO userDto = this.converter.toDTO(requestedUserEntity, UserDTO.class);
        userDto.setPassword(null);
        return userDto;
    }

    @Override
    public UserDTO exchangeTimeGiftBoxByStar(HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");

        if (requestedUserEntity.getNumOfStar() < 10)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không đủ số sao để đổi Hộp quà thời gian.");

        increaseTimeGiftBox(1, requestedUserEntity);
        requestedUserEntity.setNumOfStar(requestedUserEntity.getNumOfStar() - 10);
        requestedUserEntity = userRepo.save(requestedUserEntity);
        UserDTO userDto = this.converter.toDTO(requestedUserEntity, UserDTO.class);
        userDto.setMessage("Đổi thành công 1 hộp quà thời gian.");
        return userDto;
    }

    @Override
    public UserDTO exchangeCoinGiftBoxByStar(HttpServletRequest request) {
        UserEntity requestedUserEntity = this.getRequestedUser(request);
        if (requestedUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");

        if (requestedUserEntity.getNumOfStar() < 20)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không đủ số sao để đổi Hộp quà xu.");

        increaseCoinGiftBox(1, requestedUserEntity);
        requestedUserEntity.setNumOfStar(requestedUserEntity.getNumOfStar() - 20);
        requestedUserEntity = userRepo.save(requestedUserEntity);
        UserDTO userDto = this.converter.toDTO(requestedUserEntity, UserDTO.class);
        userDto.setMessage("Đổi thành công 1 hộp quà xu.");
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
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");

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
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không tồn tại người dùng này.");

        if (requestedUserEntity.getReferrerUser() != null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Không thể thay đổi mã người giới thiệu.");

        // listRequest[0] = referrer code
        if (userDto.getListRequest().size() != 1)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Có lỗi xảy ra.");
        String referrerCode = (String) userDto.getListRequest().get(0);

        if (referrerCode.trim().equals(""))
            return (UserDTO)this.ExceptionObject(new UserDTO(), "Có lỗi xảy ra.");

        if (requestedUserEntity.getUsername().equals(referrerCode))
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Bạn không thể tự thêm mã người giới thiệu của chính mình.");

        UserEntity referrerUserEntity = userRepo.findOne(referrerCode);
        if (referrerUserEntity == null)
            return (UserDTO) this.ExceptionObject(new UserDTO(), "Mã người giới thiệu không tồn tại.");

        requestedUserEntity.setReferrerUser(referrerUserEntity);
        requestedUserEntity.setAccountBalance(requestedUserEntity.getAccountBalance() + 1000);
        userRepo.save(requestedUserEntity);
        UserDTO resDto = new UserDTO();
        resDto.setMessage("Thêm mã người giới thiệu thành công. Bạn đã nhận được 1000 xu.");
        return resDto;
    }

}
