# 账本App数据库文档

## 1. 数据库架构概述

账本App使用Room数据库作为本地存储解决方案，采用MVVM架构模式。数据库主要包含两个表：`transactions`（交易表）和`categories`（分类表）。

### 1.1 数据库层次结构

```
┌───────────────────┐    ┌───────────────────┐    ┌───────────────────┐
│    ViewModel      │    │      UseCase      │    │    Repository     │
└───────────────────┘    └───────────────────┘    └───────────────────┘
          ↑                      ↑                      ↑
          │                      │                      │
┌───────────────────┐    ┌───────────────────┐    ┌───────────────────┐
│      UI Layer     │    │ Business Logic    │    │  Data Access      │
└───────────────────┘    └───────────────────┘    └───────────────────┘
                                                        ↑
                                                        │
┌────────────────────────────────────────────────────────┐
│                    Room Database                      │
└────────────────────────────────────────────────────────┘
```

## 2. 数据库表结构

### 2.1 交易表（transactions）

| 字段名 | 数据类型 | 约束 | 描述 |
|-------|---------|------|------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT | 交易ID |
| amount | REAL | NOT NULL | 交易金额 |
| type | TEXT | NOT NULL | 交易类型（INCOME/EXPENSE） |
| categoryId | INTEGER | NOT NULL | 分类ID（外键关联categories表） |
| date | TEXT | NOT NULL | 交易日期时间 |
| description | TEXT | NULL | 交易描述 |
| createdAt | TEXT | NOT NULL | 创建时间 |
| updatedAt | TEXT | NOT NULL | 更新时间 |

### 2.2 分类表（categories）

| 字段名 | 数据类型 | 约束 | 描述 |
|-------|---------|------|------|
| id | INTEGER | PRIMARY KEY AUTOINCREMENT | 分类ID |
| name | TEXT | NOT NULL | 分类名称 |
| type | TEXT | NOT NULL | 交易类型（INCOME/EXPENSE） |
| icon | TEXT | NOT NULL | 分类图标名称 |
| color | TEXT | NOT NULL | 分类颜色 |
| isDefault | INTEGER | NOT NULL | 是否为默认分类 |

## 3. 实体类

### 3.1 TransactionEntity

```kotlin
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Long,
    val date: LocalDateTime,
    val description: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
```

### 3.2 CategoryEntity

```kotlin
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: TransactionType,
    val icon: String,
    val color: String = "#FF6200EE",
    val isDefault: Boolean = false
)
```

### 3.3 TransactionType 枚举

```kotlin
enum class TransactionType {
    INCOME,  // 收入
    EXPENSE  // 支出
}
```

## 4. DAO接口

### 4.1 TransactionDao

提供交易相关的数据库操作：

- `insert(transaction: TransactionEntity)`: 插入交易
- `update(transaction: TransactionEntity)`: 更新交易
- `delete(transaction: TransactionEntity)`: 删除交易
- `getById(id: Long)`: 根据ID获取交易
- `getAll()`: 获取所有交易（按日期降序）
- `getByDateRange(startDate: LocalDateTime, endDate: LocalDateTime)`: 根据日期范围获取交易
- `getByCategory(categoryId: Long)`: 根据分类获取交易
- `getByType(type: TransactionType)`: 根据类型获取交易
- `search(query: String)`: 搜索交易
- `getTotalByTypeAndDateRange(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime)`: 获取指定类型和日期范围的总金额
- `getCategoryBreakdown(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime)`: 获取分类金额 breakdown
- `getDailyTotals(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime)`: 获取每日总金额

### 4.2 CategoryDao

提供分类相关的数据库操作：

- `insert(category: CategoryEntity)`: 插入分类
- `update(category: CategoryEntity)`: 更新分类
- `delete(category: CategoryEntity)`: 删除分类
- `getById(id: Long)`: 根据ID获取分类
- `getAll()`: 获取所有分类
- `getByType(type: TransactionType)`: 根据类型获取分类
- `countByType(type: TransactionType)`: 根据类型统计分类数量

