package com.thur.demo.model.subscriptions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table( name = "ServicesPhoto")
public class ServicesPhoto {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "servicesPhoto_id")
	private Integer servicesPhotoId;
	
    @Column(name = "servPhoto", columnDefinition = "VARCHAR(MAX)")
    private String servPhoto;
	
	@ManyToOne
	@JoinColumn(name = "service_id")
	private ServicesInfo servicesInfo;
    
}
