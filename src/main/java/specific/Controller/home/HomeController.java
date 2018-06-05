package specific.Controller.home;

import com.jfinal.core.Controller;
import specific.service.home.HomeService;

public class HomeController  extends Controller {
    static HomeService service = new HomeService();

    public void queryByHome() {

        renderJson(service.queryByHome());
    }
    public void queryByHome2() {

        renderJson(service.queryByHome2());
    }
    public void queryByHome3() {

        renderJson(service.queryByHome3());
    }



}