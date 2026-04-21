package com.zh;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.zh.bean.auto.TRoleUser;
import com.zh.filter.SignFilter;
import com.zh.service.TRoleUserService;


/**
 * 初始化管理员
 */
@Component
public class PemInit implements CommandLineRunner {

	@Autowired
	private StringRedisTemplate sRedis;

	@Autowired
	private SignFilter sFilter;

	@Autowired
	private TRoleUserService tRoleUserService;

	private final static Logger log = LoggerFactory.getLogger(PemInit.class);

	@Override
	public void run(String... strings) throws Exception {
		try {
			if (!hasManager()) {
				updateManager();
			}
			
			// 缓存权限
			if (!sRedis.hasKey("hasPem:")) {
				sFilter.updatePem();
			}
		} catch (Exception e) {
			log.error(e.toString(), e);
		}
	}

	/**
	 * 是否缓存了管理者id
	 */
	private boolean hasManager() {
		Set<String> keys = sRedis.keys("manager:*");
		if (keys != null && keys.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 设置缓存中的管理者id
	 */
	private void updateManager() {
		List<TRoleUser> list = tRoleUserService.selectAllManager();
		if (list == null || list.size() == 0) {
			return;
		}
		ValueOperations<String, String> opsForValue = sRedis.opsForValue();
		for (TRoleUser dto : list) {
			opsForValue.set("manager:" + dto.getUserid(), "");
		}
	}
}