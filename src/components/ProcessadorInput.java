package components;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
/*
Esse processador de eventos funciona somente na tela do jogo.
Ele é responsável por ações quando SPACE, UP_ARROW ou ESCAPE são pressionados ou soltos.
*/
public class ProcessadorInput implements InputProcessor {
    private GameScreen flapPing;

    public ProcessadorInput(GameScreen j) {
        this.flapPing = j;
    }

    public boolean keyDown(int keycode) {
        if (flapPing.FimDeJogo) {
            if (keycode == Keys.ESCAPE) {
                // Quando o jogo encerrar, o usuário pode apertar ESC para voltar para o menu e reiniciar
                flapPing.app.setScreen(new MenuScreen(flapPing.app));
            }
            // Se o jogo estiver sido encerrado, a função keyDown() encerra aqui
            return false;
        }
        // A tecla SPACE faz o jogador A pular e muda a textura
        if (keycode == Keys.W) {
            flapPing.Player1.Jump();
            flapPing.Player1.SetAtualTexture(flapPing.Player1.GetSkins()[1]);
        }
        // Caso o jogo seja cooperativo, a tecla responsável pelo pulo do jogador B é a seta para cima
        if (keycode == Keys.UP && flapPing.COOP) {
            flapPing.Player2.Jump();
            flapPing.Player2.SetAtualTexture(flapPing.Player2.GetSkins()[1]);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (flapPing.FimDeJogo) // Se o jogo for encerrado a função keyUp() termina aqui
            return false;
        
        // Se soltar o SPACE, o pulo do jogador 1 e sua textura são resetados
        if (keycode == Keys.W) {
            flapPing.Player1.PuloPermitido = true;
            flapPing.Player1.SetAtualTexture(flapPing.Player1.GetSkins()[0]);
        }
        // Se Soltar a seta para cima, o pulo e textura do jogador B são resetados
        if (keycode == Keys.UP && flapPing.COOP) {
            flapPing.Player2.PuloPermitido = true;
            flapPing.Player2.SetAtualTexture(flapPing.Player2.GetSkins()[0]);
        }
        return false;
    }

    // Funções não utilizadas

    @Override
    public boolean keyTyped(char character) {
       
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
       
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
       
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
       
        return false;
    }
}
