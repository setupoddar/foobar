package com.flipkart.mdm.dal.dao;

import com.flipkart.mdm.dal.exception.DBException;
import com.flipkart.mdm.model.GenericModel;
import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;

abstract class GenericDAO<T extends GenericModel> extends AbstractDAO<T> {

    @Inject
    protected GenericDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveOrUpdate(T object) throws DBException {
        try {
            if (object != null) {
                persist(object);
            }
        } catch (Exception e) {
            throw new DBException("Unable to save or update object", e);
        }
    }


    public void save(T object) throws DBException {
        try {
            if (object != null) {
                if (StringUtils.isNotEmpty(object.getId()))
                    throw new DBException("Id already present");
                persist(object);
            }
        } catch (Exception e) {
            throw new DBException("Unable to save object", e);
        }
    }


    public void update(T object) throws DBException {
        try {
            if (object != null) {
                if (StringUtils.isEmpty(object.getId()))
                    throw new DBException("Id missing");
                persist(object);
            }
        } catch (Exception e) {
            throw new DBException("Unable to update object", e);
        }
    }


    public T findById(String id) throws DBException {
        try {
            return get(id);
        } catch (Exception e) {
            throw new DBException("Unable to find object with - " + id, e);
        }
    }

    public void delete(T object) throws DBException {
        try {
            if (object != null) {
                currentSession().delete(object);
            }
        } catch (Exception e) {
            throw new DBException("Unable to delete object ", e);
        }
    }
}
