package reLogic.factories;

import java.io.Serial;

// Factory producer class for creating different types of tile factories
public class TilesFactoryProducer {

    // Factory method to get a specific type of tiles factory based on whether sequence tiles are needed
    public static TilesFactory getTilesFactory(boolean sequence){

        if (sequence){
            return new SequenceTilesFactory(); // Returns a factory for creating sequence tiles
        }else {
            return new NonSequenceTilesFactory(); // Returns a factory for creating non-sequence tiles
        }
    }
}
