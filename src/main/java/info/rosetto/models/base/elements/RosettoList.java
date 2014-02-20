package info.rosetto.models.base.elements;


public interface RosettoList extends RosettoValue {
    

    public RosettoValue first();
    
    public RosettoValue rest();
    
    public RosettoValue getAt(int index);

}
