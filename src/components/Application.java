package components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/*
    Essa é a classe da aplicação
*/

public class Application extends Game{

    // Título do jogo, largura e altura da tela
    public static final String TITULO = "Flap Ping";
    public static final int V_WIDTH = 800;
    public static final int V_HEIGHT = 600;

    public OrthographicCamera camera;

    // batch utilizado para desenhar
    public SpriteBatch batch; 

    // BitMapFont utilizado para escrita na tela
    public BitmapFont font; 

    // Essa variável contem todas as Texturas usadas no jogo
    public AssetManager assets;

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
        new LwjglApplication(new Application(), TITULO, V_WIDTH, V_HEIGHT, false);
    }

    @Override
    public void create() {
      
        assets = new AssetManager();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
        batch = new SpriteBatch();

        font = new BitmapFont();

        // Troca para a tela de Carregamento
        this.setScreen(new LoadingScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        assets.dispose();
        this.getScreen().dispose();
    }
}
