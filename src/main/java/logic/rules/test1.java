package logic.rules;

import logic.tiles.*;

public class test1 {
    public static void main(String[] arg){
        Rule rule = new Rule();
        VictoryRule victoryRule = new VictoryRule();
        HandTile handTile = new HandTile();
        handTile.addTile(new CharacterTile("Character",1));
        handTile.addTile(new CharacterTile("Character",1));
        handTile.addTile(new CharacterTile("Character",1));
        handTile.addTile(new BambooTile("Bamboo",3));
        handTile.addTile(new BambooTile("Bamboo",3));
        handTile.addTile(new BambooTile("Bamboo",3));
        handTile.addTile(new DotTile("Dot",7));
        handTile.addTile(new DotTile("Dot",7));
        handTile.addTile(new DotTile("Dot",7));
        handTile.addTile(new DotTile("Dot",7));
        handTile.addTile(new HonorTile("East",1));
        handTile.addTile(new HonorTile("East",1));
        handTile.addTile(new HonorTile("East",1));
        handTile.addTile(new HonorTile("West",3));
        handTile.addTile(new HonorTile("West",3));
        handTile.addTile(new HonorTile("West",3));
        System.out.println(rule.getSequenceNumber(handTile.getTileSet()));
        System.out.println(rule.getTripletNumber(handTile.getTileSet()));
        System.out.println(rule.getPairNumber(handTile.getTileSet()));
        System.out.println(rule.getKongNumber(handTile.getTileSet()));
        System.out.println(victoryRule.allTriplets(handTile));


    }
}
