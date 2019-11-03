package jp.ehcache.Springbootehcachedemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import jp.ehcache.Springbootehcachedemo.model.Item;
import jp.ehcache.Springbootehcachedemo.repository.ItemRepository;

@Service("ItemCacheService")
public class ItemCacheService {
	
	@Autowired
	ItemRepository itemRepository;

	@Cacheable(value = "itemCache", key = "#itemId")
	public Item getItem(int itemId) throws Exception {
		System.out.println("In getItem ItemCacheService method...");
		try {
		return itemRepository.getItem(itemId);
		}catch(Exception e) {
			System.out.println("Exception during caching call");
		}
		return null;
	}

	@CachePut(value = "itemCache", key = "#itemId") //cache put works by updating retun value so always we should return the value also with out key should be passed for proper updation
    public Item updateItem(Item item,int itemId){
		//item.setId(itemId);
        System.out.println("In updateItem ItemCacheService method...");
        itemRepository.updateItem(item);
		return item;
    }

	@CacheEvict(value="itemCache",key = "#id")
	public int deleteItem(int id) {
        System.out.println("In ItemCache delete event..");
        return itemRepository.deleteItem(id);
		
	}

}
