import { Plugin, Notice, requestUrl, TFile } from "obsidian";
import { SendMarkdownHttpSettings, DEFAULT_SETTINGS, SendMarkdownHttpSettingTab } from "./settings.js";

export default class SendMarkdownHttp extends Plugin {
    settings: SendMarkdownHttpSettings = {} as SendMarkdownHttpSettings;

    async onload() {
        await this.loadSettings();
        console.log("SendMarkdownHttp Plugin geladen mit Einstellungen:", this.settings);

        // Kontextmenü-Eintrag für Markdown-Dateien hinzufügen
        this.registerEvent(this.app.workspace.on("file-menu", (menu, file: TFile) => {
            if (file.extension === "md") {
                menu.addItem((item) => {
                    item
                        .setTitle(this.settings.menuItemTitle) // Titel aus Settings
                        .setIcon(this.settings.menuItemIcon)  // Icon aus Settings
                        .onClick(() => this.sendFileContent(file));
                });
            }
        }));

        // Plugin-Einstellungen verfügbar machen
        this.addSettingTab(new SendMarkdownHttpSettingTab(this.app, this));
    }

    async sendFileContent(file: TFile) {
        try {
            const content = await this.app.vault.read(file);
            if (!this.settings.targetUrl) {
                new Notice("Keine Ziel-URL konfiguriert.");
                return;
            }

            // HTTP-Header vorbereiten
            const headers: Record<string, string> = {
                "Content-Type": "text/plain",
            };

            // Falls API-Key gesetzt ist, als Bearer-Token senden
            if (this.settings.apiKey) {
                headers["Authorization"] = `Bearer ${this.settings.apiKey}`;
            }

            // HTTP-Request senden
            const response = await requestUrl({
                url: this.settings.targetUrl,
                method: "POST",
                headers: headers,
                body: content,
            });

            // Erfolgreiche oder fehlerhafte Antwort behandeln
            if (response.status === 200) {
                new Notice("Datei erfolgreich gesendet.");
            } else {
                new Notice(`Fehler beim Senden: ${response.status}`);
            }
        } catch (error) {
            console.error("Fehler:", error);
            new Notice("Ein unerwarteter Fehler ist aufgetreten.");
        }
    }

    async loadSettings() {
        this.settings = Object.assign({}, DEFAULT_SETTINGS, await this.loadData());
    }

    async saveSettings() {
        await this.saveData(this.settings);
    }
}