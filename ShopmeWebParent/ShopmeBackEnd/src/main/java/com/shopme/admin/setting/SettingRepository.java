package com.shopme.admin.setting;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Setting;

public interface SettingRepository extends CrudRepository<Setting, String> {

}