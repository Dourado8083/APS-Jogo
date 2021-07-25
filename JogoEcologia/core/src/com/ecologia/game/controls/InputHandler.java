package com.ecologia.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.ecologia.game.sprites.Player;
import com.ecologia.game.sprites.Player.State;

public class InputHandler {
	
	public InputHandler(Player player, float deltaTime) {
			if (player.currentState != State.HURT){
				//Classe que cuida dos inputs
				//Aplicando fisica cinetica das classes box2d
				//velocidade em float por conta da precisao
				if ((Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x < 3f)){
					if (player.b2body.getLinearVelocity().x < 0){
						player.b2body.setLinearVelocity(0,player.b2body.getLinearVelocity().y);
					}
					player.b2body.applyLinearImpulse(new Vector2(0.13f, 0), player.b2body.getWorldCenter(), true);
				}
				if ((Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x > -3f)){
					if (player.b2body.getLinearVelocity().x > 0){
						player.b2body.setLinearVelocity(0,player.b2body.getLinearVelocity().y);
					}
					player.b2body.applyLinearImpulse(new Vector2(-0.13f, 0), player.b2body.getWorldCenter(), true);
				}
				
				if ((Gdx.input.isKeyJustPressed(Input.Keys.W) && player.b2body.getLinearVelocity().y == 0f)){
					player.b2body.applyLinearImpulse(new Vector2(0f, 4.0f), player.b2body.getWorldCenter(), true);
				}
				
				if (player.b2body.getLinearVelocity().x > 3f) {
					player.b2body.setLinearVelocity(new Vector2(3f, player.b2body.getLinearVelocity().y));			
				}
				
				if (player.b2body.getLinearVelocity().x < -3f) {
					player.b2body.setLinearVelocity(new Vector2(-3f, player.b2body.getLinearVelocity().y));
				}
				//desacelera o player para nao ficar "escorregadio"
				//age de forma semelhante a fricção
				//apenas entra em ação se o jogador nao esta tentando se movimentar
				if(( Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D)) != true && player.b2body.getLinearVelocity().x > 0) {
					player.b2body.applyLinearImpulse(new Vector2((player.b2body.getLinearVelocity().x/2)* -1, 0), player.b2body.getWorldCenter(), true);
				}
				if(( Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D)) != true && player.b2body.getLinearVelocity().x < 0) {
					player.b2body.applyLinearImpulse(new Vector2((player.b2body.getLinearVelocity().x/2)* -1, 0), player.b2body.getWorldCenter(), true);
				}
			}
			
			
		}
}
	/*public void damagePlayer() {
		
		
	}*/
