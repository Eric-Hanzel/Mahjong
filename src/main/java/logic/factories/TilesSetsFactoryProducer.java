package logic.factories;

public class TilesSetsFactoryProducer {
    public static TilesSetsFactory getTilesSetFactory(boolean Single){

        if (Single){
            return new SingleArrayListFactory();
        }else {
            return new DoubleArrayListFactory();
        }
    }
}