## 5. Repository类

### 5.1 TransactionRepository

封装交易相关的业务逻辑：

- 基本CRUD操作
- 获取收入/支出总额
- 获取余额（收入-支出）
- 获取分类金额 breakdown
- 获取每日金额统计

### 5.2 CategoryRepository

封装分类相关的业务逻辑：

- 基本CRUD操作
- 获取收入/支出分类
- 初始化默认分类

## 6. 数据库初始化

### 6.1 默认分类初始化

应用启动时，会自动检查是否存在分类数据。如果不存在，则插入默认分类：

#### 6.1.1 默认收入分类

- Salary（工资）: 绿色
- Bonus（奖金）: 蓝色
- Investment（投资）: 橙色
- Other Income（其他收入）: 紫色

#### 6.1.2 默认支出分类

- Food（餐饮）: 红色
- Transportation（交通）: 蓝色
- Shopping（购物）: 紫色
- Entertainment（娱乐）: 橙色
- Housing（住房）: 绿色
- Utilities（水电煤）: 青色
- Healthcare（医疗）: 粉色
- Other Expense（其他支出）: 灰色

## 7. 数据库操作流程

### 7.1 添加交易流程

1. 用户在添加交易页面填写交易信息
2. 系统验证输入数据的有效性
3. 系统检查所选分类是否存在
4. 创建TransactionEntity对象
5. 通过TransactionRepository插入交易
6. 数据库执行插入操作并返回交易ID

### 7.2 查看交易流程

1. 用户进入交易列表页面
2. 系统通过TransactionRepository加载交易数据
3. TransactionRepository调用TransactionDao获取交易列表
4. 数据库执行查询操作并返回交易列表
5. 系统将交易数据显示在UI上

### 7.3 分类管理流程

1. 用户进入分类管理页面
2. 系统通过CategoryRepository加载分类数据
3. 系统显示分类列表
4. 用户可以添加、编辑或删除分类
5. 系统通过CategoryRepository执行相应的数据库操作

## 8. 依赖注入

应用使用Hilt进行依赖注入，简化组件间的依赖管理：

### 8.1 数据库依赖

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideLedgerDatabase(@ApplicationContext context: Context): LedgerDatabase {
        return Room.databaseBuilder(
            context,
            LedgerDatabase::class.java,
            "ledger_database"
        ).build()
    }

    @Provides
    fun provideTransactionDao(database: LedgerDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideCategoryDao(database: LedgerDatabase): CategoryDao {
        return database.categoryDao()
    }
}
```

### 8.2 Repository依赖

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository {
        return TransactionRepository(transactionDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }
}
```

## 9. 日志系统

应用使用自定义的LogUtils类进行日志记录，只在调试模式下输出日志：

### 9.1 数据库操作日志

所有数据库相关操作都会记录详细的日志，包括：

- 插入/更新/删除操作的详细信息
- 查询操作的参数和结果
- 默认分类初始化的过程
- 错误和异常信息

## 10. 性能优化

### 10.1 异步操作

- 使用Kotlin Coroutines进行异步数据库操作
- 使用Flow API实现数据的流式传输
- 避免在主线程执行数据库操作

### 10.2 查询优化

- 合理使用索引
- 避免复杂的嵌套查询
- 使用分页加载大量数据

## 11. 数据库版本管理

### 11.1 当前版本

- 数据库版本: 1
- 导出Schema: false

### 11.2 版本升级策略

未来数据库结构变更时，将：

1. 增加数据库版本号
2. 提供Migration策略
3. 确保数据的安全性和完整性

## 12. 总结

账本App的数据库设计采用了Room框架，提供了：

1. 清晰的表结构设计
2. 完整的CRUD操作
3. 丰富的查询功能
4. 默认数据初始化
5. 详细的日志记录
6. 良好的性能优化

这套数据库架构能够满足用户日常记账的需求，同时为未来的功能扩展提供了坚实的基础。