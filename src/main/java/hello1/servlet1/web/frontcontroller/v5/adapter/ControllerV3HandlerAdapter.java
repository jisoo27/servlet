package hello1.servlet1.web.frontcontroller.v5.adapter;

import hello1.servlet1.web.frontcontroller.ModelView;
import hello1.servlet1.web.frontcontroller.v3.ControllerV3;
import hello1.servlet1.web.frontcontroller.v5.MyHandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV3HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return  (handler instanceof ControllerV3);
    } // ControllerV3를 처리할 수 있는 어댑터를 뜻한다.
     /*
     * instanceof 연산자란?
     * 왼쪽에는 참조변수 오른쪽에는 타입(클래스명)이 피연산자로 위치한다.
     * 연산의 결과로 boolean 값 true, false 를 반환한다.
     * */

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        ControllerV3 controller = (ControllerV3) handler;
        Map<String, String> paramMap = createParaMap(request);
        ModelView mv = controller.process(paramMap);
        return mv;
    }
    /*
    * handler를 컨트롤러 V3로 변환한 다음에 V3 형식에 맞도록 호출한다.
    * supports( )를 통해 ControllerV3만 지원하기 때문에 타입 변환은 걱정없이 실행해도 된다.
    * ControllerV3는 ModelView를 반환하므로 그대로 ModelView를 반하면 된다.
    * */


    private Map<String, String> createParaMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
