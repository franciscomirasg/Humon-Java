package com.shadowgunther;

public class Controller {
    private static Controller INSTANCE;

    private Controller()
    {

    }

    public static Controller getINSTANCE() {
        return INSTANCE;
    }
}
