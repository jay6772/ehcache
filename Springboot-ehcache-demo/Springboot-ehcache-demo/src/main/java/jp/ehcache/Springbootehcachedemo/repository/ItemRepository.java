package jp.ehcache.Springbootehcachedemo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import jp.ehcache.Springbootehcachedemo.model.Item;

@Repository("ItemRepository")
public class ItemRepository {

	@Value("${cassandra-config.keyspace-name}")
	private String keyspaceName;
	
	@Autowired
	@Qualifier("cassandraSessionBean")
	Session session;
	
	
	public Item getItem(int itemId) {
		System.out.println("Inside repo class to fetch data from database");
		Item item = new Item();
		String query = "SELECT * FROM ITEM WHERE ID=?";
		Row row = null;
		ResultSet resultSet = null;
		
		try {
		resultSet = session.execute(query, itemId);
		row = resultSet.one();
		if(null != row) {
			
			item.setId(row.get("id",Integer.class));
			item.setName(row.get("name",String.class));
			item.setCategory(row.get("category",String.class));
		}
		}catch(Exception e) {
			System.out.println("Exception during query execution"+e);
		}
		
		return item;
	}


	public void updateItem(Item item) {
		String query = "update ITEM set name=?,category=? WHERE ID=?";
		try {
		 session.execute(query, item.getName(),item.getCategory(),item.getId());
		}catch(Exception e) {
			System.out.println("Exception during update query execution"+e);
		}
		
	}


	public int deleteItem(int id) {
		String query = "DELETE FROM ITEM WHERE ID=?";
		try {
		 session.execute(query,id);
		}catch(Exception e) {
			System.out.println("Exception during update query execution"+e);
		}
		return id;
	}

}
