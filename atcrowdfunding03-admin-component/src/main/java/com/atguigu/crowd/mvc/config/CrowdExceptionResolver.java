package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.exception.AccessForbiddenException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 异常处理器
 * @author Lemon
 * @create 2023-02-17-12:04
 */
@ControllerAdvice
public class CrowdExceptionResolver {
    public ModelAndView resolveLoginAcctAlreadyInUseException(
            LoginAcctAlreadyInUseException exception,
            HttpServletRequest request,HttpServletResponse response) throws IOException {
        return commonResolveException(exception, request, response, "admin-add");
    }
    @ExceptionHandler(value = {LoginFailedException.class, AccessForbiddenException.class})
    public ModelAndView resolveLoginFailedException(LoginFailedException exception,
                                                    HttpServletResponse response,
                                                    HttpServletRequest request) throws IOException {
        return commonResolveException(exception,request,response,"admin-login");
    }
    @ExceptionHandler(value=Exception.class)
    public ModelAndView resolveException(Exception exception,HttpServletRequest request,HttpServletResponse response) throws IOException {
        return commonResolveException(exception,request,response,"system-error");
    }
    /**
     * 检测请求是否为AJAX，以不同的形式返回错误信息
     * @author: Lemon
     * @create:2023/2/17-12:38
    */
    private ModelAndView commonResolveException(
            Exception exception,
            HttpServletRequest request,
            HttpServletResponse response,
            String viewName
    ) throws IOException {
        if(CrowdUtil.judgeRequestType(request)){
            Gson gson=new Gson();
            ResultEntity<Object> result = ResultEntity.failed(exception.getMessage());
            String s = gson.toJson(result);
            response.getWriter().write(s);
            return null;
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject(CrowdConstant.ATTR_NAME_EXCEPTION,exception);
        mav.setViewName(viewName);
        return mav;
    }
}
