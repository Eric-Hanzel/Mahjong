package reLogic.tilesSets;

import reLogic.factories.TilesFactory;
import reLogic.factories.TilesFactoryProducer;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;

public class LibraryTileSet extends SingleArrayListTileDecorator {
    @Serial
    private static final long serialVersionUID = 11L;
    private ArrayList<reLogic.tiles.Tile> libraryTileSet;
    public LibraryTileSet(){
        TilesFactory sequenceTilesProducer = TilesFactoryProducer.getTilesFactory(true);
        TilesFactory nonSequenceTilesProducer = TilesFactoryProducer.getTilesFactory(false);
        libraryTileSet = new ArrayList<reLogic.tiles.Tile>();
        String[] tileTypes = {"1Character","2Character","3Character","4Character","5Character","6Character","7Character",
                "8Character","9Character", "1Bamboo","2Bamboo","3Bamboo","4Bamboo","5Bamboo","6Bamboo","7Bamboo","8Bamboo",
                "9Bamboo", "1Dot","2Dot","3Dot","4Dot","5Dot","6Dot","7Dot","8Dot","9Dot","East","West","South","North",
                "Red","Green","White"};
        for (int i = 0; i < 4; ++i){
            for (int j = 0; j < 34;++j){
                if (j<=26){
                    libraryTileSet.add(sequenceTilesProducer.getTile(tileTypes[j]));
                }else{
                    libraryTileSet.add(nonSequenceTilesProducer.getTile(tileTypes[j]));
                }
            }
        }
        setTileSets(libraryTileSet);
        setTileNumber(libraryTileSet.size());
    }


    @Override
    public void sort() {
        Collections.shuffle(libraryTileSet);
    }
}
