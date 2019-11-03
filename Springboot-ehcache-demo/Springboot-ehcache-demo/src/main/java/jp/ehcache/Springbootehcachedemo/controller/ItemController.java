package jp.ehcache.Springbootehcachedemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jp.ehcache.Springbootehcachedemo.model.Item;
import jp.ehcache.Springbootehcachedemo.service.ItemCacheService;

@RestController
public class ItemController {
	
    @Autowired
    ItemCacheService itemCacheService;
    @GetMapping("/item/{itemId}")
    @ResponseBody
    public ResponseEntity<Item> getItem(@PathVariable int itemId) throws Exception{
        Item item = itemCacheService.getItem(itemId);
        return new ResponseEntity<Item>(item, HttpStatus.OK);
    }
    
    @PutMapping("/updateItem")
    @ResponseBody
    public ResponseEntity<Item> updateItem(@RequestBody Item item){
        if(item != null){
        	int itemId = item.getId();
        	itemCacheService.updateItem(item,itemId);//
        }
        return new ResponseEntity<Item>(item, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteItem(@PathVariable int id){
    	itemCacheService.deleteItem(id);
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
}
