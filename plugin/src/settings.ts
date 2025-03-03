import { PluginSettingTab, App, Setting } from "obsidian";

// Schnittstelle für die Plugin-Einstellungen
export interface SendMarkdownHttpSettings {
    targetUrl: string;
    apiKey: string;
    menuItemTitle: string; // Neu: Menü-Eintrag Titel
    menuItemIcon: string;  // Neu: Menü-Eintrag Icon
}

// Standardwerte für die Einstellungen
export const DEFAULT_SETTINGS: SendMarkdownHttpSettings = {
    targetUrl: "https://example.com/api",
    apiKey: "",
    menuItemTitle: "Drucken",  // Standardwert
    menuItemIcon: "printer"    // Standardwert
};

// Einstellungs-Tab für Obsidian
export class SendMarkdownHttpSettingTab extends PluginSettingTab {
    plugin: any;

    constructor(app: App, plugin: any) {
        super(app, plugin);
        this.plugin = plugin;
    }

    display(): void {
        const { containerEl } = this;
        containerEl.empty();

        containerEl.createEl("h2", { text: "Einstellungen für SendMarkdownHttp" });

        // Ziel-URL
        new Setting(containerEl)
            .setName("Ziel-URL")
            .setDesc("Gib die URL ein, an die das Markdown gesendet werden soll.")
            .addText((text) =>
                text
                    .setPlaceholder("https://example.com/api")
                    .setValue(this.plugin.settings.targetUrl)
                    .onChange(async (value) => {
                        this.plugin.settings.targetUrl = value.trim();
                        await this.plugin.saveSettings();
                    })
            );

        // API-Key
        new Setting(containerEl)
            .setName("API-Key (optional)")
            .setDesc("Falls gesetzt, wird der API-Key als Bearer-Token in den Request-Header eingefügt.")
            .addText((text) =>
                text
                    .setPlaceholder("API-Key eingeben")
                    .setValue(this.plugin.settings.apiKey)
                    .onChange(async (value) => {
                        this.plugin.settings.apiKey = value.trim();
                        await this.plugin.saveSettings();
                    })
            );

        // Menü-Eintrag Titel
        new Setting(containerEl)
            .setName("Menüeintrag Titel")
            .setDesc("Titel des Menüeintrags im Datei-Kontextmenü.")
            .addText((text) =>
                text
                    .setPlaceholder("Drucken")
                    .setValue(this.plugin.settings.menuItemTitle)
                    .onChange(async (value) => {
                        this.plugin.settings.menuItemTitle = value.trim();
                        await this.plugin.saveSettings();
                    })
            );

        // Menü-Eintrag Icon
        new Setting(containerEl)
            .setName("Menüeintrag Icon")
            .setDesc("Icon für den Menüeintrag im Datei-Kontextmenü. Muss ein gültiger Obsidian-Icon-Name sein.")
            .addText((text) =>
                text
                    .setPlaceholder("printer")
                    .setValue(this.plugin.settings.menuItemIcon)
                    .onChange(async (value) => {
                        this.plugin.settings.menuItemIcon = value.trim();
                        await this.plugin.saveSettings();
                    })
            );
    }
}