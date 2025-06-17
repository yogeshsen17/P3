package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.CartDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class CartModelHibImp implements CartModelInt {

	@Override
	public long add(CartDTO dto) throws ApplicationException, DuplicateRecordException {

		System.out.println("in addddddddddddd");
		/* log.debug("cartmodel hib start"); */

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
			throw new ApplicationException("Exception in Cart Add " + e.getMessage());
		} finally {
			session.close();
		}
		/* log.debug("Model add End"); */
		return dto.getId();

	}

	@Override
	public void delete(CartDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in Cart Delete" + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public void update(CartDTO dto) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction tx = null;

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Cart update" + e.getMessage());
		} finally {
			session.close();
		}
	}

	@Override
	public CartDTO findByPK(long pk) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = null;
		CartDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (CartDTO) session.get(CartDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting User by pk");
		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return list(0, 0);
	}

	@Override
	public List list(int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CartDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in  Cart list");
		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List search(CartDTO dto, int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub

		System.out.println("hellllo" + pageNo + "....." + pageSize + "........" + dto.getId() + "......" + dto.getId());

		Session session = null;
		ArrayList<CartDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CartDTO.class);
			if (dto != null) {
				if (dto.getId() != null) {
					criteria.add(Restrictions.like("id", dto.getId()));
				}
				if (dto.getCustomerName() != null && dto.getCustomerName().length() > 0) {
					criteria.add(Restrictions.like("CustomerName", dto.getCustomerName() + "%"));
				}
				if (dto.getProductName() != null && dto.getProductName().length() > 0) {
					criteria.add(Restrictions.like("productName", dto.getProductName() + "%"));
				}
				if (dto.getTransactionDate() != null && dto.getTransactionDate().getTime() > 0) {
					criteria.add(Restrictions.like("transactionDate", dto.getTransactionDate()));
				}
				if (dto.getProductQuantity() > 0) {
					criteria.add(Restrictions.eq("productQuantity", dto.getProductQuantity()));
				}
			}
			// if pageSize is greater than 0
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = (ArrayList<CartDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in user search");
		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public List search(CartDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return search(dto, 0, 0);
	}

}