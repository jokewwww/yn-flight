package com.zh.service.impl;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.zh.bean.login.LoginUser;
import com.zh.bean.login.Token;
import com.zh.constant.Constant;
import com.zh.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenServiceImpl implements TokenService {

	private static final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);

	/**
	 * token过期秒数
	 */
	@Value("${token.expire.seconds}")
	private Integer expireSeconds;

	@Autowired
	private RedisTemplate<String, LoginUser> redisTemplate;

	/**
	 * 私钥
	 */
	@Value("${token.jwtSecret}")
	private String jwtSecret;

	private static Key KEY = null;
	private static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

	@Override
	public Token saveToken(LoginUser loginUser) {
		loginUser.setToken(UUID.randomUUID().toString());
		cacheLoginUser(loginUser);
		String jwtToken = createJWTToken(loginUser);
		return new Token(jwtToken, loginUser.getLoginTime());
	}

	/**
	 * 生成token
	 */
	private String createJWTToken(LoginUser loginUser) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(LOGIN_USER_KEY, loginUser.getToken());
		String jwtToken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance())
				.compact();
		return jwtToken;
	}

	private void cacheLoginUser(LoginUser loginUser) {
		loginUser.setLoginTime(System.currentTimeMillis());
		loginUser.setExpireTime(loginUser.getLoginTime() + expireSeconds * 1000);
		redisTemplate.boundValueOps(getTokenKey(loginUser.getToken())).set(loginUser, expireSeconds, TimeUnit.SECONDS);
		
		String staffId = null;
		if (loginUser.getLoginUserIn() != null) {
			staffId = loginUser.getLoginUserIn().getStaffId();
		}
		if (staffId == null) {
			staffId = loginUser.getStaffId();
		}
		Set<String> keys = redisTemplate.keys(Constant.LOGIN_KEY + staffId + ":*");
		if (keys != null && keys.size() > 0) {
			for (String key : keys) {
				if (key.replace(":", "").length() + 4 == key.length()
						&& (key.endsWith(":0") || key.endsWith(":1") || key.endsWith(":103") || key.endsWith(":104"))) {
					redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
				}
			}
		}
		keys = redisTemplate.keys(Constant.PC_LOGIN + staffId + ":*");
		if (keys != null && keys.size() > 0) {
			for (String key : keys) {
				if (key.replace(":", "").length() + 4 == key.length()
						&& (key.endsWith(":0") || key.endsWith(":1") || key.endsWith(":103") || key.endsWith(":104"))) {
					redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
				}
			}
		}
		keys = redisTemplate.keys(Constant.LOGIN_KEY + staffId);
		if (keys != null && keys.size() > 0) {
			for (String key : keys) {
				redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
			}
		}
		keys = redisTemplate.keys(Constant.PC_LOGIN + staffId);
		if (keys != null && keys.size() > 0) {
			for (String key : keys) {
				redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
			}
		}
	}

	/**
	 * 刷新缓存
	 */
	@Override
	public void refresh(LoginUser loginUser) {
		cacheLoginUser(loginUser);
	}

	@Override
	public LoginUser getLoginUser(String jwtToken) {
		String uuid = getUUIDFromJWT(jwtToken);
		if (uuid != null) {
			return redisTemplate.boundValueOps(getTokenKey(uuid)).get();
		}

		return null;
	}

	@Override
	public boolean deleteToken(String jwtToken) {
		String uuid = getUUIDFromJWT(jwtToken);
		if (uuid != null) {
			String key = getTokenKey(uuid);
			LoginUser loginUser = redisTemplate.opsForValue().get(key);
			if (loginUser != null) {
				redisTemplate.delete(key);
				// 退出日志
				// logService.save(loginUser.getId(), "退出", true, null);
			}
			return true;
		}

		return false;
	}

	private String getTokenKey(String uuid) {
		return "tokens:" + uuid;
	}

	/**
	 * 获取秘钥Key
	 */
	private Key getKeyInstance() {
		if (KEY == null) {
			synchronized (TokenServiceImpl.class) {
				if (KEY == null) {
					byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSecret);
					KEY = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
				}
			}
		}
		return KEY;
	}

	/**
	 * 获取uuid
	 */
	private String getUUIDFromJWT(String jwtToken) {
		if ("null".equals(jwtToken) || "undifined".equals(jwtToken) || StringUtils.isBlank(jwtToken)) {
			return null;
		}

		try {
			Map<String, Object> jwtClaims = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(jwtToken)
					.getBody();
			return MapUtils.getString(jwtClaims, LOGIN_USER_KEY);
		} catch (ExpiredJwtException e) {
			log.error("{}已过期", jwtToken);
		} catch (Exception e) {
			log.error("{}", e);
		}

		return null;
	}
}
