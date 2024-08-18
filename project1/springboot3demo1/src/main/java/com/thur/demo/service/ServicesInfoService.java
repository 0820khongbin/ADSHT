package com.thur.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thur.demo.model.subscriptions.ServicesInfo;
import com.thur.demo.model.subscriptions.ServicesInfoRepository;
import com.thur.demo.model.subscriptions.ServicesPhoto;
import com.thur.demo.model.subscriptions.ServicesPhotoRepository;
import com.thur.demo.model.subscriptions.SubServices;
import com.thur.demo.model.subscriptions.SubServicesRepository;
import com.thur.demo.model.subscriptions.Subscriptions;
import com.thur.demo.model.subscriptions.SubscriptionsRepository;
import com.thur.demo.model.user.User;
import com.thur.demo.model.user.UserRepository;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ServicesInfoService {

	
	
	@Autowired
	private ServicesInfoRepository servicesInfoRepo;
	
	@Autowired
	private SubscriptionsRepository subscriptionsRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	//修改
	public ServicesInfo modify(String json, Integer id) {
		try {
			JSONObject obj = new JSONObject(json);
			//Integer id = obj.isNull("serviceId") ? null : obj.getInt("serviceId");
			String name = obj.isNull("serviceName") ? null : obj.getString("serviceName");
			Integer price = obj.isNull("price") ? null : obj.getInt("price");
			String description = obj.isNull("description") ? null : obj.getString("description");
			String image = obj.isNull("image") ? null : obj.getString("image");
			Integer level = obj.isNull("level") ? null : obj.getInt("level");
			Optional<ServicesInfo> optional = servicesInfoRepo.findById(id);
			if(optional.isPresent()) {
				ServicesInfo update = optional.get();
				//update.setServiceId(id);
				update.setServiceName(name);
				update.setPrice(price);
				update.setDescription(description);
				update.setLevel(level);
				update.setImage(image);
				return servicesInfoRepo.save(update);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//查詢全部
	public List<ServicesInfo> findAll() {
		return servicesInfoRepo.findAll();
	}

	
	//查詢我建立的訂閱
	public List<ServicesInfo> findCreater(UUID loginUserId) {
		return servicesInfoRepo.findCreater(loginUserId);
	}
	
	//查詢使用者目前訂閱的資料(by id)
	public List<ServicesInfo> findMySubsById(UUID id) {
		try {
			return servicesInfoRepo.findMySubs(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//查詢單筆(by id)
	public ServicesInfo findById(Integer id) {
		Optional<ServicesInfo> optional = servicesInfoRepo.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	//刪除單筆資料(by id)
	public boolean remove(Integer id) {
		try {
			if(id!=null) {
				Optional<ServicesInfo> optional = servicesInfoRepo.findById(id);
				if(optional.isPresent()) {
					servicesInfoRepo.deleteById(id);
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	//新增單筆資料
	public ServicesInfo insert(String json,UUID loginUserId) {
		try {
			JSONObject obj = new JSONObject(json);
			String name = obj.isNull("serviceName") ? null : obj.getString("serviceName");
			Integer price = obj.isNull("price") ? null : obj.getInt("price");
			String description = obj.isNull("description") ? null : obj.getString("description");
			String image = obj.isNull("image") ? null : obj.getString("image");
			Integer level = obj.isNull("level") ? null : obj.getInt("level");
			//boolean acitve = obj.isNull("acitve") ? null : obj.getBoolean("acitve");
				ServicesInfo insert = new ServicesInfo();
				insert.setServiceName(name);
				insert.setDescription(description);
				insert.setPrice(price);
				insert.setLevel(level);
				try {
					insert.setImage(image);
				} catch (Exception e) {
					insert.setImage(null);
				}
				ServicesInfo servicesInfo = servicesInfoRepo.save(insert);

//				System.out.println(insert.getServiceId());
///
				Subscriptions insert2 = new Subscriptions();
				Optional<User> userOpt = userRepo.findById(loginUserId);
				Optional<ServicesInfo> servOpt = servicesInfoRepo.findById(insert.getServiceId());
				insert2.setUser(userOpt.get());
				insert2.setServicesInfo(servOpt.get());
				insert2.setActive(false);
				subscriptionsRepo.save(insert2);
///
				return servicesInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<ServicesInfo> find(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return servicesInfoRepo.find(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public long count(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return servicesInfoRepo.count(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}
