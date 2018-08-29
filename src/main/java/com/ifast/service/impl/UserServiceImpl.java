package com.ifast.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.plugins.Page;
import com.ifast.api.config.JWTConfig;
import com.ifast.api.exception.IFastApiException;
import com.ifast.api.util.JWTUtil;
import com.ifast.common.config.CacheConfiguration;
import com.ifast.common.config.IFastConfig;
import com.ifast.common.type.EnumErrorCode;
import com.ifast.common.utils.SpringContextHolder;
import com.ifast.dao.ApiUserDao;
import com.ifast.domain.ApiUserDO;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import com.ifast.service.UserService;
import com.ifast.common.base.CoreServiceImpl;

import java.io.IOException;
import java.util.Map;

/**
 * <pre>
 * @author
 * </pre>
 * <small> 2018-08-19 19:10:03 | Aron</small>
 */
@Service(value = "apiService")
public class UserServiceImpl extends CoreServiceImpl<ApiUserDao, ApiUserDO> implements UserService {

    OkHttpClient client = new OkHttpClient();

    private static class Holder {
        static final JWTConfig jwt = SpringContextHolder.getBean(IFastConfig.class).getJwt();
        static final Cache logoutTokens = CacheConfiguration.dynaConfigCache("tokenExpires", 0, jwt.getRefreshTokenExpire(), 1000);

        static {
            JWTUtil.mykey = jwt.getUserPrimaryKey();
            String appid = jwt.getUserPrimaryKey();
        }
    }

    @Override
    public ApiUserDO getUserByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String userId = JWTUtil.getUserId(token);
        if (userId == null || "".equals(userId)) {
            return null;
        }
        return this.selectById(userId);
    }

    /**
     * 登陆token
     *
     * @param code
     * @return
     * @throws IOException
     */
    @Override
    public String getToken(String code) throws IOException {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        Map<String, String> map;
        if (response.isSuccessful()) {
            map = JSONObject.parseObject(response.body().string(), new TypeReference<Map<String, String>>() {
            });
            map.put("openid", "df");
            map.put("session_key", "dd");
            map.put("unionid", "33");
        } else {
            throw new IOException("Unexpected code " + response);
        }
        if (map.get("openid") == null) {
            return "";
        }
        ApiUserDO apiUserDO = new ApiUserDO();
        apiUserDO.setUnionid(map.get("unionid"));
        apiUserDO.setOpenid(map.get("openid"));
        apiUserDO.setSession_key(map.get("session_key"));
        this.insert(apiUserDO);
        return JWTUtil.sign(apiUserDO.getId().toString(), apiUserDO.getOpenid() + apiUserDO.getSession_key(), Holder.jwt.getExpireTime());
    }

    /**
     * 验证token
     *
     * @param token
     * @return
     */
    @Override
    public boolean verifyToken(String token) {
        String userId = null;
        ApiUserDO user = null;
        return org.apache.commons.lang.StringUtils.isNotBlank(token)
                && (userId = JWTUtil.getUserId(token)) != null
                && notLogout(token)
                && (user = selectById(Long.parseLong(userId))) != null
                && (JWTUtil.verify(token, userId, user.getOpenid() + user.getSession_key()));
    }

    private boolean notLogout(String token) {
        if (Holder.logoutTokens.get(token) != null) {
            throw new IFastApiException(EnumErrorCode.apiAuthorizationLoggedout.getMsg());
        }
        return true;
    }

    /**
     * 关联查询
     *
     * @param id
     * @return
     */
    @Override
    public ApiUserDO queryById(String id) {
        return this.baseMapper.queryById(id);
    }

    /**
     * 分页查询用户信息
     *
     * @param page
     * @param userDO
     * @return
     */
    @Override
    public Page<ApiUserDO> queryUserPage(Page page, ApiUserDO userDO) {
        return page.setRecords(baseMapper.queryByUserDo(page,userDO));
    }
}
