package org.nuist.myblog.config;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nuist.myblog.entity.Result;
import org.nuist.myblog.util.JWTUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 拦截器
 */
public class JWTInterceptors implements HandlerInterceptor {
    public static final int invalid_signature = 10001;// 无效签名
    public static final int signature_error = 10002;// 签名错误
    public static final int token_error = 10003;// token错误
    public static final int token_expired = 10004;// token过期
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Result<Integer> result = new Result<>();
        Map<String,Object> map = new HashMap<>();
        // 获取请求头中令牌
        String token = request.getHeader("token");
        try {
            // 验证令牌
            JWTUtils.verify(token);
            return true;  // 放行请求

        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            result.setMessage("无效签名！");
            result.setData(invalid_signature);
            // map.put("msg","无效签名！");
        }catch (TokenExpiredException e){
            e.printStackTrace();
            result.setMessage("token过期");
            result.setData(token_expired);
            // map.put("msg","token过期");
        }catch (AlgorithmMismatchException e){
            e.printStackTrace();
            result.setMessage("算法不一致");
            result.setData(signature_error);
            // map.put("msg","算法不一致");
        }catch (Exception e){
            e.printStackTrace();
            result.setMessage("token无效！");
            result.setData(token_error);
            // map.put("msg","token无效！");
        }
        // map.put("state",false);  // 设置状态
        result.setSuccess(false);
        // 将map以json的形式响应到前台  map --> json  (jackson)
        String json = new ObjectMapper().writeValueAsString(result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
