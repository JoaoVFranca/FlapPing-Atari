package components;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

// Essa classe é utilizada somente na Tela de Menu, ela serve de processador de eventos durante a tela
// de menu.

public class MenuInputProcessor implements InputProcessor {

    MenuScreen menu;

    MenuInputProcessor(MenuScreen menu) {
        this.menu = menu;
    }

    @Override
    public boolean keyDown(int keycode) {
        // Trocar a opção selecionada no menu ----------
        if (keycode == Keys.W || keycode == Keys.UP) {
            if (menu.currentOption > 1) {
                menu.currentOption--;
                menu.OptionSound.play(.5f);
            }
        }
        if (keycode == Keys.S || keycode == Keys.DOWN) {
            if (menu.currentOption < menu.MaxOption) {
                menu.currentOption++;
                menu.OptionSound.play(.5f);
            }
        }
        // ---------------------------------------------
        
        // quando o usuário confirma a opcao selecionada
        if (keycode == Keys.ENTER) {
            menu.Confirmado.play(.5f); // Toca o som de confirmado
            switch (menu.currentOption) {
                case 1:
                    if (menu.MenuMusic.isPlaying())
                        menu.MenuMusic.stop();
                    // Inicia o jogo CO-OP
                    menu.app.setScreen(new GameScreen(menu.app, menu.WallMode, true));
                    break;
                case 2:
                    if (menu.MenuMusic.isPlaying())
                        menu.MenuMusic.stop();
                    // Inicia o jogo vs CPU
                    menu.app.setScreen(new GameScreen(menu.app, menu.WallMode, false));
                    break;
                case 3:
                    // Ativa ou desativa o modo de jogo com barreira
                    menu.WallMode = !menu.WallMode;
                    break;
            }

        }
        return false;
    }

    // Funções não utilizadas

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

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
