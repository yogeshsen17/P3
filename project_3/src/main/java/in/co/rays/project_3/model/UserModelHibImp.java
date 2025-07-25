package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.UserDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.exception.RecordNotFoundException;
import in.co.rays.project_3.util.EmailBuilder;
import in.co.rays.project_3.util.EmailMessage;
import in.co.rays.project_3.util.EmailUtility;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implements of User model
 * 
 * @author YOGESH SEN
 *
 */
public class UserModelHibImp implements UserModelInt{

	/**
	 * Add a User.
	 *
	 * @param dto the bean
	 * @return the long
	 * @throws DuplicateRecordException the duplicate record exception
	 * @throws RecordNotFoundException  the record not found exception
	 * @throws ApplicationException     the application exception
	 */
	public long add(UserDTO dto) throws ApplicationException, DuplicateRecordException {

		System.out.println("in addddddddddddd");
		/* log.debug("usermodel hib start"); */

		UserDTO existDto = null;
		existDto = findByLogin(dto.getLogin());
		if (existDto != null) {
			throw new DuplicateRecordException("login id already exist");
		}
		Session session = HibDataSource.getSession();
		Transaction tx = null;
		try {

			tx = session.beginTransaction();

			session.save(dto);

			dto.getId();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			// TODO: handle exception
			if (tx != null) {
				tx.rollback();

			}
			throw new ApplicationException("Exception in User Add " + e.getMessage());
		} finally {
			session.close();
		}
		/* log.debug("Model add End"); */
		return dto.getId();

	}

