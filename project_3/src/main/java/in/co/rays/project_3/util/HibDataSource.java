package in.co.rays.project_3.util;

import org.hibernate.Session; 
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate DataSource is used to provide the object of sessionFactory and
 * session
 * 
 * @author YOGESH SEN
 *
 */
public class HibDataSource {

	private static SessionFactory sessionFactory = null;

	public static SessionFactory getSessionFactory() {

		sessionFactory = new Configuration().configure().buildSessionFactory();

		return sessionFactory ;
	}

	public static Session getSession() {

		Session session = getSessionFactory().openSession();

		return session;
	}

	public static void colseSession(Session session) {

		if (session != null) {
			session.close();
		}

	}

}
