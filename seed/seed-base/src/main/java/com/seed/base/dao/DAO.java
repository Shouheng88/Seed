package com.seed.base.dao;

import com.seed.base.exception.DAOException;
import com.seed.base.model.query.SearchObject;

import java.util.List;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 14:09
 */
public interface DAO<T, E> {

    int insert(T entity) throws DAOException;

    int update(T entity) throws DAOException;

    int updatePOSelective(T entity) throws DAOException;

    List<T> searchBySo(SearchObject so) throws DAOException;

    List<E> searchVosBySo(SearchObject so) throws DAOException;

    long searchCountBySo(SearchObject so) throws DAOException;

    int deleteByPrimaryKey(Long id) throws DAOException;

    T selectByPrimaryKey(Long id) throws DAOException;

    E selectVoByPrimaryKey(Long id) throws DAOException;

}
