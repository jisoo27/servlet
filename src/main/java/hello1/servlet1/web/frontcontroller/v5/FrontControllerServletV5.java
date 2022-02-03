package hello1.servlet1.web.frontcontroller.v5;

import hello1.servlet1.web.frontcontroller.ModelView;
import hello1.servlet1.web.frontcontroller.MyView;
import hello1.servlet1.web.frontcontroller.v3.MemberFormControllerV3;
import hello1.servlet1.web.frontcontroller.v3.MemberListControllerV3;
import hello1.servlet1.web.frontcontroller.v3.MemberSaveControllerV3;
import hello1.servlet1.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    // 매핑 정보의 값이 ControllerV3, ControllerV4 같은 인터페이스에서 아무 값이나 받을 수 있는 object로 변경되었다.
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() { // 생성자는 핸들러 매핑과 어댑터를 초기화 한다.
        initHandlerMappingMap(); // 핸들러 매핑 초기화
        initHandlerAdapters(); // 어댑터 초기화
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object handler = getHandler(request);
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = getHandlerAdapter(handler);
        ModelView mv = adapter.handle(request, response, handler);
        // 어댑터의 handle(request, response, handler) 메서드를 통해 실제 어댑터가 호출된다.
        // 어댑터는 handler(컨트롤러)를 호출하고 그 결과를 어댑터에 맞추어 반환한다.
        // ControllerV3HandlerAdapter의 경우 어댑터의 모양과 컨트롤러의 모양이 유사해서 변환 로직이 단순하다.

        MyView view = viewResolver(mv.getViewName());
        view.render(mv.getModel(), request, response);

    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    } // 핸들러 매핑 정보인 handlerMappingMap 에서 URL 에 매핑된 핸들러(컨트롤러) 객체를 찾아서 반환한다.

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" +handler);
    } // handler를 처리할 수 있는 어댑터를 adapter.supports(handler)를 통해서 찾는다.
      // handler가 ControllerV3 인터페이스를 구현했다면, ControllerV3HandlerAdapter 객체가 반환된다.

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
