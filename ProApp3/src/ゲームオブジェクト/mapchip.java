package ゲームオブジェクト;

import org.newdawn.slick.SpriteSheet;

import 座標系.point;
import 座標系.rect;

public class mapchip extends rect {
	public static SpriteSheet ssheet;
	
	
	
	public mapchip(){
		location = new point();
		size     = new point();
	}
}
