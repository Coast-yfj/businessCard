package com.ifast.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
import org.apache.xerces.impl.dv.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import com.ifast.service.UserService;
import com.ifast.common.base.CoreServiceImpl;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.*;
import java.util.Arrays;
import java.util.List;
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

    @Value("${appid}")
    private String appid;

    @Value("${secret}")
    private String secret;


    private static class Holder {
        static final JWTConfig jwt = SpringContextHolder.getBean(IFastConfig.class).getJwt();
        static final Cache logoutTokens = CacheConfiguration.dynaConfigCache("tokenExpires", 0, jwt.getRefreshTokenExpire(), 1000);

        static {
            JWTUtil.mykey = jwt.getUserPrimaryKey();
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
        ApiUserDO apiUserDO = new ApiUserDO();
        apiUserDO.setId(Long.parseLong(userId));
        return this.queryById(userId);
    }

    /**
     * 登陆token
     *
     * @param code
     * @return
     * @throws IOException
     */
    @Override
    public String getToken(String code, String iv, String encryptedData) throws Exception {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        Map<String, String> map;
        if (response.isSuccessful()) {
            map = JSONObject.parseObject(response.body().string(), new TypeReference<Map<String, String>>() {
            });
        } else {
            throw new IOException("Unexpected code " + response);
        }
        if (map.get("openid") == null) {
            return "";
        }
        ApiUserDO apiUserDO = new ApiUserDO();
        apiUserDO.setOpenid(map.get("openid"));
        ApiUserDO userDO = this.selectOne(new EntityWrapper<>(apiUserDO));
        if (userDO != null) {
            apiUserDO = userDO;
        }
        // apiUserDO.setUnionid(map.get("unionid")==null?"":map.get("unionid"));
        apiUserDO.setSession_key(map.get("session_key"));
        Map<String, String> userInfo = getUserInfo(encryptedData, apiUserDO.getSession_key(), iv);
        apiUserDO.setAvatarUrl(userInfo.get("avatarUrl"));
        apiUserDO.setCity(userInfo.get("city"));
        apiUserDO.setProvince(userInfo.get("province"));
        apiUserDO.setCountry(userInfo.get("country"));
        apiUserDO.setNickName(userInfo.get("nickName"));
        apiUserDO.setGender(userInfo.get("gender"));
        this.insertOrUpdate(apiUserDO);
        return Base64.encode(apiUserDO.getId().toString().getBytes());
//    return JWTUtil.sign(apiUserDO.getId().toString(), apiUserDO.getOpenid() + apiUserDO.getSession_key(), Holder.jwt.getExpireTime());
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
//        return org.apache.commons.lang.StringUtils.isNotBlank(token)
//                && (userId = JWTUtil.getUserId(token)) != null
//                && notLogout(token)
//                && (user = selectById(Long.parseLong(userId))) != null
//                && (JWTUtil.verify(token, userId, user.getOpenid() + user.getSession_key()));
        return org.apache.commons.lang.StringUtils.isNotBlank(token)
                && (userId = new String(Base64.decode(token))) != null
                && (user = selectById(Long.parseLong(userId))) != null;

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
        return page.setRecords(baseMapper.queryByUserDo(page, userDO));
    }

    /**
     * 解密用户敏感数据获取用户信息
     *
     * @param sessionKey    数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     */
    public Map<String, String> getUserInfo(String encryptedData, String sessionKey, String iv) throws Exception {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        int base = 16;
        if (keyByte.length % base != 0) {
            int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
            keyByte = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
        parameters.init(new IvParameterSpec(ivByte));
        // 初始化
        cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
        byte[] resultByte = cipher.doFinal(dataByte);
        if (null != resultByte && resultByte.length > 0) {
            String result = new String(resultByte, "UTF-8");
            return JSONObject.parseObject(result, new TypeReference<Map<String, String>>() {
            });
        }
        return null;
    }

    /**
     * 查询名片夹
     *
     * @param id
     * @return
     */
    @Override
    public List<ApiUserDO> queryByIds(String id) {
        return baseMapper.queryByIds(id);
    }
}
