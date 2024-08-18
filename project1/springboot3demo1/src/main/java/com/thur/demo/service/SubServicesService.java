package com.thur.demo.service;


import java.util.Optional;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thur.demo.model.subscriptions.ServicesInfo;
import com.thur.demo.model.subscriptions.SubServices;
import com.thur.demo.model.subscriptions.SubServicesId;
import com.thur.demo.model.subscriptions.SubServicesRepository;
import com.thur.demo.model.subscriptions.Subscriptions;
import com.thur.demo.model.user.User;

@Service
public class SubServicesService {
	
	
	
	@Autowired
	private SubServicesRepository subServRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ServicesInfoService servicesInfoService;
	
	
	@Transactional
	public SubServices insert(String json, UUID userId) {
		try {
			JSONObject obj = new JSONObject(json);
			Integer serviceId = obj.isNull("servInfoId") ? null : obj.getInt("servInfoId");
			SubServices exist = subServRepo.findByUsersIdAndServiceId(userId, serviceId);
			if(exist != null) {
				return null;
			}
			Optional<User> user =  userService.findById(userId);
			ServicesInfo servicesInfo = servicesInfoService.findById(serviceId);
			if(servicesInfo == null) {
				return null;
			}
			// id 物件
			SubServicesId subServicesId = new SubServicesId();
			subServicesId.setUserId(userId);
			subServicesId.setServiceId(serviceId);
			
			// Cart 物件
			SubServices subServices = new SubServices();
			subServices.setSubServicesId(subServicesId);
			subServices.setUser(user.get());
			subServices.setServicesInfo(servicesInfo);
			subServices.setServiceName(servicesInfo.getServiceName());	
			return subServRepo.save(subServices);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	//刪除單筆資料(by id)
	public boolean remove(Integer serviceId, UUID userId) {
		if(serviceId!=null) {
			SubServices subServices = subServRepo.findByUsersIdAndServiceId(userId, serviceId);
			if(subServices != null) {
				subServRepo.deleteById(subServices.getSubServicesId());
				return true;
			}
		}
		return false;
	}

	
}
