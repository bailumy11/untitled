package Dao.impl;

import Dao.IBaseDao;
import util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class BaseDao<T> implements IBaseDao<T> {
    private Class<T> clz;

    public BaseDao() {
        ParameterizedType paramClass = (ParameterizedType)this.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = paramClass.getActualTypeArguments();
        clz = (Class<T>)actualTypeArguments[0];

        Class class1 = getClass();
        System.out.println(class1);

        Class superclass = class1.getSuperclass();
        System.out.println(superclass);

        Type type = class1.getGenericSuperclass();
        System.out.println(type);


        ParameterizedType p =(ParameterizedType)type;
        Type[] typeList=p.getActualTypeArguments();
        clz=(Class<T>)typeList[0];
        System.out.println(clz);

    }

    @Override
    public void save(T object) {
        EntityManager manager = JpaUtil.getEntityManager();
        manager.getTransaction().begin();
        manager.persist(object);
        manager.getTransaction().commit();
        manager.close();
    }

    @Override
    public void update(T object) {
        EntityManager manager = JpaUtil.getEntityManager();
        manager.getTransaction().begin();
        manager.merge(object);
        manager.getTransaction().commit();
        manager.close();
    }

    @Override
    public void delete(Serializable i) {
        EntityManager manager = JpaUtil.getEntityManager();
        manager.getTransaction().begin();
        T s = manager.find(clz,i);
        manager.remove(s);
        manager.getTransaction().commit();
        manager.close();
    }

    @Override
    public T getOne(Serializable i) {
        EntityManager manager = JpaUtil.getEntityManager();
        T s = manager.find(clz,i);
        manager.close();
        return s;
    }

    @Override
    public List<T> getAll() {
        EntityManager manager = JpaUtil.getEntityManager();
        String hql = "select p from "+clz.getName()+" as p";
        Query query = manager.createQuery(hql);
        return query.getResultList();
    }
}