package com.move_up.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.move_up.dto.MessageDTO;
import com.move_up.dto.MissionDTO;
import com.move_up.dto.MomoDTO;
import com.move_up.dto.RoleDTO;
import com.move_up.dto.UserDTO;
import com.move_up.dto.WithdrawRequestDTO;
import com.move_up.entity.MessageEntity;
import com.move_up.entity.MissionEntity;
import com.move_up.entity.MomoEntity;
import com.move_up.entity.RoleEntity;
import com.move_up.entity.UserEntity;
import com.move_up.entity.WithdrawRequestEntity;
import com.move_up.repository.MessageRepository;
import com.move_up.repository.MissionRepository;
import com.move_up.repository.MomoRepository;
import com.move_up.repository.RoleRepository;
import com.move_up.repository.UserRepository;
import com.move_up.repository.WithdrawRequestRepository;

@Service
@Transactional
public class Converter {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private MomoRepository momoRepo;

	@Autowired
	private MissionRepository missionRepo;

	@Autowired
	private MessageRepository messageRepo;

	@Autowired
	private WithdrawRequestRepository withdrawRequestRepo;

	@Autowired
	private EntityManagerFactory emf;

	public <T> T toDTO(Object entity, Class<T> tClass) {
		if (entity == null)
			return null;

		ModelMapper modelMap = new ModelMapper();
		T resObj = modelMap.map(entity, tClass);

		return this.dtoObject(entity, resObj);
	}

	public <T> T toEntity(Object dto, Class<T> tClass) {
		if (dto == null)
			return null;

		ModelMapper modelMap = new ModelMapper();
		T resObj = modelMap.map(dto, tClass);

		return this.entityObject(dto, resObj);
	}

	@SuppressWarnings("unchecked")
	private <T> T dtoObject(Object entity, T resObj) {
		
		if (resObj instanceof MessageDTO) {
			MessageDTO messageDto = (MessageDTO) resObj;
			MessageEntity messageEntity = (MessageEntity) entity;

			if (messageEntity.getSentUser() != null)
				messageDto.setSentUsername(messageEntity.getSentUser().getUsername());
			if (messageEntity.getReceivedUser() != null)
				messageDto.setReceivedUsername(messageEntity.getReceivedUser().getUsername());

			return (T) messageDto;
		} else if (resObj instanceof MissionDTO) {
			MissionDTO missionDto = (MissionDTO) resObj;
			MissionEntity missionEntity = (MissionEntity) entity;

			if (missionEntity.getUsers() != null) {
				for (UserEntity userEntity : missionEntity.getUsers())
					missionDto.getUsernames().add(userEntity.getUsername());
			}

			return (T) missionDto;
		} else if (resObj instanceof MomoDTO) {
			MomoDTO momoDto = (MomoDTO) resObj;
			MomoEntity momoEntity = (MomoEntity) entity;

			if (momoEntity.getUser() != null)
				momoDto.setUsername(momoEntity.getUser().getUsername());

			return (T) momoDto;
		} else if (resObj instanceof RoleDTO) {
			RoleDTO roleDto = (RoleDTO) resObj;
			RoleEntity roleEntity = (RoleEntity) entity;

			if (roleEntity.getUsers() != null) {
				for (UserEntity userEntity : roleEntity.getUsers())
					roleDto.getUsernames().add(userEntity.getUsername());
			}

			return (T) roleDto;
		} else if (resObj instanceof UserDTO) {
			EntityManager entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			
			UserDTO userDto = (UserDTO) resObj;
			UserEntity userEntity = (UserEntity) entity;

			if (userEntity.getReferredUsers() != null) {
				for (UserEntity referredUserEntity : userEntity.getReferredUsers())
					userDto.getReferredUsernames().add(referredUserEntity.getUsername());
			}

			if (userEntity.getReferrerUser() != null)
				userDto.setReferrerUsername(userEntity.getReferrerUser().getUsername());

			if (userEntity.getRoles() != null) {
				List<RoleEntity> roles = entityManager.createQuery(
						"SELECT role FROM RoleEntity role JOIN FETCH role.users user WHERE user.username = ?1",
						RoleEntity.class)
						.setParameter(1, userEntity.getUsername())
						.getResultList();

				for (RoleEntity roleEntity : roles)
					userDto.getRoleCodes().add(roleEntity.getCode());
			}

			Collection<GrantedAuthority> authorities = new ArrayList<>();
			if (userEntity.getRoles() != null) {
				List<RoleEntity> roles = entityManager.createQuery(
						"SELECT role FROM RoleEntity role JOIN FETCH role.users user WHERE user.username = ?1",
						RoleEntity.class)
						.setParameter(1, userEntity.getUsername())
						.getResultList();
				
				for (RoleEntity roleEntity : roles)
					authorities.add(new SimpleGrantedAuthority(roleEntity.getCode()));
			}
			userDto.setAuthorities(authorities);

			if (userEntity.getMomo() != null)
				userDto.setMomoPhoneNumber(userEntity.getMomo().getPhoneNumber());

			if (userEntity.getMissions() != null) {
				List<MissionEntity> missions = entityManager.createQuery(
						"SELECT mission FROM MissionEntity mission JOIN FETCH mission.users user WHERE user.username = ?1",
						MissionEntity.class)
						.setParameter(1, userEntity.getUsername())
						.getResultList();
				
				for (MissionEntity missionEntity : missions)
					userDto.getMissionIds().add(missionEntity.getId());
			}

			if (userEntity.getSentMessages() != null) {
				List<MessageEntity> sentMessages = entityManager.createQuery(
						"SELECT message FROM MessageEntity message WHERE message.sentUser.username = ?1",
						MessageEntity.class)
						.setParameter(1, userEntity.getUsername())
						.getResultList();
				for (MessageEntity sentMessageEntity : sentMessages)
					userDto.getSentMessageIds().add(sentMessageEntity.getId());
			}

			if (userEntity.getReceivedMessages() != null) {
				List<MessageEntity> receivedMessages = entityManager.createQuery(
						"SELECT message FROM MessageEntity message WHERE message.receivedUser.username = ?1",
						MessageEntity.class)
						.setParameter(1, userEntity.getUsername())
						.getResultList();
				for (MessageEntity receivedMessageEntity : receivedMessages)
					userDto.getReceivedMessageIds().add(receivedMessageEntity.getId());
			}

			if (userEntity.getWithdrawRequests() != null) {
				List<WithdrawRequestEntity> withdrawRequestEntities = entityManager.createQuery(
						"SELECT withdrawRequest FROM WithdrawRequestEntity withdrawRequest "
						+ "WHERE withdrawRequest.user.username = ?1",
						WithdrawRequestEntity.class)
						.setParameter(1, userEntity.getUsername())
						.getResultList();
				for (WithdrawRequestEntity withdrawRequestEntity : withdrawRequestEntities)
					userDto.getWithdrawRequestIds().add(withdrawRequestEntity.getId());
			}

			entityManager.getTransaction().commit();
			entityManager.close();
			return (T) userDto;
		} else if (resObj instanceof WithdrawRequestDTO) {
			WithdrawRequestDTO withdrawRequestDto = (WithdrawRequestDTO) resObj;
			WithdrawRequestEntity withdrawRequestEntity = (WithdrawRequestEntity) entity;

			if (withdrawRequestEntity.getUser() != null) {
				withdrawRequestDto.setUsername(withdrawRequestEntity.getUser().getUsername());
			}

			return (T) withdrawRequestDto;
		}

		return resObj;
	}

