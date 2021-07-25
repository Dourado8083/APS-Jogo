package com.ecologia.game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ecologia.game.JogoEcologia;
import com.ecologia.game.scenes.Hud;
import com.ecologia.game.screens.Fase1;
import com.ecologia.game.sprites.Caminhao;
import com.ecologia.game.sprites.Lixo;
import com.ecologia.game.sprites.Plataformas;
import com.ecologia.game.sprites.Player;
import com.ecologia.game.sprites.Vidro;

public class LevelRenderer {

	public OrthographicCamera gamecam;	
	
	
	//viewport: ajusta o que a camera vai poder ver e como
	public Viewport gamePort;
	
	//TmxMapLoader: loada o mapa do Tiled (Editor de mapas)
	//TiledMap: Objeto do TiledMap
	//OrthogonalTiledMapRenderer: renderiza o mapa na tela
	public TmxMapLoader mapLoader;
	public TiledMap map;
	public OrthogonalTiledMapRenderer renderer; 
	
	//Box2d:
	//library que atribui fisica ao mapa e objetos
	//Usamos para definir colisões.
	//Ela encapsula seu jogo em um "mundo"/caixa
	//dentro do world, ficam os bodies
	//dentro dos bodies, ficam as propriedades como fixture
	//dentro de fixtures, ficam o formato(shape), densidade, etc
	public World world;
	public Box2DDebugRenderer b2debug;
	
	
	public Player player;
	
	//importa o arquivo de texturas
	public TextureAtlas tAtlas;

	public JogoEcologia game;
	Texture texture;
	
	public Hud hud;
	
	private float gameTime = 240;
	
	public LevelRenderer(String mapName, JogoEcologia game) {
		this.game = game;
		
		this.tAtlas = new TextureAtlas("hoodie.pack");
		this.gamecam = new OrthographicCamera();
		
		this.mapLoader = new TmxMapLoader();
		this.map = mapLoader.load("city-map-" + mapName+".tmx");
		this.renderer = new OrthogonalTiledMapRenderer(map, 1 /JogoEcologia.PPM);
		
		//fitviewport: configura a resolução de modo que mantem o aspect ratio usando barras pretas
		this.gamePort = new FitViewport(JogoEcologia.V_WIDTH  /JogoEcologia.PPM,JogoEcologia.V_HEIGHT /JogoEcologia.PPM, gamecam);
				
		//camera por padrao fica em 0, 0, 0
		//isso significa que o jogo só ocuparia o quadrante positivo da tela
		//ou seja, 25%
		//entao, mudamos a posicao da camera para o centro do viewport.
		this.gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		
		//parametro numerica: gravidade
		//parametro booleana: se objetos asleep ficam em rest (perdem simulacao fisica)
		this.world = new World(new Vector2(0,-10), true);
		
		b2debug = new Box2DDebugRenderer();
		//emite as lihas (hitboxes)
		b2debug.setDrawBodies(false);
		b2debug.setDrawContacts(false);
		b2debug.setDrawJoints(false);
		b2debug.setDrawVelocities(false);
		//define do que consiste o Body, Shape e Fixture
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body = null;
		
		//Define o jogador
		player = new Player(world,this);
		hud = new Hud(game.batch, mapName, gameTime);
		
		
		
		//Conta layers de baixo para cima (no editor Tiled), comecando do 0
		//Lida com o chao
		//pega os objetos ellipse/circle/rectangles definidos no Tiled e define o corpo de acordo com o que deve ser
		for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			
			//Dinamico: Se move
			//Estatico: nao se move
			//Kinematic: nao é afetado por forcas como gravidade, mas é afetado por forcas como velocidade
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth()/2) /JogoEcologia.PPM, (rect.getY() + rect.getHeight() /2)/JogoEcologia.PPM);
			
			//adiciona o body ao world do box2d
			body = world.createBody(bdef);
			
			//Define o shape da fixture e adiciona ao body
			shape.setAsBox((rect.getWidth() / 2) /JogoEcologia.PPM, (rect.getHeight() /2) /JogoEcologia.PPM);
			fdef.shape = shape;
			
			body.createFixture(fdef);
			
			//As colisoes do mundo vao funcionar conforme definidas na classe WorldContactListener
			//ContactListener: faz o gerenciamento das colisoes e interações dos objetos do jogo
			world.setContactListener(new WorldContactListener());
			
		}
		//Layer das plataformas
		for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				Plataformas plataformas = new Plataformas(world,  map, bdef, fdef, rect, body, shape);
		
		}
		
		//layer dos interactibles (lixo)
		int totLixo = 0;
		for (MapObject object : map.getLayers().get(11).getObjects().getByType(EllipseMapObject.class)) {	
			Ellipse el = ((EllipseMapObject) object).getEllipse();
			Lixo f1Lixo = new Lixo(world, bdef, map, fdef, el );
			totLixo++;
			
		}		
		
		//layer dos interactibles (vidro/obstaculo)
		for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {	
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			Vidro vidros = new Vidro(world, bdef, map, fdef, rect, player );			
		}	
		
		//Layer do caminhao (chegada do nivel)
		for (MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			Caminhao caminhao = new Caminhao(world, bdef, map, fdef, rect);
	
	}
				
				JogoEcologia.totalLixo = totLixo;
				

	}
	
	public TextureAtlas getAtlas() {
		//retorna o TextureAtlas do player
		return tAtlas;
	}
	
	public void update(float deltaTime, boolean passouFase) {
		//Classe chamada para atualizar o movimento do jogador e a camera a cada execucao do metodo render (60x por segundo)	
		
		InputHandler inputs = new InputHandler(player,deltaTime);
		//Diz quantas vezes por segundo calcular a colisão
		//quanto maior o numero, mais preciso o calculo mas mais demora para calcular
		world.step(1/60f, 6, 2);
		
		player.update(deltaTime);
		
		//Camera segue o jogador verticalmente e horizontalmente
		gamecam.position.x = player.b2body.getPosition().x;
		gamecam.position.y = player.b2body.getPosition().y;
		
		//camera PRECISA ser atualizada cada iteracao de render
		gamecam.update();
		
		//Só rendereiza o que nossa camera pode ver
		renderer.setView(gamecam);
		
		TimeSystem();
		hud.update(passouFase, player, gameTime);
		
		
	}
	
	public void gameOver() {
		
		
	}	
	
	
	
	
	public void show() {
		
	}

	
	public void render(float delta, boolean passouFase) {
		update(delta, passouFase);
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Jogo só carrega o que a camera mostra
		renderer.render();
		
		b2debug.render(world, gamecam.combined);
				
		//Carrega as texturas usando o spriteBatch da JogoEcologia.java
		//spriteBatches sao feitas para carregar o maximo de texturas de uma só vez
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		//game.batch.end();
		game.batch.setProjectionMatrix(gamecam.combined);
		game.batch.begin();
		player.draw(game.batch);
		
		game.batch.end();
		
		}

	
	public void TimeSystem() {
		//render roda 60x por segundo, entao tira aproximadamente 1/60 segundos do timer cada iteração de render
		if (gameTime > 0){
		gameTime -= 0.01667;
		}if (this.gameTime <= 0){
			player.playerHurt = true;
		}
		
	}
	
	public void resize(int width, int height) {
		//Atualiza as dimensoes do 

		
		gamePort.update(width,height);
	}
	
}
	
	
	

