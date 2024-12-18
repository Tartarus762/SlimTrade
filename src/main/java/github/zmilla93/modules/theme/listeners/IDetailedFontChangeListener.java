package github.zmilla93.modules.theme.listeners;

// FIXME: Is this needed? It is probably better to just use IFontChangeListener to avoid random bugs.
public interface IDetailedFontChangeListener {

    void onFontStyleChanged();

    void onFontSizeChanged();

    void onIconSizeChanged();

}