	/**
	 * Delete a User.
	 *
	 * @param dto the bean
	 * @throws ApplicationException the application exception
	 */
	public void delete(UserDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in User Delete" + e.getMessage());
		} finally {
			session.close();
		}
	}

	/**
	 * Update a user.
	 *
	 * @param bean the bean
	 * @throws ApplicationException     the application exception
	 * @throws DuplicateRecordException the duplicate record exception
	 * @throws RecordNotFoundException  the record not found exception
	 */
	public void update(UserDTO dto) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;
		UserDTO exesistDto = findByLogin(dto.getLogin());

		if (exesistDto != null && exesistDto.getId() != dto.getId()){
			throw new DuplicateRecordException("Login id already exist");
		}

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(dto);
			tx.commit();
		} catch (HibernateException e){
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in User update" + e.getMessage());
		} finally {
			session.close();
		}
	}

	/**
	 * Find User by PK.
	 *
	 * @param pk : get parameter
	 * @return bean
	 * @throws ApplicationException the application exception
	 */

	public UserDTO findByPK(long pk) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = null;
		UserDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (UserDTO) session.get(UserDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			session.close();
		}

		return dto;
	}

	/**
	 * Find User by Login.
	 *
	 * @param login : get parameter
	 * @return bean
	 * @throws ApplicationException the application exception
	 */

	public UserDTO findByLogin(String login) throws ApplicationException {
		//TODO Auto-generated method stub
		Session session = null;
		UserDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(UserDTO.class);
			criteria.add(Restrictions.eq("login", login));
			List list = criteria.list();
			if (list.size() > 0) {
				dto = (UserDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting User by Login " + e.getMessage());

		} finally {
			session.close();
		}

		return dto;
	}

	/**
	 * Get List of User.
	 *
	 * @return list : List of User
	 * @throws ApplicationException the application exception
	 */
	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return list(0, 0);
	}

	/**
	 * Get List of User with pagination.
	 *
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @return list : List of users
	 * @throws ApplicationException the application exception
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(UserDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in  Users list");
		} finally {
			session.close();
		}

		return list;
	}

	/**
	 * Search User.
	 *
	 * @param bean : Search Parameters
	 * @return the list
	 * @throws ApplicationException the application exception
	 */
	public List search(UserDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return search(dto, 0, 0);
	}

	/**
	 * Search User with pagination.
	 *
	 * @param bean     : Search Parameters
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @return list : List of Users
	 * @throws ApplicationException the application exception
	 */
	public List search(UserDTO dto, int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub

		System.out.println(
				"hellllo" + pageNo + "....." + pageSize + "........" + dto.getId() + "......" + dto.getRoleId());

		Session session = null;
		ArrayList<UserDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(UserDTO.class);
			if (dto != null) {
				if (dto.getId() != null) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}
				if (dto.getFirstName() != null && dto.getFirstName().length() > 0) {
					criteria.add(Restrictions.like("firstName", dto.getFirstName() + "%"));
				}
				if (dto.getLastName() != null && dto.getLastName().length() > 0) {
					criteria.add(Restrictions.like("lastName", dto.getLastName() + "%"));
				}
				if (dto.getLogin() != null && dto.getLogin().length() > 0) {
					criteria.add(Restrictions.like("login", dto.getLogin() + "%"));
				}
				if (dto.getPassword() != null && dto.getPassword().length() > 0) {
					criteria.add(Restrictions.like("password", dto.getPassword() + "%"));
				}
				if (dto.getGender() != null && dto.getGender().length() > 0) {
					criteria.add(Restrictions.like("gender", dto.getGender() + "%"));
				}
				if (dto.getDob() != null && dto.getDob().getTime() > 0) {
					criteria.add(Restrictions.eq("dob", dto.getDob()));
				}
				if (dto.getRoleId() > 0) {
					criteria.add(Restrictions.eq("roleId", dto.getRoleId()));
				}
				
			}
			// if pageSize is greater than 0
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = (ArrayList<UserDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in user search");
		} finally {
			session.close();
		}

		return list;
	}

	/**
	 * Authenticate.
	 *
	 * @param login    : String login
	 * @param password password : String Password
	 * @return the user bean
	 * @throws ApplicationException the application exception
	 */
	public UserDTO authenticate(String login, String password) throws ApplicationException {
		// TODO Auto-generated method stub
		System.out.println(login + "kkkkk" + password);
		Session session = null;
		UserDTO dto = null;
		session = HibDataSource.getSession();
		Query q = session.createQuery("from UserDTO where login=? and password=?");
		
//		q.setCacheable(true);

		q.setString(0, login);
		q.setString(1, password);
		List list = q.list();
		if (list.size() > 0) {
			dto = (UserDTO) list.get(0);
		} else {
			dto = null;

		}
		return dto;
	}
	/**
	 * Get User Roles
	 * 
	 * @return List : User Role List
	 * @param bean
	 * @throws ApplicationException
	 */
	public List getRoles(UserDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Change password.
	 *
	 * @param id          : long id
	 * @param oldPassword password : String oldPassword
	 * @param newPassword : String newPassword
	 * @return true, if successful
	 * @throws RecordNotFoundException the record not found exception
	 * @throws ApplicationException    the application exception
	 */

	public boolean changePassword(long id, String newPassword, String oldPassword)
			throws ApplicationException, RecordNotFoundException {
		// TODO Auto-generated method stub
		boolean flag = false;
		UserDTO dtoExist = null;

		dtoExist = findByPK(id);

		if (dtoExist != null && dtoExist.getPassword().equals(oldPassword)) {
			dtoExist.setPassword(newPassword);
			try {
				update(dtoExist);
			} catch (DuplicateRecordException e) {

				throw new ApplicationException("LoginId is already exist");
			}
			flag = true;
		} else {
			throw new RecordNotFoundException("Login not exist");
		}

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("login", dtoExist.getLogin());
		map.put("password", dtoExist.getPassword());
		map.put("firstName", dtoExist.getFirstName());
		map.put("lastName", dtoExist.getLastName());

		String message = EmailBuilder.getChangePasswordMessage(map);

		EmailMessage msg = new EmailMessage();

		msg.setTo(dtoExist.getLogin());
		msg.setSubject("Password has been changed Successfully.");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);

		return flag;

	}

	/**
	 * Send the password of User to his Email.
	 *
	 * @param login : User Login
	 * @return boolean : true if success otherwise false
	 * @throws RecordNotFoundException : if user not found
	 * @throws ApplicationException    the application exception
	 */

	public boolean forgetPassword(String login) throws ApplicationException, RecordNotFoundException {
		// TODO Auto-generated method stub
		UserDTO userData = findByLogin(login);
		boolean flag = false;
		if (userData == null) {
			throw new RecordNotFoundException("Email Id Does not matched.");

		}

		
		  HashMap<String, String> map = new HashMap<String, String>();
		  
		  map.put("login", userData.getLogin()); map.put("password",
		  userData.getPassword()); map.put("firstName", userData.getFirstName());
		  map.put("lastName", userData.getLastName());
		  
		  String message = EmailBuilder.getForgetPasswordMessage(map);
		  
		  EmailMessage msg = new EmailMessage();
		  
		  msg.setTo(login); msg.setSubject("SUNARYS ORS Password reset");
		  msg.setMessage(message); msg.setMessageType(EmailMessage.HTML_MSG);
		  EmailUtility.sendMail(msg);
		 
		flag = true;

		return flag;
	}

	/**
	 * Reset Password of User with auto generated Password.
	 *
	 * @param bean the bean
	 * @return boolean : true if success otherwise false
	 * 
	 *         : User Login
	 * @throws ApplicationException the application exception
	 */

	public boolean resetPassword(UserDTO dto) throws ApplicationException, RecordNotFoundException {
		// TODO Auto-generated method stub
		String newPassword = String.valueOf(new Date().getTime()).substring(0, 4);
		UserDTO userData = findByPK(dto.getId());
		userData.setPassword(newPassword);

		try {
			update(userData);
		} catch (DuplicateRecordException e) {
			return false;
		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", dto.getLogin());
		map.put("password", dto.getPassword());

		String message = EmailBuilder.getForgetPasswordMessage(map);

		EmailMessage msg = new EmailMessage();

		msg.setTo(dto.getLogin());
		msg.setSubject("Password has been reset");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);

		return true;
	}

	/**
	 * Register a user.
	 *
	 * @param bean the bean
	 * @return the long
	 * @throws DuplicateRecordException : throws when user already exists
	 * @throws RecordNotFoundException  the record not found exception
	 * @throws ApplicationException     the application exception
	 */
	public long registerUser(UserDTO dto) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub
		long pk = add(dto);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", dto.getLogin());
		map.put("password", dto.getPassword());

		String message = EmailBuilder.getUserRegistrationMessage(map);

		EmailMessage msg = new EmailMessage();

		msg.setTo(dto.getLogin());
		msg.setSubject("Registration is successful for ORS Project SUNRAYS Technologies");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);

		return pk;
	}

}