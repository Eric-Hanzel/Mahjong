package logic.rules;

import logic.tiles.BambooTile;
import logic.tiles.CharacterTile;
import logic.tiles.DotTile;
import logic.tiles.HandTile;

public class test1 {
    public static void main(String[] arg){
        Rule rule = new Rule();
        VictoryRule victoryRule = new VictoryRule();
        HandTile handTile = new HandTile("test");
        handTile.addTile(new CharacterTile("Character",1,1));
        handTile.addTile(new CharacterTile("Character",2,2));
        handTile.addTile(new CharacterTile("Character",3,1));
        handTile.addTile(new BambooTile("Bamboo",3,2));
        handTile.addTile(new BambooTile("Bamboo",4,1));
        handTile.addTile(new BambooTile("Bamboo",5,1));
        handTile.addTile(new BambooTile("Bamboo",5,1));
        handTile.addTile(new BambooTile("Bamboo",5,1));
        handTile.addTile(new DotTile("Dot",7,1));
        handTile.addTile(new DotTile("Dot",8,2));
        handTile.addTile(new DotTile("Dot",8,3));
        handTile.addTile(new DotTile("Dot",9,1));
        handTile.addTile(new DotTile("Dot",9,1));
        handTile.addTile(new DotTile("Dot",9,1));
        System.out.println(rule.getSequenceNumber(handTile.getTileSet()));
        System.out.println(rule.getTripletNumber(handTile.getTileSet()));
        System.out.println(rule.getPairNumber(handTile.getTileSet()));
        System.out.println(rule.getKongNumber(handTile.getTileSet()));
        System.out.println(victoryRule.bigSingleWait(handTile));


    }
}
