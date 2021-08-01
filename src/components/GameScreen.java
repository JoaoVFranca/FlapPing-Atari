package components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
/*
    Essa é a classe da Tela do jogo principal
*/
public class GameScreen implements Screen {

    Application app;

    // Musica de fundo do jogo, que irá fica em loop
    Music GameMusic;

    Sound WinSound; // Som de vitória
    private boolean WinSoundPlayed = false; // Condição para o som de vitória tocar uma única vez

    Sound LoseSound; // Som de derrota
    private boolean LoseSoundPlayed = false; // Condição para o som de derrota tocar uma única vez

    // Variáveis que irão armazenar as texturas de cada entidade do jogo
    private Texture player1Skin, player1SkinJump;
    private Texture player2Skin, player2SkinJump;
    private Texture passaroRightSkin, passaroLeftSkin;
    private Texture bolaSkin;

    // Entidades do jogo
    public Player Player1; 
    public Player Player2; // O Player2 só será inicializado caso o jogo for CO-OP
    public Bola bola;
    public Passaro passaro;
    public CPU bot; // O bot só será inicializado caso o jogo NÃO for CO-OP

    // Barreiras ( só irão ser inicializadas caso a opção de modo barreira do menu estivesse ativada)
    public Parede[] Paredes_A, Paredes_B;

    public boolean WallMode; // Modo com barreira
    public boolean COOP; // Jogo CO-OP? caso não, o Player1 jogará contra a CPU
    public boolean FimDeJogo = false; // Condição para fim de jogo

    // Pontuação do jogador A e B
    public int PontosA = 9, PontosB = 9;

    GameScreen(Application app, boolean WallMode, boolean COOP) {
        this.app = app;

        this.WallMode = WallMode;
        this.COOP = COOP;

        // Tocando Música de fundo do jogo
        GameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/fundo_jogo.mp3"));
        GameMusic.setLooping(true);
        GameMusic.setVolume(.1f);
        GameMusic.play();

        // Setup camera
		app.camera = new OrthographicCamera();
		app.camera.setToOrtho(false, Application.V_WIDTH, Application.V_HEIGHT);

        // Ajuste do tamanho da font do BitMapFont da variável app
        app.font.setScale(2f);

        // Guarda as Texturas armazenadas nos assets dentro das variáveis de textura das entidades
        SetTextures();

        // Criação do jogador A
        Texture[] skinPack =  new Texture[]{player1Skin,player1SkinJump,player1Skin};
        Player1 = new Player(this, 30 + 32, 200, 32, 64, skinPack);

        skinPack = new Texture[]{player2Skin,player2SkinJump,player2Skin};
        // Caso o jogo for Cooperativo, um segundo Player é criado, caso contrário um bot é criado
        if (COOP) {
            // Criação do jogador B
            Player2 = new Player(this, 700, 200, 32, 64, skinPack);
            WinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/som_vitoria.wav"));
        } else  { 
            // Criação do Bot 
            bot = new CPU(this, 700, 200, 64, 64, skinPack);
            WinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/som_vitoria.wav"));
            // O som de derrota é definido e tocado apenas caso o jogador A perca contra o bot
            LoseSound = Gdx.audio.newSound(Gdx.files.internal("sounds/som_derrota.wav"));
        }

        // Criação da bola
        bola = new Bola(this, 50, 50, 8, 8, bolaSkin);

        // Criação do pássaro/foguete
        skinPack = new Texture[]{passaroRightSkin,passaroLeftSkin,passaroRightSkin};
        passaro = new Passaro(this, (int)app.camera.viewportWidth/2, (int)app.camera.viewportHeight/2,
        128, 128, skinPack);

        /*
        Se o modo barreira estiver ativo, as paredes são declaradas e inicializadas
        Lembrando que cada objeto parede é uma entidade 32x32, que em conjunto, simulam
        uma grande muralha.
        */
        if (WallMode) {
            Paredes_A = new Parede[19];
            Paredes_B = new Parede[19];
            for (int i = 0; i < Paredes_A.length; i++) {
                Paredes_A[i] = new Parede(this, 0, i * 32, 32, 32);
                Paredes_B[i] = new Parede(this, (int) app.camera.viewportWidth - 32, i * 32, 32, 32);
            }
        }

        // Coloca um processador de eventos na tela de Jogo
        Gdx.input.setInputProcessor(new ProcessadorInput(this));
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();

    }

