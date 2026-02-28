/**
 * 分类数据访问对象
 * 
 * 用于分类实体的数据库操作
 * 
 * 创建日期: 2026-02-12
 */
package com.ledger.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ledger.app.data.entity.CategoryEntity
import com.ledger.app.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow

/**
 * 分类DAO接口
 * 
 * 定义分类实体的数据库操作方法
 */
@Dao
interface CategoryDao {
    /**
     * 插入分类
     * 
     * @param category 分类实体
     * @return 插入的分类ID
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity): Long

    /**
     * 更新分类
     * 
     * @param category 分类实体
     */
    @Update
    suspend fun update(category: CategoryEntity)

    /**
     * 删除分类
     * 
     * @param category 分类实体
     */
    @Delete
    suspend fun delete(category: CategoryEntity)

    /**
     * 根据ID获取分类
     * 
     * @param id 分类ID
     * @return 分类实体，不存在则返回null
     */
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getById(id: Long): CategoryEntity?
    
    /**
     * 根据ID列表批量获取分类
     * 
     * @param ids 分类ID列表
     * @return 分类实体列表
     */
    @Query("SELECT * FROM categories WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Long>): List<CategoryEntity>

    /**
     * 获取所有分类
     * 
     * @return 分类列表流
     */
    @Query("SELECT * FROM categories ORDER BY name")
    fun getAll(): Flow<List<CategoryEntity>>

    /**
     * 根据类型获取分类
     * 
     * @param type 交易类型
     * @return 分类列表流
     */
    @Query("SELECT * FROM categories WHERE type = :type ORDER BY name")
    fun getByType(type: TransactionType): Flow<List<CategoryEntity>>

    /**
     * 根据类型统计分类数量
     * 
     * @param type 交易类型
     * @return 分类数量
     */
    @Query("SELECT COUNT(*) FROM categories WHERE type = :type")
    suspend fun countByType(type: TransactionType): Int
}
