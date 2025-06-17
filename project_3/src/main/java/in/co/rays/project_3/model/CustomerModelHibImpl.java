package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.CustomerDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class CustomerModelHibImpl implements CustomerModelInt {

	@Override
	public long add(CustomerDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = HibDataSource.getSession();
		Transaction tx = null;
		long id;
		try {
			tx = session.beginTransaction();
			id = (long) session.save(dto);
			dto.getId();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			// TODO: handle exception
			if (tx != null) {
				tx.rollback();

			}
			throw new ApplicationException("Exception in Customer Add " + e.getMessage());
		} finally {
			session.close();
		}
		/* log.debug("Model add End"); */ 
		System.out.println("@@@@@@@@@@@@@@@@===="+id);
		return id;

	}

	@Override
	public void update(CustomerDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = HibDataSource.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Customer update" + e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public void delete(CustomerDTO dto) throws ApplicationException {
		Session session = HibDataSource.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Customer Delete" + e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public CustomerDTO findByPK(long pk) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = HibDataSource.getSession();
		CustomerDTO dto = null;
		try {
			dto = (CustomerDTO) session.get(CustomerDTO.class, pk);
		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting Customer by pk");
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
		Session session = HibDataSource.getSession();
		List list = null;
		try {

			Criteria criteria = session.createCriteria(CustomerDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in  Customers list");
		} finally {
			session.close();
		}

		return list;

	}

	@Override
	public List search(CustomerDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return search(dto, 0, 0);
	}

	@Override
	public List search(CustomerDTO dto, int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub
		Session session = null;
		ArrayList<CustomerDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CustomerDTO.class);
			if (dto != null) {

				System.out.println("11111111111111111111111111111111111111111111111111111111111");
				if (dto.getId() != null) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}
				if (dto.getClientName() != null && dto.getClientName().length() > 0) {
					criteria.add(Restrictions.like("clientName", dto.getClientName() + "%"));
				}
				if (dto.getLocation() != null && dto.getLocation().length() > 0) {
					criteria.add(Restrictions.like("location", dto.getLocation() + "%"));
				}
				if (dto.getContactNumber() > 0) {
					criteria.add(Restrictions.eq("contactNumber", dto.getContactNumber()));
				}
				if (dto.getImportance() > 0) {
					criteria.add(Restrictions.eq("importance", dto.getImportance()));
				}

			}
			// if pageSize is greater than 0
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = (ArrayList<CustomerDTO>) criteria.list();
		} catch (HibernateException e) {
			throw new ApplicationException("Exception in customer search");
		} finally {
			session.close();
		}

		return list;

	}

}
