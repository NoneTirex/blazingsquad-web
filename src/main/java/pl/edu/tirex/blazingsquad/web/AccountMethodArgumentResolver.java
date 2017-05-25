package pl.edu.tirex.blazingsquad.web;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.tirex.blazingsquad.web.groups.Flag;
import pl.edu.tirex.blazingsquad.web.groups.GroupFlag;
import pl.edu.tirex.blazingsquad.web.groups.RequiredActivate;
import pl.edu.tirex.blazingsquad.web.groups.RequiredAuthorized;
import pl.edu.tirex.blazingsquad.web.groups.RequiredUnauthorized;
import pl.edu.tirex.blazingsquad.web.utils.HttpRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

@Component
public class AccountMethodArgumentResolver implements HandlerMethodArgumentResolver, HandlerInterceptor
{
    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        return Account.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndView, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception
    {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null)
        {
            return null;
        }
        return HttpRequestUtils.getAccountFromSession(request);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception
    {
        RequiredUnauthorized requiredUnauthorized = getAnnotation(RequiredUnauthorized.class, object);
        RequiredAuthorized requiredAuthorized = getAnnotation(RequiredAuthorized.class, object);
        Flag flag = getAnnotation(Flag.class, object);
        GroupFlag[] needFlags = null;
        if (flag != null && flag.value().length > 0)
        {
            needFlags = flag.value();
        }
        Account account = HttpRequestUtils.getAccountFromSession(request);
        if (account == null && (requiredAuthorized != null))
        {
            if (!requiredAuthorized.value().isEmpty())
            {
                response.sendRedirect(requiredAuthorized.value());
                return false;
            }
            response.sendRedirect("/login");
            return false;
        }
        if (account == null)
        {
            return true;
        }
        if (requiredUnauthorized != null)
        {
            if (!requiredUnauthorized.value().isEmpty())
            {
                response.sendRedirect(requiredUnauthorized.value());
                return false;
            }
            response.sendRedirect("/");
            return false;
        }
        if (account.getGroup() != null && needFlags != null)
        {
            int i = 0;
            for (GroupFlag groupFlag : needFlags)
            {
                if (account.getGroup().getFlags().contains(groupFlag))
                {
                    i++;
                }
            }
            if (i < needFlags.length)
            {
                //not access
                response.sendRedirect("/");
                return false;
            }
        }
        //        if (needFlags != null || (requiredAuthorized != null))
        //        {
        RequiredActivate requiredActivate = getAnnotation(RequiredActivate.class, object);
        if (requiredActivate != null && !account.isActive())
        {
            if (!requiredActivate.value().isEmpty())
            {
                response.sendRedirect(requiredActivate.value());
                return false;
            }
            response.sendRedirect("/");
            return false;
        }
        //        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception
    {
        Account account = HttpRequestUtils.getAccountFromSession(request);
        if (account != null)
        {
            modelAndView.addObject("account", account);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception
    {

    }

    private <A extends Annotation> A getAnnotation(Class<A> annotationType, Object object)
    {
        if (object instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod = ((HandlerMethod) object);
            A annotation = handlerMethod.getMethodAnnotation(annotationType);
            if (annotation == null)
            {
                annotation = handlerMethod.getMethod().getDeclaringClass().getAnnotation(annotationType);
            }
            return annotation;
        }
        return null;
    }
}
