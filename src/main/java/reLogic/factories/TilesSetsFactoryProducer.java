package reLogic.factories;

// Factory producer class for creating different types of tiles set factories
public class TilesSetsFactoryProducer {

    // Factory method to get a specific type of tiles sets factory based on the input flag
    public static TilesSetsFactory getTilesSetFactory(boolean Single){

        if (Single){
            return new SingleArrayListFactory(); // Returns a factory for creating single array list tile sets
        }else {
            return new DoubleArrayListFactory(); // Returns a factory for creating double array list tile sets
        }
    }
}
