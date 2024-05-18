package logic.factories;

public class TilesFactoryProducer {
    public static TilesFactory getTilesFactory(boolean sequence){

        if (sequence){
            return new SequenceTilesFactory();
        }else {
            return new NonSequenceTilesFactory();
        }
    }
}
