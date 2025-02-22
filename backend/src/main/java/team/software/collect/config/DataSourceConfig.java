package team.software.collect.config;

import com.github.pagehelper.PageInterceptor;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = DataSourceConfig.PACKAGE,sqlSessionFactoryRef = "testDsSqlSessionFactory")
public class DataSourceConfig {

    // 精确到 cluster 目录，以便跟其他数据源隔离
    static final String PACKAGE = "team.software.collect.mapperservice";
    static final String MAPPER_LOCATION = "classpath:mapper/*.xml";
    static final String ALIAS = "team.software.collect.po";

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClass;

    @Bean(name = "testDataSource")
    public DataSource clusterDataSource()throws PropertyVetoException{
        ComboPooledDataSource dataSource=new ComboPooledDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        // 当连接关闭时，不自动提交事务
        dataSource.setAutoCommitOnClose(false);
        // 设置连接的最大闲置时间 - 5小时（需要小于 mysql 默认的 wait_timeout） 超时则将其回收
        dataSource.setMaxIdleTime(18000);
        // 设置用于检测数据库连接是否仍有效的SQL
        dataSource.setPreferredTestQuery("select 1");
        // 设置闲置连接测试周期，保证连接池里的连接都是有效的 - 1 小时（需要小于 mysql 默认的 wait_timeout）
        dataSource.setIdleConnectionTestPeriod(3600);
        return dataSource;
    }

    @Bean(name = "testTransactionManager")
    public DataSourceTransactionManager clusterTransactionManager() throws PropertyVetoException {
        return new DataSourceTransactionManager(clusterDataSource());
    }

    // 创建SqlSessionFactory对象
    @Bean(name = "testDsSqlSessionFactory")
    public SqlSessionFactory testDsSqlSessionFactory(@Qualifier("testDataSource") DataSource clusterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(clusterDataSource);

        // 加载全局的配置文件
        sessionFactory.setConfigLocation(
                new DefaultResourceLoader().getResource("classpath:mybatis-config.xml"));

        // 配置类型别名
        sessionFactory.setTypeAliasesPackage(ALIAS);

        // 分页插件
        PageInterceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        interceptor.setProperties(properties);

        // 添加插件
        sessionFactory.setPlugins(interceptor);

        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(DataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
