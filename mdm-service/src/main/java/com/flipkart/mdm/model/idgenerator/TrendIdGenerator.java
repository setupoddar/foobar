package com.flipkart.mdm.model.idgenerator;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * Created by setu.poddar on 24/05/17.
 */
public class TrendIdGenerator implements IdentifierGenerator {

    @Override
    public String generate(SessionImplementor session, Object object) throws HibernateException {
        return Long.toString(System.currentTimeMillis() / 60000, Character.MAX_RADIX).toUpperCase()
                + RandomStringUtils.randomAlphanumeric(8).toUpperCase();
    }
}
