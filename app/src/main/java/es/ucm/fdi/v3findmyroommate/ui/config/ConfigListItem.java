package es.ucm.fdi.v3findmyroommate.ui.config;

public class ConfigListItem {
    private String title;
    private String subtitle;

    public ConfigListItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
