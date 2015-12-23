package com.xyc.proj.conf;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.xyc.proj.global.Constants;
import com.xyc.proj.mapper.ClientUserMapper;
import com.xyc.proj.mapper.CommunityMapper;
import com.xyc.proj.mapper.DepositStatMapper;
import com.xyc.proj.mapper.OrderMapper;
import com.xyc.proj.mapper.OrderStatMapper;
import com.xyc.proj.mapper.UserAddressMapper;
import com.xyc.proj.mapper.WorkerMapper;

/**
 * 获取第二个数据库的连接信息，在application.properties中配置，并指定特定的前缀
 *
 * @author 小翼
 * @version 1.0.0
 */
@Configuration
public class MyBatisConfiguration {


	@Autowired
	DataSource datasource;
	@Autowired
	SqlSessionFactory sqlSessionFactory;
	@Autowired
	SqlSessionTemplate sessionTemplate; 
	
	@Autowired
	private UserAddressMapper userAddressMapper; 
	@Autowired
	OrderMapper orderMapper;
	
	@Autowired
	WorkerMapper workerMapper;
	
	@Autowired
	CommunityMapper communityMapper;
	
	@Autowired
	ClientUserMapper clientUserMapper;
	@Autowired
	OrderStatMapper OrderStatMapper;
	@Autowired
	DepositStatMapper depositStatMapper;
	@Bean
	public SqlSessionTemplate sqlSessionTemplate() {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
 
	
	@Bean
	public UserAddressMapper uniqueUserStatMap() {
		return sessionTemplate.getMapper(UserAddressMapper.class);
	}
	
	@Bean
	public OrderMapper orderMapper() {
		return sessionTemplate.getMapper(OrderMapper.class);
	}
	
	@Bean
	public WorkerMapper workerMapper() {
		return sessionTemplate.getMapper(WorkerMapper.class);
	}
	
	@Bean
	public CommunityMapper communityMapper() {
		return sessionTemplate.getMapper(CommunityMapper.class);
	}
	
	
	@Bean
	public ClientUserMapper clientUserMapper() {
		return sessionTemplate.getMapper(ClientUserMapper.class);
	}
	
	@Bean
	public OrderStatMapper orderStatMapper() {
		return sessionTemplate.getMapper(OrderStatMapper.class);
	}
	
	@Bean
	public DepositStatMapper depositStatMapper() {
		return sessionTemplate.getMapper(DepositStatMapper.class);
	}
	
    
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource){
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource); 
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();           
		try {
			sqlSessionFactoryBean.setMapperLocations(resolver.getResources(Constants.MYBATIS_CONFIG_FILE_PATH));
			sqlSessionFactory = sqlSessionFactoryBean.getObject();
		} catch (Exception e) {
			throw new RuntimeException("MyBatis SqlSessionFactory init Error:" + e.getMessage());
		}
		
		return sqlSessionFactory;
	}
	   
	

}
