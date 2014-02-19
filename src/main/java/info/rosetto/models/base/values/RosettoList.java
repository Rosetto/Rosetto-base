package info.rosetto.models.base.values;


public interface RosettoList {
    

    public RosettoValue first();
    
    public RosettoValue rest();
    
    public RosettoValue getAt(int index);

}
