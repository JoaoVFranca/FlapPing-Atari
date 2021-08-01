package components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

/*
    Tela de menu, aqui o usuário escolhe a opção que quer jogar:
        Seta para cima ou W: opção acima
        Seta para baixo ou S: opção abaixo
        ENTER: confirma a seleção
    Depois a tela de jogo é carregada de acordo com as opções selecionadas pelo usuário
*/

public class MenuScreen implements Screen{

    Application app;

    // Imagem com o Título do jogo
    private Texture TitleSprite;
    // Imagem para o indicador de opção selecionada no menu
    private Texture seta;
    // Imagem para o indicador do modo com barreira, se está ativo o menu ira mostrar essa 
    // textura ao lado da opção do modo bareira.
    private Texture sinalCerto;

    // Musica de fundo para o menu, que irá ficar em loop
    public Music MenuMusic;
    // Som de troca de opção selecionada
    public Sound OptionSound;
    // Som de confirmação da opção selecionada
    public Sound Confirmado;

    // Vetor com o texto das opções
    public String[] options = {"VS", "CPU", "MODO BARREIRA"};
    // Número de opções disponíveis
    public int MaxOption = 3;
    // Índice da opção selecionada
    public int currentOption = 1;
    // Indica se o modo com barreira está ativo ou não
    public boolean WallMode = false;
    
    MenuScreen(Application app) {
        this.app = app;

        // Aumento do tamanho da fonte
        app.font.setScale(3f);

        // Colocando um processador de eventos nessa tela de menu
        Gdx.input.setInputProcessor(new MenuInputProcessor(this));

        // Guardando as texturas
        seta = app.assets.get("assets/rocketRight.png");
        TitleSprite = app.assets.get("assets/titulo.png");
        sinalCerto = app.assets.get("assets/sinalcerto.png");

        // Coloca a musica de fundo pra tocar
        MenuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/fundo_menu.mp3"));
        MenuMusic.setLooping(true);
        MenuMusic.setVolume(.1f);
        MenuMusic.play();

        // Guardando os sons
        OptionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/som_menu.wav"));
        Confirmado = Gdx.audio.newSound(Gdx.files.internal("sounds/som_confirmacao.wav"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        app.batch.begin();

        //Desenha a imagem do título
        app.batch.draw(TitleSprite, 150, 325, 512, 256);

        // Escreve as opções do menu
        DesenhaMenuOpcoes();

        // Desenha o indicador da opção selecionada
        DesenhaIndicadorOpcao();

        // Se o modo com barreira estiver ativado, um indicador será desenhado
        DesenhaIndicadorModoBarreira();
    
        app.batch.end();

    }

    private void DesenhaIndicadorModoBarreira() {
        if (WallMode)
            app.batch.draw(sinalCerto, app.camera.viewportWidth - 210, app.camera.viewportHeight/2 - 210, 64, 64);
    }

    private void DesenhaIndicadorOpcao() {
        if (currentOption == 1)
            app.batch.draw(seta, app.camera.viewportWidth/2 - 125, app.camera.viewportHeight/2 - 80, 64, 64);
        else if (currentOption == 2)
            app.batch.draw(seta, app.camera.viewportWidth/2 - 150, app.camera.viewportHeight/2 - 150, 64, 64);
        else if (currentOption == 3)
            app.batch.draw(seta, 130, app.camera.viewportHeight/2 - 220, 64, 64);
    }

    private void DesenhaMenuOpcoes() {
        app.font.setColor(Color.WHITE);
        app.font.draw(app.batch, options[0], app.camera.viewportWidth/2 - 40 , app.camera.viewportHeight/2 - 30);
        app.font.draw(app.batch, options[1], app.camera.viewportWidth/2 - 70, app.camera.viewportHeight/2 - 100);
        app.font.draw(app.batch, options[2], 200, app.camera.viewportHeight/2 - 170);
    }

    // Funções não utilizadas 

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
