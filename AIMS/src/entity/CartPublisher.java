package entity;

import views.CartObserver;

public interface CartPublisher {
    public void registerObserver(CartObserver productObserver);
    public void unregisterObserver(CartObserver productObserver);
    public void notifyObserver();
}
