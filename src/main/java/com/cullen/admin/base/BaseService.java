package com.cullen.admin.base;


/**
 * @author cullen
 * @date 2019-10-12  22:01
 * @email newxieyang@msn.cn
 */
@FunctionalInterface
public interface BaseService<E> {

    BaseMapper<E> getMapper();


    /***
     * 按照id获取对象
     * @param id
     * @return
     */
    default E get(Object id) {
        return getMapper().selectByPrimaryKey(id);
    }


    /***
     * 保存对象
     * @param entity
     * @return
     */
    default int save(E entity) {
        return getMapper().insertSelective(entity);
    }


    /***
     * 更新对象
     * @param entity
     * @return
     */
    default int update(E entity) {
        return getMapper().updateByPrimaryKeySelective(entity);
    }


    /***
     * 按照主键删除对象
     * @param key
     * @return
     */
    default int delete(String key) {
        return getMapper().deleteByPrimaryKey(key);
    }


}
