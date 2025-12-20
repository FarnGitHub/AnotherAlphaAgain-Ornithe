package farn.another_alpha_again.sub;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;

public class GuiException extends Screen {
    private RuntimeException e;

    public GuiException(RuntimeException exception){
        e = exception;
    }

    public void init() {
        this.buttons.clear();
        this.buttons.add(new ButtonWidget(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Back to title screen"));
    }

    protected void buttonClicked(ButtonWidget guiButton1) {
        if(guiButton1.active) {
            if(guiButton1.id == 0) {
                this.minecraft.openScreen(new TitleScreen());
            }
        }
    }

    public void render(int i1, int i2, float f3) {
        this.renderBackground();
        this.drawCenteredString(this.textRenderer, "An exception has occurred!", this.width / 2, this.height / 4 - 60 + 20, 0xFFFFFF);
        this.drawString(this.textRenderer, "Minecraft ran into an unexpected problem:", this.width / 2 - 140, this.height / 4 - 60 + 60, 10526880);
        this.drawString(this.textRenderer, "", this.width / 2 - 140, this.height / 4 - 60 + 60 + 18, 10526880);
		String message = e.getMessage();
		if(e.getMessage() == null) {
			message = e.toString();
		}
        this.drawCenteredString(this.textRenderer, message, this.width / 2, this.height / 4 - 60 + 60 + 27, 0xFFFFFF00);
        this.drawString(this.textRenderer, "To prevent more problems, the current game has quit.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 60, 10526880);
        super.render(i1, i2, f3);
    }
}
