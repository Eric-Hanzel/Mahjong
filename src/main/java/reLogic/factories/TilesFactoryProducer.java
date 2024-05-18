package reLogic.factories;

import java.io.Serial;

public class TilesFactoryProducer {
    public static TilesFactory getTilesFactory(boolean sequence){

        if (sequence){
            return new SequenceTilesFactory();
        }else {
            return new NonSequenceTilesFactory();
        }
    }
}
