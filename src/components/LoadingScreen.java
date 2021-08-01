package components;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/*
    Tela de carregamento, aqui as texturas são carregadas e então é chamado a tela de menu
*/

public class LoadingScreen implements Screen {

    // Esta variável app possui um batch, um bitMapFont e uma variável assets para armazenar Texturas
    private Application app;

    // Um shapeRenderer para desenhar a barra de carregamento
    private ShapeRenderer shapeRenderer;

    // Variáveis que auxiliam o desenho da barra de carregamento
    private float progress;
    private long delayTime = 0;

    LoadingScreen(Application app) {
        this.app = app;

        this.shapeRenderer = new ShapeRenderer();
        this.progress = 0f;

        SetAssets();
    }

    // Essa função armazena todas as imagens da pasta assets como uma classe do tipo Texture
    private void SetAssets() {
        app.assets.load("assets/player1.png", Texture.class);
        app.assets.load("assets/player1fire.png", Texture.class);
        app.assets.load("assets/player2.png", Texture.class);
        app.assets.load("assets/player2fire.png", Texture.class);
        app.assets.load("assets/rocketRight.png", Texture.class);
        app.assets.load("assets/rocketLeft.png", Texture.class);
        app.assets.load("assets/Bola.png", Texture.class);
        app.assets.load("assets/titulo.png", Texture.class);
        app.assets.load("assets/barreira.png", Texture.class);
        app.assets.load("assets/sinalcerto.png", Texture.class);
    }

    private void update() {
        progress =  lerp(progress,app.assets.getProgress(),.1f);
        if (app.assets.update()) {
            // Caso os arquivos tenham sido carregados completamente
            delayTime++;
            if (delayTime == 100) // Depois do carregamento, há um pequeno tempo de espera
                // Troca de tela de carregamento para o menu principal
                app.setScreen(new MenuScreen(app));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();

        DesenhaBarraCarregamento();

        // Escreve na tela uma mensagem que está carregando
        app.batch.begin();
        app.font.draw(app.batch, "LOADING...", 50, 50);
        app.batch.end();
    }

    private void DesenhaBarraCarregamento() {
        // Desenha barra total fixa
        shapeRenderer.begin(ShapeType.FilledRectangle);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.filledRect(30, app.camera.viewportHeight/2 - 8, app.camera.viewportWidth - 62, 20);
        shapeRenderer.end();

        // Desenha uma barra secundária com o tamanho variado de acordo com o progresso do carregamento
        shapeRenderer.begin(ShapeType.FilledRectangle);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.filledRect(32, app.camera.viewportHeight/2 - 6, progress * (app.camera.viewportWidth - 66), 16);
        shapeRenderer.end();
    }

    public float lerp(float a, float b, float f) {
        return (float)(a * (1.0 - f)) + (b * f);
    }

    // Funções não utilizadas --------------------------------------------

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void show() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void dispose() {
        
    }
    
}
