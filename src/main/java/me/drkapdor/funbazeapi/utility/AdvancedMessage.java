package me.drkapdor.funbazeapi.utility;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;

/**
 * Обёртка для упрощённого взаимодействия с Chat Component API
 * @author DrKapdor
 */

public class AdvancedMessage {

    private TextComponent component;
    private ComponentBuilder builder;

    public AdvancedMessage() {
        builder = new ComponentBuilder("");
    }

    public AdvancedMessage(String message) {
        component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
    }

    public AdvancedMessage setClickCommand(String command) {
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }

    public AdvancedMessage setClickSuggestion(String suggestion) {
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestion));
        return this;
    }

    public AdvancedMessage setClickUrl(String url) {
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        return this;
    }

    public AdvancedMessage setHoverText(String text) {
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(ChatColor.translateAlternateColorCodes('&', text))}));
        return this;
    }

    public AdvancedMessage append(AdvancedMessage message) {
        if (builder == null)
            builder = new ComponentBuilder(component.getText());
        builder.append(message.getComponent(), ComponentBuilder.FormatRetention.FORMATTING);
        return this;
    }

    public AdvancedMessage append(String message) {
        append(new AdvancedMessage(message));
        return this;
    }

    public AdvancedMessage newLine() {
        append("\n");
        return this;
    }

    public String getRaw() {
        return component.getText();
    }

    public TextComponent getComponent() {
        return component;
    }

    public boolean isBuilder() {
        return builder != null;
    }

    public BaseComponent[] build() {
        if (builder != null)
            return builder.create();
        return null;
    }
}
