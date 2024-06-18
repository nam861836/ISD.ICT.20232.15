package entity.shipping;

public class AdditionalInfo {
    private String deliveryTime;
    private String deliveryInstruction;

    public void setDeliveryInstruction(String deliveryInstruction) {
        this.deliveryInstruction = deliveryInstruction;
    }

    public String getDeliveryInstruction() {
        return this.deliveryInstruction;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public AdditionalInfo(){
        super();
    }
    public AdditionalInfo(String deliveryTime, String deliveryInstruction){
        super();
        this.deliveryInstruction = deliveryInstruction;
        this.deliveryTime = deliveryTime;
    }

    public void enterAddtionalData(String deliveryTime, String deliveryInstruction){
        this.deliveryInstruction = deliveryInstruction;
        this.deliveryTime = deliveryTime;
    }

    

}
