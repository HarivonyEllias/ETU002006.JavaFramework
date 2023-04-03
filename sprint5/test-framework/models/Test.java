package models;

import etu2006.framework.annotation.Url;
import etu2006.framework.renderer.ModelView;

public class Test {

    @Url("/test")
    public ModelView helloWorld() {
        return new ModelView("views/test.jsp");
    }
}