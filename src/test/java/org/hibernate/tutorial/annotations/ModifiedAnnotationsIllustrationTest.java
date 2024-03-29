/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.tutorial.annotations;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import junit.framework.TestCase;
import util.SessionUtil;
import util.TransactionUtil;

/**
 * Illustrates the use of Hibernate native APIs.  The code here is unchanged from the {@code basic} example, the
 * only difference being the use of annotations to supply the metadata instead of Hibernate mapping files.
 *
 * @author Steve Ebersole
 */
public class ModifiedAnnotationsIllustrationTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
		// A SessionFactory is set up once for an application!
		SessionUtil.initSessionFactory();
	}

	@Override
	protected void tearDown() throws Exception {
		SessionUtil.closeSessionFactory();
	}

	@SuppressWarnings({ "unchecked" })
	public void testBasicUsage() {
		// create a couple of events...
		TransactionUtil.doWithTransaction(session -> {
			session.save( new Event( "Our very first event!", new Date() ) );
			session.save( new Event( "A follow up event", new Date() ) );
		});

		// now lets pull events from the database and list them
		TransactionUtil.doWithTransaction(session -> {
			List result = session.createQuery( "from Event" ).list();
			for ( Event event : (List<Event>) result ) {
				System.out.println( "Event (" + event.getDate() + ") : " + event.getTitle() );
			}
		});
	}

}
