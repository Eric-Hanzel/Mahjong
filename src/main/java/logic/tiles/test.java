package logic.tiles;

public class test {
    public static void main(String[] arg){

        LibraryTile libraryTile = new LibraryTile();
        PlayedTile playedTile = new PlayedTile();
        HandTile handTile = new HandTile("test");
        libraryTile.sort();
        System.out.println(libraryTile.getTileSet());
        for (int i = 0; i < 14; i++){
            handTile.addTile(libraryTile.discard());
        }
        System.out.println(handTile.getTileSet());
        handTile.sort();
        System.out.println(handTile.getTileSet());
    }
}