	@SuppressWarnings("unchecked")
	private <T> T entityObject(Object dto, T resObj) {

		if (resObj instanceof MessageEntity) {
			MessageEntity messageEntity = (MessageEntity) resObj;
			MessageDTO messageDto = (MessageDTO) dto;

			if (messageDto.getSentUsername() != null)
				messageEntity.setSentUser(userRepo.findById(messageDto.getSentUsername()).get());
			if (messageDto.getReceivedUsername() != null)
				messageEntity.setReceivedUser(userRepo.findById(messageDto.getReceivedUsername()).get());

			return (T) messageEntity;
		} else if (resObj instanceof MissionEntity) {
			MissionEntity missionEntity = (MissionEntity) resObj;
			MissionDTO missionDto = (MissionDTO) dto;

			if (missionDto.getUsernames() != null) {
				for (String username : missionDto.getUsernames())
					missionEntity.getUsers().add(userRepo.findById(username).get());
			}

			return (T) missionEntity;
		} else if (resObj instanceof MomoEntity) {
			MomoEntity momoEntity = (MomoEntity) resObj;
			MomoDTO momoDto = (MomoDTO) dto;

			if (momoDto.getUsername() != null)
				momoEntity.setUser(userRepo.findById(momoDto.getUsername()).get());

			return (T) momoEntity;
		} else if (resObj instanceof RoleEntity) {
			RoleEntity roleEntity = (RoleEntity) resObj;
			RoleDTO roleDto = (RoleDTO) dto;

			if (roleDto.getUsernames() != null) {
				for (String username : roleDto.getUsernames())
					roleEntity.getUsers().add(userRepo.findById(username).get());
			}

			return (T) roleEntity;
		} else if (resObj instanceof UserEntity) {
			UserEntity userEntity = (UserEntity) resObj;
			UserDTO userDto = (UserDTO) dto;

			if (userDto.getReferredUsernames() != null) {
				for (String username : userDto.getReferredUsernames())
					userEntity.getReferredUsers().add(userRepo.findById(username).get());
			}

			if (userDto.getReferrerUsername() != null)
				userEntity.setReferrerUser(userRepo.findById(userDto.getReferrerUsername()).get());

			if (userDto.getRoleCodes() != null) {
				for (String roleCode : userDto.getRoleCodes())
					userEntity.getRoles().add(roleRepo.findById(roleCode).get());
			}

			if (userDto.getMomoPhoneNumber() != null)
				userEntity.setMomo(momoRepo.findById(userDto.getMomoPhoneNumber()).get());

			if (userDto.getMissionIds() != null) {
				for (Long id : userDto.getMissionIds())
					userEntity.getMissions().add(missionRepo.findById(id).get());
			}

			if (userDto.getSentMessageIds() != null) {
				for (Long id : userDto.getSentMessageIds())
					userEntity.getSentMessages().add(messageRepo.findById(id).get());
			}

			if (userDto.getReceivedMessageIds() != null) {
				for (Long id : userDto.getReceivedMessageIds())
					userEntity.getReceivedMessages().add(messageRepo.findById(id).get());
			}

			if (userDto.getWithdrawRequestIds() != null) {
				for (Long id : userDto.getWithdrawRequestIds())
					userEntity.getWithdrawRequests().add(withdrawRequestRepo.findById(id).get());
			}

			return (T) userEntity;
		} else if (resObj instanceof WithdrawRequestEntity) {
			WithdrawRequestEntity withdrawRequestEntity = (WithdrawRequestEntity) resObj;
			WithdrawRequestDTO withdrawRequestDto = (WithdrawRequestDTO) dto;

			if (withdrawRequestDto.getUsername() != null) {
				withdrawRequestEntity.setUser(userRepo.findById(withdrawRequestDto.getUsername()).get());
			}

			return (T) withdrawRequestEntity;
		}

		return (T) resObj;
	}
	
}
