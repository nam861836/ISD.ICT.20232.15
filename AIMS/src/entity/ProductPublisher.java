package entity;

import views.ProductObserver;

public interface ProductPublisher {
    public void registerObserver(ProductObserver productObserver);
    public void unregisterObserver(ProductObserver productObserver);
    public void notifyObserver();
}
