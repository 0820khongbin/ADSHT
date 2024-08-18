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
public class ServicesPhotoService {

	@Autowired
	private ServicesInfoRepository servicesInfoRepo;
	
	@Autowired
	private ServicesPhotoRepository servicesPhotoRepo;
	

	//新增單筆資料
	public ServicesPhoto insert(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			Integer serviceId = obj.isNull("serviceId") ? null : obj.getInt("serviceId");
			String photo = obj.isNull("photo") ? null : obj.getString("photo");
			ServicesPhoto insert = new ServicesPhoto();
				try {
					insert.setServPhoto(photo);
				} catch (Exception e) {
					insert.setServPhoto(null);
				}
				Optional<ServicesInfo> servInfoOpt = servicesInfoRepo.findById(serviceId);
				insert.setServicesInfo(servInfoOpt.get());
				return servicesPhotoRepo.save(insert);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