    public void update() {
        String pontuacaoA = "" + PontosA;
        String pontuacaoB = "" + PontosB;
        // Se o jogador A atingir 10 pontos
        if (PontosA == 10) {
            pontuacaoA = "W";
            FimDeJogo = true;
            // Toca o som de vitória apenas uma vez
            if (!WinSoundPlayed) {
                WinSound.play(.1f);
                WinSoundPlayed = true;
            }
        }

        // Se o jogador B atingir 10 pontos
        if (PontosB == 10) {
            pontuacaoB = "W";
            FimDeJogo = true;
            /*
            Caso seja um jogo cooperativo, o som de vitoria toca normalmente, mas caso seja
            contra a CPU, o som de derrota é tocado (ambos são tocados apenas uma vez)
            */
            if (COOP && !WinSoundPlayed) {
                WinSound.play(.1f);
                WinSoundPlayed = true;
            } 
            if (!COOP && !LoseSoundPlayed) {
                LoseSound.play(.1f);
                LoseSoundPlayed = true;
            }
        }

        app.batch.begin();

        DesenhaPlacar(pontuacaoA,pontuacaoB);

        DesenhaEntidades();

        /*
        Caso o modo barreira for ativo, as paredes que não foram destruidas são destruidas
        e são testadas se colidiram com a bola
        */
        if (WallMode) {
            draw_and_collide_Walls();
        }

        if (PontosA == 10 || PontosB == 10)
            MensagemVoltarParaMenu(); // Essa função apenas mostra uma mensagem no final do jogo

        app.batch.end();

        /* 
        Caso o jogo tenha encerrado, a função render() termina aqui, e se a musica de fundo
        estiver tocando, ela é encerrada.
        */
        if (FimDeJogo) {
            if (GameMusic.isPlaying())
                GameMusic.stop();
            return;
        }

        MoveEntidades();
    }

    // Essa função chama a função mover específica de cada entidade
    private void MoveEntidades() {
        Player1.Mover();
        if (COOP)
            Player2.Mover();
        else    
            bot.Mover();
        bola.Mover();
        passaro.Mover();
    }

    private void DesenhaEntidades() {
        // Desenha jogador A
        app.batch.draw(Player1.GetSkins()[2], Player1.x - 32, Player1.y,64,64);

        if (COOP)
            app.batch.draw(Player2.GetSkins()[2], Player2.x, Player2.y,64,64); // Desenha jogador B
        else    
            app.batch.draw(bot.GetSkins()[2], bot.x, bot.y,64,64); // Desenha bot

        // Desenha bola e pássaro
        app.batch.draw(bola.skin, bola.x, bola.y, 8, 8);
        app.batch.draw(passaro.GetAtualSkin(), passaro.x, passaro.y, passaro.width, passaro.height);
    }

    private void DesenhaPlacar(String pontuacaoA, String pontuacaoB) {
        app.font.setColor(Color.BLUE);
        app.font.draw(app.batch, pontuacaoA, 50, app.camera.viewportHeight - 50);
        app.font.setColor(Color.RED);
        app.font.draw(app.batch, pontuacaoB, app.camera.viewportWidth - 100, app.camera.viewportHeight - 50);
    }

    private void SetTextures() {
        player1Skin = app.assets.get("assets/player1.png", Texture.class);
        player1SkinJump = app.assets.get("assets/player1fire.png", Texture.class);

        player2Skin = app.assets.get("assets/player2.png", Texture.class);
        player2SkinJump = app.assets.get("assets/player2fire.png", Texture.class);

        passaroLeftSkin = app.assets.get("assets/rocketLeft.png", Texture.class);
        passaroRightSkin = app.assets.get("assets/rocketRight.png", Texture.class);

        bolaSkin = app.assets.get("assets/Bola.png", Texture.class);
    }

    private void MensagemVoltarParaMenu() {
        app.font.setColor(Color.WHITE);
        app.font.draw(app.batch, "Press ESC to main menu", app.camera.viewportWidth/2 - 155, 200);
    }

    private void draw_and_collide_Walls() {
        // Percorre parede por parede em cada vetor
        for (Parede parede : Paredes_A) {
            /* 
            Caso a parede não tenha sido destruida, ela é desenhada e um teste
            de colisão é realizado.
            */
            if (!parede.Destruida) {
                parede.ColisionWall();
                app.batch.draw(parede.Skin, parede.x, parede.y);
            }
        }
        for (Parede parede : Paredes_B) {
            if (!parede.Destruida) {
                parede.ColisionWall();
                app.batch.draw(parede.Skin, parede.x, parede.y);
            }
        }
    }

    @Override
    public void dispose() {
    }

    // Fim ------------------------------------------------

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
    
}
