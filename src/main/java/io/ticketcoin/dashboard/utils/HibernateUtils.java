package io.ticketcoin.dashboard.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

	private static  SessionFactory sessionFactory;
	
	static {
		try {
			// Crea la SessionFactory da hibernate.cfg.xml
			sessionFactory = new Configuration().configure().buildSessionFactory();

			
			
				// A SessionFactory is set up once for an application!
				final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
						.configure() // configures settings from hibernate.cfg.xml
						.build();
				try {
					sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
				}
				catch (Exception e) {
					// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
					// so destroy it manually.
					StandardServiceRegistryBuilder.destroy( registry );
				}
			
			
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtils.sessionFactory = sessionFactory;
	}
	
}
